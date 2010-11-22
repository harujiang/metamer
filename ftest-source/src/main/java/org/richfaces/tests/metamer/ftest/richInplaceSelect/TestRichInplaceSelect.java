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
package org.richfaces.tests.metamer.ftest.richInplaceSelect;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardNoRequest;
import static org.jboss.test.selenium.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import org.jboss.test.selenium.css.CssProperty;

import org.jboss.test.selenium.dom.Event;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.waiting.EventFiredCondition;
import org.richfaces.tests.metamer.ftest.AbstractMetamerTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * Test case for page faces/components/richInplaceSelect/simple.xhtml.
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision$
 */
public class TestRichInplaceSelect extends AbstractMetamerTest {

    private JQueryLocator select = pjq("span[id$=inplaceSelect]");
    private JQueryLocator label = pjq("span.rf-is-lbl");
    private JQueryLocator input = pjq("input[id$=inplaceSelectInput]");
    private JQueryLocator popup = pjq("span.rf-is-lst-cord");
    private JQueryLocator edit = pjq("span.rf-is-edit");
    private JQueryLocator options = jq("span.rf-is-opt:eq({0})"); // 00..49
    private JQueryLocator okButton = jq("input.rf-is-btn[id$=Okbtn]");
    private JQueryLocator cancelButton = jq("input.rf-is-btn[id$=Cancelbtn]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richInplaceSelect/simple.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(selenium.isElementPresent(select), "Inplace select is not on the page.");
        assertTrue(selenium.isElementPresent(label), "Default label should be present on the page.");
        assertEquals(selenium.getText(label), "Click here to edit", "Default label");
        assertTrue(selenium.isElementPresent(input), "Input should be present on the page.");
        assertFalse(selenium.isDisplayed(popup), "Popup should not be displayed on the page.");
    }

    @Test
    @IssueTracking("https://jira.jboss.org/browse/RF-9664")
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

        guardNoRequest(selenium).click(options.format(10));
        assertTrue(selenium.belongsClass(select, "rf-is-c-s"), "New class should be added to inplace select.");
        assertTrue(selenium.belongsClass(edit, "rf-is-none"), "Edit should contain class rf-is-none when popup is closed.");

        assertEquals(selenium.getText(label), "Hawaii", "Label should contain selected value.");
    }

    @Test
    public void testDefaultLabel() {
        selenium.type(pjq("input[type=text][id$=defaultLabelInput]"), "new label");
        selenium.waitForPageToLoad();
        assertEquals(selenium.getText(label), "new label", "Default label should change");

        selenium.type(pjq("input[type=text][id$=defaultLabelInput]"), "");
        selenium.waitForPageToLoad();
        assertEquals(selenium.getText(label), "", "Default label should change");

        assertTrue(selenium.isElementPresent(select), "Inplace select is not on the page.");
        assertTrue(selenium.isElementPresent(label), "Default label should be present on the page.");
        assertTrue(selenium.isElementPresent(input), "Input should be present on the page.");
        assertFalse(selenium.isDisplayed(popup), "Popup should not be displayed on the page.");
    }

    @Test
    @IssueTracking("https://jira.jboss.org/browse/RF-9844")
    public void testEditEvent() {
        selenium.type(pjq("input[type=text][id$=editEventInput]"), "mouseup");
        selenium.waitForPageToLoad();

        selenium.mouseDown(select);
        assertFalse(selenium.isDisplayed(popup), "Popup should not be displayed.");
        selenium.mouseUp(select);
        assertTrue(selenium.isDisplayed(popup), "Popup should be displayed.");

        selenium.type(pjq("input[type=text][id$=editEventInput]"), "nonexistingevent");
        selenium.waitForPageToLoad();

        selenium.click(select);
        assertTrue(selenium.isDisplayed(popup), "Popup should be displayed.");
    }

    @Test
    public void testItemClass() {
        final String value = "metamer-ftest-class";
        selenium.type(pjq("input[type=text][id$=itemClassInput]"), value);
        selenium.waitForPageToLoad();

        for (int i = 0; i < 50; i++) {
            assertTrue(selenium.belongsClass(options.format(i), value), "Select option "
                    + selenium.getText(options.format(i)) + " does not contain class " + value);
        }
    }

    @Test
    @IssueTracking("https://jira.jboss.org/browse/RF-9845")
    public void testListClass() {
        testStyleClass(popup, "listClass");
    }

    @Test
    @IssueTracking("https://jira.jboss.org/browse/RF-9647")
    public void testListHeight() {
        selenium.type(pjq("input[type=text][id$=listHeightInput]"), "300px");
        selenium.waitForPageToLoad();

        String height = selenium.getStyle(jq("span.rf-is-lst-scrl"), CssProperty.HEIGHT);
        assertEquals(height, "300px", "Height of list did not change");

        selenium.type(pjq("input[type=text][id$=listHeightInput]"), "");
        selenium.waitForPageToLoad();

        // it cannot handle null because of a bug in Mojarra and Myfaces and
        // generates style="height: ; " instead of default value
        height = selenium.getStyle(jq("span.rf-is-lst-scrl"), CssProperty.HEIGHT);
        assertEquals(height, "200px", "Height of list did not change");
    }

    @Test
    @IssueTracking("https://jira.jboss.org/browse/RF-9647")
    public void testListWidth() {
        selenium.type(pjq("input[type=text][id$=listWidthInput]"), "300px");
        selenium.waitForPageToLoad();

        String width = selenium.getStyle(jq("span.rf-is-lst-pos"), CssProperty.WIDTH);
        assertEquals(width, "300px", "Width of list did not change");

        selenium.type(pjq("input[type=text][id$=listWidthInput]"), "");
        selenium.waitForPageToLoad();

        // it cannot handle null because of a bug in Mojarra and Myfaces and
        // generates style="width: ; " instead of default value
        width = selenium.getStyle(jq("span.rf-is-lst-pos"), CssProperty.WIDTH);
        assertEquals(width, "200px", "Width of list did not change");
    }

    @Test
    @IssueTracking("https://jira.jboss.org/browse/RF-9849")
    public void testOnblur() {
        testFireEvent(Event.BLUR, select);
    }

    @Test
    @IssueTracking("https://jira.jboss.org/browse/RF-9571")
    public void testOnchange() {
        selenium.type(pjq("input[type=text][id$=onchangeInput]"), "metamerEvents += \"change \"");
        selenium.waitForPageToLoad();

        selenium.click(select);
        selenium.click(options.format(10));

        waitGui.failWith("Attribute onchange does not work correctly").until(
                new EventFiredCondition(Event.CHANGE));
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, select);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, select);
    }

    @Test
    @IssueTracking("https://jira.jboss.org/browse/RF-9849")
    public void testOnfocus() {
        testFireEvent(Event.FOCUS, select);
    }

    @Test
    public void testOninputblur() {
        testFireEvent(Event.BLUR, input, "inputblur");
    }

    @Test
    public void testOninputclick() {
        testFireEvent(Event.CLICK, input, "inputclick");
    }

    @Test
    public void testOninputdblclick() {
        testFireEvent(Event.DBLCLICK, input, "inputdblclick");
    }

    @Test
    public void testOninputfocus() {
        testFireEvent(Event.FOCUS, input, "inputfocus");
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
    public void testOninputselect() {
        testFireEvent(Event.SELECT, input, "inputselect");
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, select);
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, select);
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, select);
    }

    @Test
    public void testOnlistclick() {
        testFireEvent(Event.CLICK, popup, "listclick");
    }

    @Test
    public void testOnlistdblclick() {
        testFireEvent(Event.DBLCLICK, popup, "listdblclick");
    }

    @Test
    public void testOnlistkeydown() {
        testFireEvent(Event.KEYDOWN, popup, "listkeydown");
    }

    @Test
    public void testOnlistkeypress() {
        testFireEvent(Event.KEYPRESS, popup, "listkeypress");
    }

    @Test
    public void testOnlistkeyup() {
        testFireEvent(Event.KEYUP, popup, "listkeyup");
    }

    @Test
    public void testOnlistmousedown() {
        testFireEvent(Event.MOUSEDOWN, popup, "listmousedown");
    }

    @Test
    public void testOnlistmousemove() {
        testFireEvent(Event.MOUSEMOVE, popup, "listmousemove");
    }

    @Test
    public void testOnlistmouseout() {
        testFireEvent(Event.MOUSEOUT, popup, "listmouseout");
    }

    @Test
    public void testOnlistmouseover() {
        testFireEvent(Event.MOUSEOVER, popup, "listmouseover");
    }

    @Test
    public void testOnlistmouseup() {
        testFireEvent(Event.MOUSEUP, popup, "listmouseup");
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, select);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, select);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, select);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, select);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, select);
    }

    @Test
    @IssueTracking("https://jira.jboss.org/browse/RF-9849")
    public void testOnselect() {
        testFireEvent(Event.SELECT, input);
    }

    @Test
    public void testOpenOnEdit() {
        selenium.click(pjq("input[type=radio][name$=openOnEditInput][value=false]"));
        selenium.waitForPageToLoad();

        selenium.click(select);
        assertFalse(selenium.belongsClass(edit, "rf-is-none"), "Edit should not contain class rf-is-none when popup is open.");
        assertFalse(selenium.isDisplayed(popup), "Popup should not be displayed.");

        selenium.click(input);
        assertTrue(selenium.isDisplayed(popup), "Popup should be displayed.");
    }

    @Test
    public void testRendered() {
        selenium.click(pjq("input[type=radio][name$=renderedInput][value=false]"));
        selenium.waitForPageToLoad();

        assertFalse(selenium.isElementPresent(select), "Panel should not be rendered when rendered=false.");
    }

    @Test
    public void testSaveOnBlurSelectTrueTrue() {
        selenium.click(pjq("input[type=radio][name$=saveOnBlurInput][value=true]"));
        selenium.waitForPageToLoad();

        selenium.click(select);
        assertTrue(selenium.isDisplayed(popup), "Popup should be displayed.");

        selenium.click(options.format(10));
        assertFalse(selenium.isDisplayed(popup), "Popup should not be displayed.");
        assertEquals(selenium.getText(label), "Hawaii", "Label should contain selected value.");
    }

    @Test
    public void testSaveOnBlurSelectTrueFalse() {
        selenium.click(pjq("input[type=radio][name$=saveOnBlurInput][value=true]"));
        selenium.waitForPageToLoad();
        selenium.click(pjq("input[type=radio][name$=saveOnSelectInput][value=false]"));
        selenium.waitForPageToLoad();

        selenium.click(select);
        assertTrue(selenium.isDisplayed(popup), "Popup should be displayed.");

        selenium.click(options.format(10));
        assertEquals(selenium.getText(label), "Click here to edit", "Label should contain default value.");
        assertFalse(selenium.isDisplayed(popup), "Popup should not be displayed.");

        selenium.fireEvent(input, Event.BLUR);
        assertFalse(selenium.isDisplayed(popup), "Popup should not be displayed.");
        assertEquals(selenium.getValue(input), "Hawaii", "Input should contain selected value.");
        waitGui.failWith("Label should contain selected value.").until(textEquals.locator(label).text("Hawaii"));
    }

    @Test
    public void testSaveOnBlurSelectFalseTrue() {
        selenium.click(pjq("input[type=radio][name$=saveOnBlurInput][value=false]"));
        selenium.waitForPageToLoad();

        selenium.click(select);
        assertTrue(selenium.isDisplayed(popup), "Popup should be displayed.");

        selenium.click(options.format(10));
        assertFalse(selenium.isDisplayed(popup), "Popup should not be displayed.");
        assertEquals(selenium.getText(label), "Hawaii", "Label should contain selected value.");
    }

    @Test
    public void testSaveOnBlurSelectFalseFalse() {
        selenium.click(pjq("input[type=radio][name$=saveOnBlurInput][value=false]"));
        selenium.waitForPageToLoad();
        selenium.click(pjq("input[type=radio][name$=saveOnSelectInput][value=false]"));
        selenium.waitForPageToLoad();

        selenium.click(select);
        assertTrue(selenium.isDisplayed(popup), "Popup should be displayed.");

        selenium.click(options.format(10));
        assertEquals(selenium.getText(label), "Click here to edit", "Label should contain default value.");
        assertFalse(selenium.isDisplayed(popup), "Popup should not be displayed.");

        selenium.fireEvent(input, Event.BLUR);
        assertFalse(selenium.isDisplayed(popup), "Popup should not be displayed.");
        assertEquals(selenium.getText(label), "Click here to edit", "Label should contain default value.");
    }

    @Test
    public void testShowControls() {
        selenium.click(pjq("input[type=radio][name$=showControlsInput][value=true]"));
        selenium.waitForPageToLoad();

        selenium.click(select);
        assertTrue(selenium.isVisible(okButton), "OK button should be visible.");
        assertTrue(selenium.isVisible(cancelButton), "Cancel button should be visible.");
        assertTrue(selenium.isDisplayed(popup), "Popup should be displayed.");
    }

    @Test
    public void testClickOkButton() {
        selenium.click(pjq("input[type=radio][name$=showControlsInput][value=true]"));
        selenium.waitForPageToLoad();
        selenium.click(pjq("input[type=radio][name$=saveOnSelectInput][value=false]"));
        selenium.waitForPageToLoad();

        selenium.click(select);
        selenium.click(options.format(10));
        assertEquals(selenium.getText(label), "Click here to edit", "Label should contain default value.");
        assertFalse(selenium.isDisplayed(popup), "Popup should not be displayed.");

        selenium.mouseDown(okButton);

        if (selenium.isElementPresent(popup)) {
            assertFalse(selenium.isDisplayed(popup), "Popup should not be displayed.");
        }
        waitGui.failWith("Label should contain selected value.").until(textEquals.locator(label).text("Hawaii"));
    }

    @Test
    public void testClickCancelButton() {
        selenium.click(pjq("input[type=radio][name$=showControlsInput][value=true]"));
        selenium.waitForPageToLoad();
        selenium.click(pjq("input[type=radio][name$=saveOnSelectInput][value=false]"));
        selenium.waitForPageToLoad();

        selenium.click(select);
        selenium.click(options.format(10));
        assertEquals(selenium.getText(label), "Click here to edit", "Label should contain default value.");
        assertFalse(selenium.isDisplayed(popup), "Popup should not be displayed.");

        selenium.click(cancelButton);
        if (selenium.isElementPresent(popup)) {
            assertFalse(selenium.isDisplayed(popup), "Popup should not be displayed.");
        }
        assertEquals(selenium.getText(label), "Click here to edit", "Label should contain default value.");
    }
}
