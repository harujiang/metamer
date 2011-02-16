/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.metamer.ftest.richInplaceSelect;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardNoRequest;
import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardXhr;
import static org.jboss.test.selenium.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.test.selenium.dom.Event;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractMetamerTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richInplaceSelect/fAjax.xhtml.
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision$
 */
public class TestRichInplaceSelectFAjax extends AbstractMetamerTest {

    private JQueryLocator select = pjq("span[id$=inplaceSelect]");
    private JQueryLocator label = pjq("span.rf-is-lbl");
    private JQueryLocator input = pjq("input[id$=inplaceSelectInput]");
    private JQueryLocator popup = pjq("span.rf-is-lst-cord");
    private JQueryLocator edit = pjq("span.rf-is-edit");
    private JQueryLocator options = jq("span.rf-is-opt:eq({0})"); // 00..49
    private JQueryLocator output = pjq("span[id$=output]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInplaceSelect/fAjax.xhtml");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10538")
    public void testClick() {
        guardNoRequest(selenium).click(select);
        assertFalse(selenium.belongsClass(edit, "rf-is-none"), "Edit should not contain class rf-is-none when popup is open.");
        assertTrue(selenium.isDisplayed(popup), "Popup should be displayed.");

        for (int i = 0; i < 50; i++) {
            assertTrue(selenium.isDisplayed(options.format(i)), "Select option " + i + " should be displayed.");
        }

        String[] selectOptions = {"Alabama", "Hawaii", "Massachusetts", "New Mexico", "South Dakota"};
        for (int i = 0; i < 50; i += 10) {
            assertEquals(selenium.getText(options.format(i)), selectOptions[i / 10], "Select option nr. " + i);
        }

        selenium.click(options.format(10));
        guardXhr(selenium).fireEvent(input, Event.BLUR);
        waitGui.failWith("Output did not change.").until(textEquals.locator(output).text("Hawaii"));

        assertTrue(selenium.belongsClass(select, "rf-is-c-s"), "New class should be added to inplace select.");
        assertTrue(selenium.belongsClass(edit, "rf-is-none"), "Edit should contain class rf-is-none when popup is closed.");

        assertEquals(selenium.getText(label), "Hawaii", "Label should contain selected value.");

        phaseInfo.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed: null -> Hawaii");
    }
}
