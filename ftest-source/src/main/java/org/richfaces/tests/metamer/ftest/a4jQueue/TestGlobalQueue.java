/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.richfaces.tests.metamer.ftest.a4jQueue;

import static org.jboss.test.selenium.utils.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.cheiron.halt.XHRHalter;
import org.jboss.cheiron.halt.XHRState;
import org.richfaces.tests.metamer.ftest.AbstractMetamerTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TestGlobalQueue extends AbstractMetamerTest {

    QueueModel queue = new QueueModel();
    QueueAttributes attributes = new QueueAttributes();

    @Inject
    @Use(empty = false)
    Integer requestDelay;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jQueue/globalQueue.xhtml");
    }

    /**
     * Tests delay between time last event occurs and time when event triggers request (begin).
     */
    @Test
    @Use(field = "requestDelay", ints = { 0, 500, 1500, 5000 })
    public void testRequestDelay() {
        attributes.setRequestDelay(requestDelay);

        queue.initializeTimes();

        for (int i = 0; i < 5; i++) {
            queue.fireEvent(1);
            queue.checkTimes(requestDelay);
        }

        queue.checkDeviationMedian(requestDelay);
    }

    /**
     * Events from one source should be stacked as occurs, while last event isn't delayed by configured requestDelay.
     */
    @Test
    public void testMultipleRequestsWithDelay() {
        attributes.setRequestDelay(3000);

        queue.initializeCounts();

        XHRHalter.enable();

        queue.fireEvent(4);
        XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.send();
        handle.complete();

        queue.checkCounts(4, 1, 1);

        queue.fireEvent(3);
        handle.waitForOpen();
        handle.send();
        handle.complete();

        queue.checkCounts(7, 2, 2);

        XHRHalter.disable();
    }

    /**
     * <p>
     * When no requestDelay (0) is set, events should fire request immediately.
     * </p>
     * 
     * <p>
     * However, when one event is waiting in queue for processing of previous request, events should be stacked.
     * </p>
     */
    @Test
    public void testMultipleRequestsWithNoDelay() {
        attributes.setRequestDelay(0);

        queue.initializeCounts();

        XHRHalter.enable();

        queue.fireEvent(1);
        queue.checkCounts(1, 1, 0);

        XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.send();

        queue.fireEvent(1);
        queue.checkCounts(2, 1, 0);

        handle.complete();
        queue.checkCounts(2, 2, 1);

        handle.waitForOpen();
        handle.send();
        queue.fireEvent(4);
        queue.checkCounts(6, 2, 1);

        handle.complete();
        queue.checkCounts(6, 3, 2);

        handle.waitForOpen();
        handle.send();
        queue.fireEvent(1);
        queue.checkCounts(7, 3, 2);

        handle.complete();
        queue.checkCounts(7, 4, 3);

        handle.waitForOpen();
        handle.send();
        queue.checkCounts(7, 4, 3);

        handle.complete();
        queue.checkCounts(7, 4, 4);

        XHRHalter.disable();
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9328")
    public void testRendered() {
        attributes.setRequestDelay(1500);
        attributes.setRendered(false);

        queue.initializeTimes();
        queue.fireEvent(1);

        // check that no requestDelay is applied while renderer=false
        queue.checkTimes(0);
        // TODO should check that no attributes is applied with renderes=false
    }

    // TODO not implemented yet
    public void testTimeout() {
        attributes.setRequestDelay(0);
        attributes.setTimeout(1000);

        XHRHalter.enable();

        queue.fireEvent(1);
        XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.continueBefore(XHRState.COMPLETE);
        queue.fireEvent(10);

        XHRHalter.disable();

        // fireEvents(1);
        // fireEvents(1);
    }

    // TODO not implemented yet
    public void testIgnoreDuplicatedResponses() {
        attributes.setIgnoreDupResponses(true);

        XHRHalter.enable();
        queue.fireEvent(1);
        XHRHalter handle = XHRHalter.getHandleBlocking();
        handle.send();
        queue.fireEvent(1);
        handle.complete();
        handle.waitForOpen();
    }
}