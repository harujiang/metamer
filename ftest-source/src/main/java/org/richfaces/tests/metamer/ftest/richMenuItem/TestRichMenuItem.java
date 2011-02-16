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
package org.richfaces.tests.metamer.ftest.richMenuItem;

import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardHttp;
import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardNoRequest;
import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardXhr;
import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.locator.option.OptionLocatorFactory.optionLabel;
import static org.jboss.test.selenium.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.test.selenium.dom.Event;
import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.locator.Attribute;
import org.jboss.test.selenium.locator.AttributeLocator;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractMetamerTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richMenuItem/simple.xhtml
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision$
 */
public class TestRichMenuItem extends AbstractMetamerTest {

    private JQueryLocator fileMenu = pjq("div[id$=menu1]");
    private JQueryLocator fileMenuLabel = pjq("div[id$=menu1_label]");
    private JQueryLocator menuItem1 = pjq("div[id$=menuItem1]");
    private JQueryLocator image = menuItem1.getDescendant(jq("img"));
    private JQueryLocator label = menuItem1.getDescendant(jq("span.rf-ddm-itm-lbl"));
    private JQueryLocator menuItem2 = pjq("div[id$=menuItem2]");
    private JQueryLocator menuGroup3 = pjq("div[id$=menuGroup3]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richMenuItem/simple.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(selenium.isElementPresent(fileMenu), "Drop down menu \"File\" should be present on the page.");
        assertTrue(selenium.isVisible(fileMenu), "Drop down menu \"File\" should be visible on the page.");

        assertTrue(selenium.isElementPresent(menuItem1), "Menu item \"New\" should be present on the page.");
        assertFalse(selenium.isVisible(menuItem1), "Menu item \"New\" should not be visible on the page.");

        guardNoRequest(selenium).mouseOver(fileMenuLabel);

        assertTrue(selenium.isElementPresent(menuItem1), "Menu item \"New\" should be present on the page.");
        assertTrue(selenium.isVisible(menuItem1), "Menu item \"New\" should be visible on the page.");

        assertTrue(selenium.isElementPresent(menuItem2), "Menu item \"Open\" should be present on the page.");
        assertTrue(selenium.isVisible(menuItem2), "Menu item \"Open\" should be visible on the page.");

        assertTrue(selenium.isElementPresent(menuGroup3), "Menu group \"Open Recent...\" should be present on the page.");
        assertTrue(selenium.isVisible(menuGroup3), "Menu group \"Open Recent...\" should be visible on the page.");

        assertTrue(selenium.isElementPresent(image), "Icon of menu item 1 should be present on the page.");
        assertTrue(selenium.isVisible(image), "Icon of menu item 1 should be visible on the page.");

        assertTrue(selenium.isElementPresent(label), "Label of menu item 1 should be present on the page.");
        assertTrue(selenium.isVisible(label), "Label of menu item 1 should be visible on the page.");

        assertEquals(selenium.getText(label), "New", "Label of first menu item.");
        AttributeLocator attr = image.getAttribute(Attribute.SRC);
        assertTrue(selenium.getAttribute(attr).contains("create_doc.gif"), "Image's src attribute should contain \"create_doc.gif\".");
    }

    @Test
    public void testAction() {
        String reqTime = selenium.getText(time);
        selenium.click(fileMenu);
        guardXhr(selenium).click(menuItem1);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        String listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(6)"));
        assertEquals(listenerOutput, "* action invoked", "Action's output");
    }

    @Test
    public void testActionListener() {
        String reqTime = selenium.getText(time);
        selenium.mouseOver(fileMenuLabel);
        guardXhr(selenium).click(menuItem1);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        String listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(5)"));
        assertEquals(listenerOutput, "* action listener invoked", "Action listener's output");
    }

    @Test
    public void testBypassUpdates() {
        selenium.click(pjq("input[type=radio][name$=bypassUpdatesInput][value=true]"));
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);
        selenium.mouseOver(fileMenuLabel);
        guardXhr(selenium).click(menuItem1);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
                PhaseId.RENDER_RESPONSE);

        String listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(3)"));
        assertEquals(listenerOutput, "* action listener invoked", "Action listener's output");
        listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(4)"));
        assertEquals(listenerOutput, "* action invoked", "Action's output");
    }

    @Test
    public void testData() {
        selenium.type(pjq("input[type=text][id$=dataInput]"), "RichFaces 4");
        selenium.waitForPageToLoad();

        selenium.type(pjq("input[type=text][id$=oncompleteInput]"), "data = event.data");
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);
        selenium.mouseOver(fileMenuLabel);
        guardXhr(selenium).click(menuItem1);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        String data = selenium.getEval(new JavaScript("window.data"));
        assertEquals(data, "RichFaces 4", "Data sent with ajax request");
    }

    @Test
    public void testDir() {
        testDir(menuItem1);
    }

    @Test
    public void testDisabled() {
        selenium.click(pjq("input[type=radio][name$=disabledInput][value=true]"));
        selenium.waitForPageToLoad();

        assertTrue(selenium.belongsClass(menuItem1, "rf-ddm-itm-dis"), "Menu item should have class \"rf-ddm-itm-dis\".");
        assertTrue(selenium.isElementPresent(menuItem1.getDescendant(jq("div.rf-ddm-emptyIcon"))), "Empty icon should be present.");
        assertFalse(selenium.isElementPresent(image), "Icon should not be present.");
    }

    @Test
    public void testExecute() {
        selenium.type(pjq("input[type=text][id$=executeInput]"), "@this executeChecker");
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);
        selenium.mouseOver(fileMenuLabel);
        guardXhr(selenium).click(menuItem1);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        JQueryLocator logItems = jq("ul.phases-list li:eq({0})");
        for (int i = 0; i < 6; i++) {
            if ("* executeChecker".equals(selenium.getText(logItems.format(i)))) {
                return;
            }
        }

        fail("Attribute execute does not work");
    }

    @Test
    public void testIcon() {
        AttributeLocator attr = image.getAttribute(Attribute.SRC);

        selenium.select(pjq("select[id$=iconInput]"), optionLabel("star"));
        selenium.waitForPageToLoad();
        assertTrue(selenium.getAttribute(attr).contains("star.png"), "Image's src attribute should contain \"star.png\".");

        selenium.select(pjq("select[id$=iconInput]"), optionLabel("nonexisting"));
        selenium.waitForPageToLoad();
        assertTrue(selenium.getAttribute(attr).contains("nonexisting"), "Image's src attribute should contain \"nonexisting\".");

        selenium.select(pjq("select[id$=iconInput]"), optionLabel("null"));
        selenium.waitForPageToLoad();
        assertFalse(selenium.isElementPresent(image), "Icon should not be present.");
        assertTrue(selenium.isElementPresent(menuItem1.getDescendant(jq("div.rf-ddm-emptyIcon"))), "Empty icon should be present.");
    }

    @Test
    public void testIconDisabled() {
        selenium.click(pjq("input[type=radio][name$=disabledInput][value=true]"));
        selenium.waitForPageToLoad();

        AttributeLocator attr = image.getAttribute(Attribute.SRC);

        assertFalse(selenium.isElementPresent(image), "Icon should not be present.");
        assertTrue(selenium.isElementPresent(menuItem1.getDescendant(jq("div.rf-ddm-emptyIcon"))), "Empty icon should be present.");

        selenium.select(pjq("select[id$=iconDisabledInput]"), optionLabel("star"));
        selenium.waitForPageToLoad();
        assertTrue(selenium.getAttribute(attr).contains("star.png"), "Image's src attribute should contain \"star.png\".");

        selenium.select(pjq("select[id$=iconDisabledInput]"), optionLabel("nonexisting"));
        selenium.waitForPageToLoad();
        assertTrue(selenium.getAttribute(attr).contains("nonexisting"), "Image's src attribute should contain \"nonexisting\".");
    }

    @Test
    public void testImmediate() {
        selenium.click(pjq("input[type=radio][name$=immediateInput][value=true]"));
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);
        selenium.mouseOver(fileMenuLabel);
        guardXhr(selenium).click(menuItem1);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);

        String listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(2)"));
        assertEquals(listenerOutput, "* action listener invoked", "Action listener's output");
        listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(3)"));
        assertEquals(listenerOutput, "* action invoked", "Action's output");
    }

    @Test
    public void testLabel() {
        selenium.type(pjq("input[type=text][id$=labelInput]"), "new label");
        selenium.waitForPageToLoad();

        assertEquals(selenium.getText(label), "new label", "New label of first menu item.");
    }

    @Test
    public void testLang() {
        testLang(menuItem1);
    }

    @Test
    public void testLimitRender() {
        selenium.click(pjq("input[type=radio][name$=limitRenderInput][value=true]"));
        selenium.waitForPageToLoad();

        String reqTime = selenium.getText(time);
        selenium.mouseOver(fileMenu);
        guardXhr(selenium).click(menuItem1);
        String newTime = selenium.getText(time);
        assertNotSame(newTime, reqTime, "Panel with ajaxRendered=true should not be rerendered.");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10200")
    public void testMode() {
        String reqTime = selenium.getText(time);
        selenium.mouseOver(fileMenu);
        guardXhr(selenium).click(menuItem1);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
                PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);

        String listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(5)"));
        assertEquals(listenerOutput, "* action listener invoked", "Action listener's output");
        listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(6)"));
        assertEquals(listenerOutput, "* action invoked", "Action's output");

        selenium.click(pjq("input[name$=modeInput][value=server]"));
        selenium.waitForPageToLoad();

        selenium.mouseOver(fileMenu);
        guardHttp(selenium).click(menuItem1);

        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
                PhaseId.UPDATE_MODEL_VALUES, PhaseId.INVOKE_APPLICATION, PhaseId.RENDER_RESPONSE);

        listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(5)"));
        assertEquals(listenerOutput, "* action listener invoked", "Action listener's output");
        listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(6)"));
        assertEquals(listenerOutput, "* action invoked", "Action's output");
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

        String reqTime = selenium.getText(time);
        selenium.mouseOver(fileMenu);
        guardXhr(selenium).click(menuItem1);
        waitGui.failWith("Page was not updated").waitForChange(reqTime, retrieveText.locator(time));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events[0], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[1], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[2], "complete", "Attribute oncomplete doesn't work");
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, menuItem1);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, menuItem1);
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, menuItem1);
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, menuItem1);
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, menuItem1);
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, menuItem1);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, menuItem1);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, menuItem1);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, menuItem1);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, menuItem1);
    }

    @Test
    public void testRendered() {
        selenium.click(pjq("input[type=radio][name$=renderedInput][value=false]"));
        selenium.waitForPageToLoad();

        assertFalse(selenium.isElementPresent(menuItem1), "Menu item should not be rendered when rendered=false.");
        assertTrue(selenium.isDisplayed(menuItem1), "Menu item should be displayed when item 1 is not rendered.");
    }

    @Test
    public void testStyle() {
        testStyle(menuItem1, "style");
    }

    @Test
    public void testStyleClass() {
        testStyleClass(menuItem1, "styleClass");
    }

    @Test
    public void testTitle() {
        testTitle(menuItem1);
    }
}
