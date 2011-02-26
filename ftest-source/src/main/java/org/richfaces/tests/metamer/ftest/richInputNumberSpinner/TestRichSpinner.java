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
package org.richfaces.tests.metamer.ftest.richInputNumberSpinner;

import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardXhr;
import static org.jboss.test.selenium.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.test.selenium.dom.Event;
import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.locator.Attribute;
import org.jboss.test.selenium.locator.AttributeLocator;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richInputNumberSpinner/simple.xhtml
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision$
 */
public class TestRichSpinner extends AbstractSpinnerTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInputNumberSpinner/simple.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(selenium.isDisplayed(spinner), "Spinner is not present on the page.");
        assertTrue(selenium.isDisplayed(input), "Spinner's input is not present on the page.");
        assertTrue(selenium.isDisplayed(up), "Spinner's up button is not present on the page.");
        assertTrue(selenium.isDisplayed(down), "Spinner's down button is not present on the page.");

    }

    @Test
    @Use(field = "number", value = "correctNumbers")
    @Override
    public void testTypeIntoInputCorrect() {
        super.testTypeIntoInputCorrect();
    }

    @Test
    @Use(field = "number", value = "smallNumbers")
    @Override
    public void testTypeIntoInputSmall() {
        super.testTypeIntoInputSmall();
    }

    @Test
    @Use(field = "number", value = "bigNumbers")
    @Override
    public void testTypeIntoInputBig() {
        super.testTypeIntoInputBig();
    }

    @Test
    @Use(field = "number", value = "decimalNumbers")
    @Override
    public void testTypeIntoInputDecimal() {
        super.testTypeIntoInputDecimal();
    }

    @Test
    @Override
    public void testTypeIntoInputNotNumber() {
        super.testTypeIntoInputNotNumber();
    }

    @Test
    @Override
    public void testClickUp() {
        super.testClickUp();
    }

    @Test
    @Override
    public void testClickDown() {
        super.testClickDown();
    }

    @Test
    public void testAccesskey() {
        testHtmlAttribute(input, "accesskey", "x");
    }

    @Test
    public void testCycled() {
        JQueryLocator selectOption = pjq("input[type=radio][name$=cycledInput][value=true]");
        selenium.click(selectOption);
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, "10");
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        // test that value change to min value (10 -> -10)
        clickArrow(up, 1);
        assertEquals(selenium.getText(output), "-10", "Output was not updated.");

        // test that value change to max value (-10 -> 10)
        clickArrow(down, 1);
        assertEquals(selenium.getText(output), "10", "Output was not updated.");
    }

    @Test
    public void testDisabled() {
        JQueryLocator selectOption = pjq("input[type=radio][name$=disabledInput][value=true]");
        selenium.click(selectOption);
        selenium.waitForPageToLoad();

        AttributeLocator disabledAttribute = input.getAttribute(new Attribute("disabled"));
        assertEquals(selenium.getAttribute(disabledAttribute), "disabled", "Input should be disabled.");

        assertFalse(selenium.isElementPresent(up), "Arrow up should be disabled.");
        assertFalse(selenium.isElementPresent(down), "Arrow down should be disabled.");

        JQueryLocator upDisabled = pjq("span[id$=spinner] span.rf-insp-inc-dis");
        JQueryLocator downDisabled = pjq("span[id$=spinner] span.rf-insp-dec-dis");

        assertTrue(selenium.isElementPresent(upDisabled), "An disabled up arrow should be displayed.");
        assertTrue(selenium.isElementPresent(downDisabled), "An disabled downarrow should be displayed.");
    }

    @Test
    public void testEnableManualInput() {
        JQueryLocator selectOption = pjq("input[type=radio][name$=enableManualInputInput][value=false]");
        selenium.click(selectOption);
        selenium.waitForPageToLoad();

        AttributeLocator readonlyAttribute = input.getAttribute(new Attribute("readonly"));
        assertEquals(selenium.getAttribute(readonlyAttribute), "readonly", "Input should be read-only.");

        assertTrue(selenium.isElementPresent(up), "Arrow up should be displayed.");
        assertTrue(selenium.isElementPresent(down), "Arrow down should be displayed.");
    }

    @Test
    public void testImmediate() {
        JQueryLocator immediateInput = pjq("input[type=radio][name$=immediateInput][value=true]");
        selenium.click(immediateInput);
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, "4");
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        assertEquals(selenium.getText(output), "4", "Output was not updated.");

        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
                PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);
        phaseInfo.assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed: 2 -> 4");
    }

    @Test
    public void testInputClass() {
        testStyleClass(input, "inputClass");
    }

    @Test
    public void testInputSize() {
        JQueryLocator selectOption = pjq("input[type=text][id$=inputSizeInput]");

        selenium.type(selectOption, "3");
        selenium.waitForPageToLoad();
        AttributeLocator sizeAttribute = input.getAttribute(new Attribute("size"));
        assertEquals(selenium.getAttribute(sizeAttribute), "3", "Input's size attribute.");

        selenium.type(selectOption, "40");
        selenium.waitForPageToLoad();
        assertEquals(selenium.getAttribute(sizeAttribute), "40", "Input's size attribute.");
    }

    @Test
    public void testMaxValueType() {
        JQueryLocator selectOption = pjq("input[type=text][id$=maxValueInput]");
        selenium.type(selectOption, "13");
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, "11");
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        assertEquals(selenium.getText(output), "11", "Output was not updated.");

        reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, "13");
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        assertEquals(selenium.getText(output), "13", "Output was not updated.");

    }

    @Test
    public void testMaxValueClick() {
        JQueryLocator selectOption = pjq("input[type=text][id$=maxValueInput]");
        selenium.type(selectOption, "13");
        selenium.waitForPageToLoad();

        clickArrow(up, 9);
        assertEquals(selenium.getText(output), "11", "Output was not updated.");

        clickArrow(up, 2);
        assertEquals(selenium.getText(output), "13", "Output was not updated.");
    }

    @Test
    public void testMinValueType() {
        JQueryLocator selectOption = pjq("input[type=text][id$=minValueInput]");
        selenium.type(selectOption, "-13");
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, "-11");
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        assertEquals(selenium.getText(output), "-11", "Output was not updated.");

        reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, "-13");
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));
        assertEquals(selenium.getText(output), "-13", "Output was not updated.");

    }

    @Test
    public void testMinValueClick() {
        JQueryLocator selectOption = pjq("input[type=text][id$=minValueInput]");
        selenium.type(selectOption, "-13");
        selenium.waitForPageToLoad();

        clickArrow(down, 13);
        assertEquals(selenium.getText(output), "-11", "Output was not updated.");

        clickArrow(down, 2);
        assertEquals(selenium.getText(output), "-13", "Output was not updated.");
    }

    @Test
    public void testOnblur() {
        testFireEvent(Event.BLUR, input);
    }

    @Test
    public void testOnchangeType() {
        selenium.type(pjq("input[id$=onchangeInput]"), "metamerEvents += \"change \"");
        selenium.waitForPageToLoad(TIMEOUT);

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));

        String reqTime = selenium.getText(time);
        guardXhr(selenium).type(input, "4");
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events[0], "change", "Attribute onchange doesn't work");
        assertEquals(events.length, 1, "Only one event should be fired");
    }

    @Test
    public void testOnchangeClick() {
        selenium.type(pjq("input[id$=onchangeInput]"), "metamerEvents += \"change \"");
        selenium.waitForPageToLoad(TIMEOUT);

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));

        clickArrow(up, 1);

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events[0], "change", "Attribute onchange doesn't work");
        assertEquals(events.length, 1, "Only one event should be fired");

        clickArrow(down, 1);

        events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events[0], "change", "Attribute onchange doesn't work.");
        assertEquals(events[1], "change", "Attribute onchange doesn't work.");
        assertEquals(events.length, 2, "Two events should be fired after two clicks on arrows.");
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, spinner);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, spinner);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10581")
    public void testOndownclick() {
        testFireEvent(Event.CLICK, down, "downclick");
    }

    @Test
    public void testOnfocus() {
        testFireEvent(Event.FOCUS, input);
    }

    @Test
    public void testOninputclick() {
        testFireEvent(Event.CLICK, input, "inputclick");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9568")
    public void testOninputdblclick() {
        testFireEvent(Event.DBLCLICK, input, "inputdblclick");
    }

    @Test
    public void testOninputkeydown() {
        testFireEvent(Event.KEYDOWN, input, "inputkeydown");
    }

    @Test
    public void testOninputkeypress() {
        testFireEvent(Event.KEYPRESS, input, "inputkeypress");
    }

    @Test
    public void testOninputkeyup() {
        testFireEvent(Event.KEYUP, input, "inputkeyup");
    }

    @Test
    public void testOninputmousedown() {
        testFireEvent(Event.MOUSEDOWN, input, "inputmousedown");
    }

    @Test
    public void testOninputmousemove() {
        testFireEvent(Event.MOUSEMOVE, input, "inputmousemove");
    }

    @Test
    public void testOninputmouseout() {
        testFireEvent(Event.MOUSEOUT, input, "inputmouseout");
    }

    @Test
    public void testOninputmouseover() {
        testFireEvent(Event.MOUSEOVER, input, "inputmouseover");
    }

    @Test
    public void testOninputmouseup() {
        testFireEvent(Event.MOUSEUP, input, "inputmouseup");
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, spinner);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, spinner);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, spinner);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, spinner);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, spinner);
    }

    @Test
    public void testOnselect() {
        testFireEvent(Event.SELECT, input);
    }

    @Test
    public void testOnupclick() {
        testFireEvent(Event.CLICK, up, "upclick");
    }

    @Test
    public void testRendered() {
        selenium.click(pjq("input[type=radio][name$=renderedInput][value=false]"));
        selenium.waitForPageToLoad();

        assertFalse(selenium.isElementPresent(spinner), "Spinner should not be rendered when rendered=false.");
    }

    @Test
    public void testStep() {
        selenium.type(pjq("input[id$=stepInput]"), "7");
        selenium.waitForPageToLoad();

        clickArrow(up, 1);
        assertEquals(selenium.getText(output), "9", "Wrong output");

        clickArrow(up, 1);
        assertEquals(selenium.getText(output), "10", "Wrong output");

        clickArrow(down, 1);
        assertEquals(selenium.getText(output), "3", "Wrong output");

        clickArrow(down, 1);
        assertEquals(selenium.getText(output), "-4", "Wrong output");

        clickArrow(down, 1);
        assertEquals(selenium.getText(output), "-10", "Wrong output");

        clickArrow(up, 1);
        assertEquals(selenium.getText(output), "-3", "Wrong output");
    }

    @Test
    public void testStyle() {
        testStyle(spinner, "style");
    }

    @Test
    public void testStyleClass() {
        testStyleClass(spinner, "styleClass");
    }

    @Test
    public void testTabindex() {
        testHtmlAttribute(input, "tabindex", "57");
    }

    @Test
    @Use(field = "number", value = "correctNumbers")
    public void testValueCorrect() {
        selenium.type(pjq("input[id$=valueInput]"), number);
        selenium.waitForPageToLoad();

        assertEquals(selenium.getText(output), number, "Output was not updated.");
    }

    @Test
    @Use(field = "number", value = "smallNumbers")
    public void testValueSmall() {
        selenium.type(pjq("input[id$=valueInput]"), number);
        selenium.waitForPageToLoad();

        assertEquals(selenium.getText(output), number, "Output was not updated.");
        assertEquals(getInputValue(), "-10", "Input was not updated.");
    }

    @Test
    @Use(field = "number", value = "bigNumbers")
    public void testValueBig() {
        selenium.type(pjq("input[id$=valueInput]"), number);
        selenium.waitForPageToLoad();

        assertEquals(selenium.getText(output), number, "Output was not updated.");
        assertEquals(getInputValue(), "10", "Input was not updated.");
    }
}
