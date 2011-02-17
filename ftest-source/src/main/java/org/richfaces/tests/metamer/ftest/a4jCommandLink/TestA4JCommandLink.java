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
package org.richfaces.tests.metamer.ftest.a4jCommandLink;

import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardNoRequest;
import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardXhr;
import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;
import javax.faces.event.PhaseId;

import org.jboss.test.selenium.dom.Event;
import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractMetamerTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/a4jCommandLink/simple.xhtml
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision$
 */
public class TestA4JCommandLink extends AbstractMetamerTest {

    private JQueryLocator input = pjq("input[id$=input]");
    private JQueryLocator link = pjq("a[id$=a4jCommandLink]");
    private JQueryLocator output1 = pjq("span[id$=output1]");
    private JQueryLocator output2 = pjq("span[id$=output2]");
    private JQueryLocator output3 = pjq("span[id$=output3]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jCommandLink/simple.xhtml");
    }

    @Test(groups = "client-side-perf")
    public void testSimpleClick() {
        guardNoRequest(selenium).typeKeys(input, "RichFaces 4");
        guardXhr(selenium).click(link);

        waitGui.until(textEquals.locator(output1).text("RichFaces 4"));

        String output = selenium.getText(output1);
        assertEquals(output, "RichFaces 4", "output1 when 'RichFaces 4' in input");

        output = selenium.getText(output2);
        assertEquals(output, "RichFa", "output2 when 'RichFaces 4' in input");

        output = selenium.getText(output3);
        assertEquals(output, "RICHFACES 4", "output3 when 'RichFaces 4' in input");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9665")
    public void testSimpleClickUnicode() {
        guardNoRequest(selenium).typeKeys(input, "ľščťžýáíéňô");
        guardXhr(selenium).click(link);

        waitGui.until(textEquals.locator(output1).text("ľščťžýáíéňô"));

        String output = selenium.getText(output1);
        assertEquals(output, "ľščťžýáíéňô", "output1 when 'ľščťžýáíéňô' in input");

        output = selenium.getText(output2);
        assertEquals(output, "ľščťžý", "output2 when 'ľščťžýáíéňô' in input");

        output = selenium.getText(output3);
        assertEquals(output, "ĽŠČŤŽÝÁÍÉŇÔ", "output3 when 'ľščťžýáíéňô' in input");
    }

    @Test
    public void testAction() {
        selenium.click(pjq("input[name$=actionInput][value=doubleStringAction]"));
        selenium.waitForPageToLoad();
        selenium.typeKeys(input, "RichFaces 4");
        guardXhr(selenium).click(link);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4"));
        assertEquals(selenium.getText(output2), "RichFaces 4RichFaces 4", "output2 when 'RichFaces 4' in input and doubleStringAction selected");

        selenium.click(pjq("input[name$=actionInput][value=first6CharsAction]"));
        selenium.waitForPageToLoad();
        selenium.typeKeys(input, "RichFaces 4ň");
        guardXhr(selenium).click(link);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4ň"));
        assertEquals(selenium.getText(output2), "RichFa", "output2 when 'RichFaces 4ň' in input and first6CharsAction selected");

        selenium.click(pjq("input[name$=actionInput][value=toUpperCaseAction]"));
        selenium.waitForPageToLoad();
        selenium.typeKeys(input, "RichFaces 4ě");
        guardXhr(selenium).click(link);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4ě"));
        assertEquals(selenium.getText(output2), "RICHFACES 4Ě", "output2 when 'RichFaces 4ě' in input and toUpperCaseAction selected");
    }

    @Test
    public void testActionListener() {
        selenium.click(pjq("input[name$=actionListenerInput][value=doubleStringActionListener]"));
        selenium.waitForPageToLoad();
        selenium.typeKeys(input, "RichFaces 4");
        guardXhr(selenium).click(link);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4"));
        assertEquals(selenium.getText(output3), "RichFaces 4RichFaces 4",
                "output3 when 'RichFaces 4' in input and doubleStringActionListener selected");

        selenium.click(pjq("input[name$=actionListenerInput][value=first6CharsActionListener]"));
        selenium.waitForPageToLoad();
        selenium.typeKeys(input, "RichFaces 4ň");
        guardXhr(selenium).click(link);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4ň"));
        assertEquals(selenium.getText(output3), "RichFa", "output3 when 'RichFaces 4ň' in input and first6CharsActionListener selected");

        selenium.click(pjq("input[name$=actionListenerInput][value=toUpperCaseActionListener]"));
        selenium.waitForPageToLoad();
        selenium.typeKeys(input, "RichFaces 4ě");
        guardXhr(selenium).click(link);
        waitGui.until(textEquals.locator(output1).text("RichFaces 4ě"));
        assertEquals(selenium.getText(output3), "RICHFACES 4Ě", "output3 when 'RichFaces 4ě' in input and toUpperCaseActionListener selected");
    }

    @Test
    public void testBypassUpdates() {
        selenium.click(pjq("input[type=radio][name$=bypassUpdatesInput][value=true]"));
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);
        guardXhr(selenium).click(link);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "", "Output 1 should not change");
        assertEquals(selenium.getText(output2), "", "Output 2 should not change");
        assertEquals(selenium.getText(output3), "", "Output 3 should not change");
        assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
                PhaseId.RENDER_RESPONSE);

        String listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(3)"));
        assertEquals(listenerOutput, "* action listener invoked", "Action listener's output");
        listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(4)"));
        assertEquals(listenerOutput, "* action invoked", "Action's output");
    }

    @Test
    public void testCharset() {
        testHtmlAttribute(link, "charset", "utf-8");
    }

    @Test
    public void testCoords() {
        testHtmlAttribute(link, "coords", "circle: 150, 60, 60");
    }

    @Test
    public void testData() {
        selenium.type(pjq("input[type=text][id$=dataInput]"), "RichFaces 4");
        selenium.waitForPageToLoad();

        selenium.type(pjq("input[type=text][id$=oncompleteInput]"), "data = event.data");
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);

        selenium.type(input, "some input text");
        guardXhr(selenium).click(link);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        String data = selenium.getEval(new JavaScript("window.data"));
        assertEquals(data, "RichFaces 4", "Data sent with ajax request");
    }

    @Test
    public void testDisabled() {
        JQueryLocator newLink = pjq("span[id$=a4jCommandLink]");

        selenium.click(pjq("input[name$=disabledInput][value=true]"));
        selenium.waitForPageToLoad();

        assertFalse(selenium.isElementPresent(link), link.getAsString() + " should not be on page when the link is disabled");
        assertTrue(selenium.isElementPresent(newLink), newLink.getAsString() + " should be on page when the link is disabled");
    }

    @Test
    public void testExecute() {
        selenium.type(pjq("input[type=text][id$=executeInput]"), "input executeChecker");
        selenium.waitForPageToLoad();

        selenium.type(input, "RichFaces 4");
        guardXhr(selenium).click(link);
        waitGui.failWith("Page was not updated").waitForChangeAndReturn("", retrieveText.locator(output1));

        JQueryLocator logItems = jq("ul.phases-list li:eq({0})");
        for (int i = 0; i < 6; i++) {
            if ("* executeChecker".equals(selenium.getText(logItems.format(i)))) {
                return;
            }
        }

        fail("Attribute execute does not work");
    }

    @Test
    public void testHreflang() {
        testHtmlAttribute(link, "hreflang", "sk");
    }

    @Test
    public void testImmediate() {
        selenium.click(pjq("input[type=radio][name$=immediateInput][value=true]"));
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);
        guardXhr(selenium).click(link);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertEquals(selenium.getText(output1), "", "Output 1 should not change");
        assertEquals(selenium.getText(output2), "", "Output 2 should not change");
        assertEquals(selenium.getText(output3), "", "Output 3 should not change");
        assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);

        String listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(2)"));
        assertEquals(listenerOutput, "* action listener invoked", "Action listener's output");
        listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(3)"));
        assertEquals(listenerOutput, "* action invoked", "Action's output");
    }

    @Test
    public void testLimitRender() {
        selenium.click(pjq("input[type=radio][name$=limitRenderInput][value=true]"));
        selenium.waitForPageToLoad();

        String timeValue = selenium.getText(time);

        selenium.type(input, "RichFaces 4");
        guardXhr(selenium).click(link);
        waitGui.failWith("Page was not updated").waitForChange("", retrieveText.locator(output1));

        assertEquals(selenium.getText(time), timeValue, "Ajax-rendered a4j:outputPanel shouldn't change");
    }

    @Test
    public void testEvents() {
        selenium.type(pjq("input[type=text][id$=onbeginInput]"), "metamerEvents += \"begin \"");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[type=text][id$=onbeforedomupdateInput]"), "metamerEvents += \"beforedomupdate \"");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[type=text][id$=oncompleteInput]"), "metamerEvents += \"complete \"");
        selenium.waitForPageToLoad();

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));

        selenium.type(input, "RichFaces 4");
        guardXhr(selenium).click(link);
        waitGui.failWith("Page was not updated").waitForChange("", retrieveText.locator(output1));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events.length, 3, "3 events should be fired.");
        assertEquals(events[0], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[1], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[2], "complete", "Attribute oncomplete doesn't work");
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, link);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, link);
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, link);
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, link);
    }

    @Test
    public void testOneyup() {
        testFireEvent(Event.KEYUP, link);
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, link);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, link);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, link);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, link);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, link);
    }

    @Test
    public void testRel() {
        testHtmlAttribute(link, "rel", "metamer");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10555")
    public void testRender() {
        JQueryLocator renderInput = pjq("input[name$=renderInput]");

        selenium.type(renderInput, "output1");
        selenium.waitForPageToLoad();

        selenium.typeKeys(input, "aaa");
        guardXhr(selenium).click(link);

        waitGui.until(textEquals.locator(output1).text("aaa"));

        String output = selenium.getText(output1);
        assertEquals(output, "aaa", "output1 when 'aaa' in input and 'output1' set to be rerendered");

        output = selenium.getText(output2);
        assertEquals(output, "", "output2 when 'aaa' in input and 'output1' set to be rerendered");

        output = selenium.getText(output3);
        assertEquals(output, "", "output3 when 'aaa' in input and 'output1' set to be rerendered");

        selenium.type(renderInput, "output2 output3");
        selenium.waitForPageToLoad();

        selenium.typeKeys(input, "bbb");
        guardXhr(selenium).click(link);

        waitGui.until(textEquals.locator(output2).text("bbb"));

        output = selenium.getText(output1);
        assertEquals(output, "aaa", "output1 when 'bbb' in input and 'output2 output3' set to be rerendered");

        output = selenium.getText(output2);
        assertEquals(output, "bbb", "output2 when 'bbb' in input and 'output2 output3' set to be rerendered");

        output = selenium.getText(output3);
        assertEquals(output, "BBB", "output3 when 'bbb' in input and 'output2 output3' set to be rerendered");
    }

    @Test
    public void testRendered() {
        selenium.click(pjq("input[name$=renderedInput][value=false]"));
        selenium.waitForPageToLoad();
        assertFalse(selenium.isElementPresent(link), "Link should not be displayed");
    }

    @Test
    public void testRev() {
        testHtmlAttribute(link, "rev", "metamer");
    }

    @Test
    public void testShape() {
        testHtmlAttribute(link, "shape", "default");
    }

    @Test
    public void testStyle() {
        testStyle(link, "style");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9307")
    public void testStyleClass() {
        testStyleClass(link, "styleClass");
    }

    @Test
    public void testTitle() {
        testTitle(link);
    }

    @Test
    public void testType() {
        testHtmlAttribute(link, "type", "metamer");
    }

    @Test
    public void testValue() {
        selenium.type(pjq("input[id$=valueInput]"), "new label");
        selenium.waitForPageToLoad();

        assertEquals(selenium.getText(link), "new label", "Value of the button did not change");
    }
}
