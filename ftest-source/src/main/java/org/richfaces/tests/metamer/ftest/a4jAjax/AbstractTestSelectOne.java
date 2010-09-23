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
package org.richfaces.tests.metamer.ftest.a4jAjax;

import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardXhr;
import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.locator.option.OptionLocatorFactory.optionValue;
import static org.testng.Assert.assertEquals;

import javax.faces.event.PhaseId;

import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractMetamerTest;

/**
 * Abstract test case for testing h:selectOneMenu and h:selectOneListbox with a4j:ajax.
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision$
 */
public abstract class AbstractTestSelectOne extends AbstractMetamerTest {

    private JQueryLocator output1 = pjq("span[id$=output1]");
    private JQueryLocator output2 = pjq("span[id$=output2]");

    public void testClick(JQueryLocator input) {
        guardXhr(selenium).select(input, optionValue("Audi"));

        String outputValue = waitGui.failWith("Page was not updated").waitForChangeAndReturn("[Ferrari, Lexus]",
                retrieveText.locator(output1));

        assertEquals(outputValue, "Audi", "Wrong output1");
        assertEquals(selenium.getText(output2), "Audi", "Wrong output2");
    }

    public void testBypassUpdates(JQueryLocator input) {
        JQueryLocator time = jq("span[id$=requestTime]");
        String timeValue = selenium.getText(time);

        selenium.click(pjq("input[type=checkbox][id$=bypassUpdatesInput]"));
        selenium.waitForPageToLoad();

        guardXhr(selenium).select(input, optionValue("Audi"));
        waitGui.failWith("Page was not updated").waitForChange(timeValue, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "Ferrari", "Output should not change");
        assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
                PhaseId.RENDER_RESPONSE);
    }

    public void testData(JQueryLocator input) {
        selenium.type(pjq("input[type=text][id$=dataInput]"), "RichFaces 4 data");
        selenium.waitForPageToLoad();

        selenium.type(pjq("input[type=text][id$=oncompleteInput]"), "data = event.data");
        selenium.waitForPageToLoad();

        JQueryLocator time = jq("span[id$=requestTime]");
        String timeValue = selenium.getText(time);

        guardXhr(selenium).select(input, optionValue("Audi"));
        waitGui.failWith("Page was not updated").waitForChange(timeValue, retrieveText.locator(time));

        String data = selenium.getEval(new JavaScript("window.data"));
        assertEquals(data, "RichFaces 4 data", "Data sent with ajax request");
    }

    public void testImmediate(JQueryLocator input) {
        JQueryLocator time = jq("span[id$=requestTime]");
        String timeValue = selenium.getText(time);

        selenium.click(pjq("input[type=checkbox][id$=immediateInput]"));
        selenium.waitForPageToLoad();

        guardXhr(selenium).select(input, optionValue("Audi"));
        waitGui.failWith("Page was not updated").waitForChange(timeValue, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "Audi", "Output should change");
        assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
                PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
    }

    public void testImmediateBypassUpdates(JQueryLocator input) {
        JQueryLocator time = jq("span[id$=requestTime]");
        String timeValue = selenium.getText(time);

        selenium.click(pjq("input[type=checkbox][id$=bypassUpdatesInput]"));
        selenium.waitForPageToLoad();
        selenium.click(pjq("input[type=checkbox][id$=immediateInput]"));
        selenium.waitForPageToLoad();

        guardXhr(selenium).select(input, optionValue("Audi"));
        waitGui.failWith("Page was not updated").waitForChange(timeValue, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "Ferrari", "Output should not change");
        assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);
    }

    public void testLimitRender(JQueryLocator input) {
        selenium.click(pjq("input[type=checkbox][id$=limitRenderInput]"));
        selenium.waitForPageToLoad();

        JQueryLocator time = jq("span[id$=requestTime]");
        String timeValue = selenium.getText(time);

        guardXhr(selenium).select(input, optionValue("Audi"));
        waitGui.failWith("Page was not updated").waitForChange("", retrieveText.locator(output1));

        assertEquals(selenium.getText(time), timeValue, "Ajax-rendered a4j:outputPanel shouldn't change");
    }

    public void testEvents(JQueryLocator input) {
        selenium.type(pjq("input[type=text][id$=onbeginInput]"), "metamerEvents += \"begin \"");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[type=text][id$=onbeforedomupdateInput]"), "metamerEvents += \"beforedomupdate \"");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[type=text][id$=oncompleteInput]"), "metamerEvents += \"complete \"");
        selenium.waitForPageToLoad();

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));

        guardXhr(selenium).select(input, optionValue("Audi"));
        waitGui.failWith("Page was not updated").waitForChange("", retrieveText.locator(output1));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events[0], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[1], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[2], "complete", "Attribute oncomplete doesn't work");
    }

    public void testRender(JQueryLocator input) {
        selenium.type(pjq("input[type=text][id$=renderInput]"), "output1");
        selenium.waitForPageToLoad();

        guardXhr(selenium).select(input, optionValue("Audi"));
        String outputValue = waitGui.failWith("Page was not updated").waitForChangeAndReturn("",
                retrieveText.locator(output1));

        assertEquals(outputValue, "Audi", "Wrong output1");
        assertEquals(selenium.getText(output2), "Ferrari", "Wrong output2");
    }
}