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
package org.richfaces.tests.metamer.ftest.a4jStatus;

import static org.testng.Assert.assertEquals;

import org.jboss.cheiron.halt.XHRHalter;
import org.jboss.test.selenium.locator.ElementLocator;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.waiting.retrievers.TextRetriever;
import org.richfaces.tests.metamer.ftest.AbstractMetamerTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public abstract class AbstracStatusTest extends AbstractMetamerTest {
    JQueryLocator button1 = pjq("input[id$=button1]");
    JQueryLocator button2 = pjq("input[id$=button2]");
    JQueryLocator buttonError = pjq("input[id$=button3]");
    JQueryLocator status = pjq("span[id$=status]");

    StatusAttributes attributes = new StatusAttributes();

    TextRetriever retrieveStatus = retrieveText.locator(status);

    XHRHalter haltHandler;
    
    @BeforeMethod
    public void cleanXHRHalter() {
        haltHandler = null;
    }
    
    protected XHRHalter getCurrentXHRHalter() {
        if (haltHandler == null) {
            haltHandler = XHRHalter.getHandleBlocking();
        } else {
            haltHandler.waitForOpen();
        }
        return haltHandler;
    }
    
    void testRequestButton(ElementLocator<?> button, String startStatusText, String stopStatusText) {
        XHRHalter.enable();
        selenium.click(button);
        XHRHalter halt = getCurrentXHRHalter();
        assertEquals(retrieveStatus.retrieve(), startStatusText);
        halt.complete();
        waitAjax.waitForChange(startStatusText, retrieveStatus);
        assertEquals(retrieveStatus.retrieve(), stopStatusText);
        XHRHalter.disable();
    }
}
