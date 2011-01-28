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
package org.richfaces.tests.metamer.ftest.richTabPanel;

import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardHttp;
import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardNoRequest;
import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardXhr;
import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.fail;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.test.selenium.dom.Event;
import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.waiting.EventFiredCondition;
import org.richfaces.tests.metamer.ftest.AbstractMetamerTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richTabPanel/simple.xhtml
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision$
 */
public class TestRichTabPanel extends AbstractMetamerTest {

    private JQueryLocator panel = pjq("div[id$=tabPanel]");
    private JQueryLocator[] items = {pjq("div[id$=tab1]"), pjq("div[id$=tab2]"), pjq("div[id$=tab3]"), pjq("div[id$=tab4]"),
        pjq("div[id$=tab5]")};
    private JQueryLocator[] itemContents = {pjq("div[id$=tab1] > div.rf-tb-cnt"), pjq("div[id$=tab2] > div.rf-tb-cnt"),
        pjq("div[id$=tab3] > div.rf-tb-cnt"), pjq("div[id$=tab4] > div.rf-tb-cnt"), pjq("div[id$=tab5] > div.rf-tb-cnt")};
    private JQueryLocator[] activeHeaders = {pjq("td[id$=tab1:header:active]"), pjq("td[id$=tab2:header:active]"),
        pjq("td[id$=tab3:header:active]"), pjq("td[id$=tab4:header:active]"), pjq("td[id$=tab5:header:active]")};
    private JQueryLocator[] inactiveHeaders = {pjq("td[id$=tab1:header:inactive]"), pjq("td[id$=tab2:header:inactive]"),
        pjq("td[id$=tab3:header:inactive]"), pjq("td[id$=tab4:header:inactive]"), pjq("td[id$=tab5:header:inactive]")};
    private JQueryLocator[] disabledHeaders = {pjq("td[id$=tab1:header:disabled]"), pjq("td[id$=tab2:header:disabled]"),
        pjq("td[id$=tab3:header:disabled]"), pjq("td[id$=tab4:header:disabled]"), pjq("td[id$=tab5:header:disabled]")};

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richTabPanel/simple.xhtml");
    }

    @Test
    public void testInit() {
        boolean displayed = selenium.isDisplayed(panel);
        assertTrue(displayed, "Tab panel is not present on the page.");

        displayed = selenium.isDisplayed(activeHeaders[0]);
        assertTrue(displayed, "Header of tab1 should be active.");
        for (int i = 1; i < 5; i++) {
            displayed = selenium.isDisplayed(activeHeaders[i]);
            assertFalse(displayed, "Header of tab " + (i + 1) + " should not be active.");
        }

        displayed = selenium.isDisplayed(inactiveHeaders[0]);
        assertFalse(displayed, "Header of tab1 should not be inactive.");
        displayed = selenium.isDisplayed(inactiveHeaders[1]);
        assertTrue(displayed, "Header of tab2 should be inactive.");

        displayed = selenium.isDisplayed(disabledHeaders[3]);
        assertTrue(displayed, "Header of tab4 should be disabled.");
        for (int i = 0; i < 3; i++) {
            displayed = selenium.isDisplayed(disabledHeaders[i]);
            assertFalse(displayed, "Header of tab " + (i + 1) + " should not be disabled.");
        }

        displayed = selenium.isDisplayed(itemContents[0]);
        assertTrue(displayed, "Content of item1 should be visible.");

        for (int i = 1; i < 5; i++) {
            displayed = selenium.isDisplayed(items[i]);
            assertFalse(displayed, "Tab" + (i + 1) + "'s content should not be visible.");
        }
    }

    @Test
    public void testActiveItem() {
        selenium.type(pjq("input[type=text][id$=activeItemInput]"), "tab5");
        selenium.waitForPageToLoad();

        boolean displayed = selenium.isDisplayed(panel);
        assertTrue(displayed, "Tab panel is not present on the page.");

        for (int i = 0; i < 4; i++) {
            displayed = selenium.isDisplayed(activeHeaders[i]);
            assertFalse(displayed, "Tab" + (i + 1) + "'s header should not be active.");
        }

        displayed = selenium.isDisplayed(itemContents[4]);
        assertTrue(displayed, "Content of tab5 should be visible.");

        for (int i = 0; i < 4; i++) {
            displayed = selenium.isDisplayed(items[i]);
            assertFalse(displayed, "Tab" + (i + 1) + "'s content should not be visible.");
        }

        selenium.type(pjq("input[type=text][id$=activeItemInput]"), "tab4");
        selenium.waitForPageToLoad();

        for (int i = 0; i < 5; i++) {
            displayed = selenium.isDisplayed(activeHeaders[i]);
            assertFalse(displayed, "Tab" + (i + 1) + "'s header should not be active.");
        }

        for (int i = 0; i < 5; i++) {
            displayed = selenium.isDisplayed(items[i]);
            assertFalse(displayed, "Tab" + (i + 1) + "'s content should not be visible.");
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10054")
    public void testBypassUpdates() {
        selenium.click(pjq("input[type=radio][name$=bypassUpdatesInput][value=true]"));
        selenium.waitForPageToLoad();

        selenium.click(inactiveHeaders[2]);
        waitGui.failWith("Tab 3 is not displayed.").until(isDisplayed.locator(itemContents[2]));

        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.PROCESS_VALIDATIONS,
                PhaseId.RENDER_RESPONSE);
    }

    @Test
    public void testCycledSwitching() {
        String panelId = selenium.getEval(new JavaScript("window.testedComponentId"));
        String result = null;

        // RichFaces.$('form:tabPanel').nextItem('tab4') will be null
        result = selenium.getEval(new JavaScript("window.RichFaces.$('" + panelId + "').nextItem('tab4')"));
        assertEquals(result, "null", "Result of function nextItem('tab4')");

        // RichFaces.$('form:tabPanel').prevItem('tab1') will be null
        result = selenium.getEval(new JavaScript("window.RichFaces.$('" + panelId + "').prevItem('tab1')"));
        assertEquals(result, "null", "Result of function prevItem('tab1')");

        selenium.click(pjq("input[type=radio][name$=cycledSwitchingInput][value=true]"));
        selenium.waitForPageToLoad();

        // RichFaces.$('form:tabPanel').nextItem('tab5') will be item1
        result = selenium.getEval(new JavaScript("window.RichFaces.$('" + panelId + "').nextItem('tab5')"));
        assertEquals(result, "tab1", "Result of function nextItem('tab5')");

        // RichFaces.$('form:tabPanel').prevItem('tab1') will be item5
        result = selenium.getEval(new JavaScript("window.RichFaces.$('" + panelId + "').prevItem('tab1')"));
        assertEquals(result, "tab5", "Result of function prevItem('tab1')");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10061")
    public void testData() {
        selenium.type(pjq("input[type=text][id$=dataInput]"), "RichFaces 4");
        selenium.waitForPageToLoad();

        selenium.type(pjq("input[type=text][id$=oncompleteInput]"), "data = event.data");
        selenium.waitForPageToLoad();

        guardXhr(selenium).click(inactiveHeaders[2]);
        waitGui.failWith("Item 3 is not displayed.").until(isDisplayed.locator(itemContents[2]));

        String data = selenium.getEval(new JavaScript("window.data"));
        assertEquals(data, "RichFaces 4", "Data sent with ajax request");
    }

    @Test
    public void testDir() {
        super.testDir(panel);
    }

    @Test
    public void testExecute() {
        selenium.type(pjq("input[type=text][id$=executeInput]"), "@this executeChecker");
        selenium.waitForPageToLoad();

        guardXhr(selenium).click(inactiveHeaders[2]);
        waitGui.failWith("Item 3 is not displayed.").until(isDisplayed.locator(itemContents[2]));

        JQueryLocator logItems = jq("ul.phases-list li:eq({0})");
        for (int i = 0; i < 6; i++) {
            if ("* executeChecker".equals(selenium.getText(logItems.format(i)))) {
                return;
            }
        }

        fail("Attribute execute does not work");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10054")
    public void testImmediate() {
        selenium.click(pjq("input[type=radio][name$=immediateInput][value=true]"));
        selenium.waitForPageToLoad();

        selenium.click(inactiveHeaders[2]);
        waitGui.failWith("Tab 3 is not displayed.").until(isDisplayed.locator(itemContents[2]));

        phaseInfo.assertPhases(PhaseId.RESTORE_VIEW, PhaseId.APPLY_REQUEST_VALUES, PhaseId.RENDER_RESPONSE);

        String listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(2)"));
        assertEquals(listenerOutput, "* item changed: tab1 -> tab3", "Item change listener's output");
    }

    @Test
    public void testItemChangeListener() {
        selenium.click(inactiveHeaders[2]);
        waitGui.failWith("Item 3 is not displayed.").until(isDisplayed.locator(itemContents[2]));

        String listenerOutput = selenium.getText(jq("div#phasesPanel li:eq(5)"));
        assertEquals(listenerOutput, "* item changed: tab1 -> tab3", "Item change listener's output");
    }

    @Test
    public void testLang() {
        testLang(panel);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9535")
    public void testLimitRender() {
        selenium.type(pjq("input[type=text][id$=renderInput]"), "@this");
        selenium.waitForPageToLoad();

        selenium.click(pjq("input[type=radio][name$=limitRenderInput][value=true]"));
        selenium.waitForPageToLoad();

        String timeValue = selenium.getText(time);

        guardXhr(selenium).click(inactiveHeaders[1]);
        waitGui.failWith("Tab 2 is not displayed.").until(isDisplayed.locator(itemContents[1]));

        String newTimeValue = selenium.getText(time);
        assertNotSame(newTimeValue, timeValue, "Panel with ajaxRendered=true should not be rerendered.");
    }

    @Test
    public void testAjaxEvents() {
        selenium.type(pjq("input[type=text][id$=onbeginInput]"), "metamerEvents += \"begin \"");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[type=text][id$=onbeforedomupdateInput]"), "metamerEvents += \"beforedomupdate \"");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[type=text][id$=oncompleteInput]"), "metamerEvents += \"complete \"");
        selenium.waitForPageToLoad();

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));

        guardXhr(selenium).click(inactiveHeaders[2]);
        waitGui.failWith("Item 3 is not displayed.").until(isDisplayed.locator(itemContents[2]));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events.length, 3, "3 events should be fired.");
        assertEquals(events[0], "begin", "Attribute onbegin doesn't work");
        assertEquals(events[1], "beforedomupdate", "Attribute onbeforedomupdate doesn't work");
        assertEquals(events[2], "complete", "Attribute oncomplete doesn't work");
    }

    @Test
    public void testOnbeforeitemchange() {
        selenium.type(pjq("input[id$=onbeforeitemchangeInput]"), "metamerEvents += \"onbeforeitemchange \"");
        selenium.waitForPageToLoad(TIMEOUT);

        guardXhr(selenium).click(inactiveHeaders[1]);
        waitGui.failWith("Tab 2 is not displayed.").until(isDisplayed.locator(itemContents[1]));

        waitGui.failWith("onbeforeitemchange attribute does not work correctly").until(new EventFiredCondition(new Event("beforeitemchange")));
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10165")
    public void testItemchangeEvents() {
        selenium.type(pjq("input[type=text][id$=onbeforeitemchangeInput]"), "metamerEvents += \"beforeitemchange \"");
        selenium.waitForPageToLoad();
        selenium.type(pjq("input[type=text][id$=onitemchangeInput]"), "metamerEvents += \"itemchange \"");
        selenium.waitForPageToLoad();

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));
        String time1Value = selenium.getText(time);

        guardXhr(selenium).click(inactiveHeaders[1]);
        waitGui.failWith("Page was not updated").waitForChange(time1Value, retrieveText.locator(time));

        String[] events = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");

        assertEquals(events.length, 2, "Two events should be fired - beforeitemchange and itemchange.");
        assertEquals(events[0], "beforeitemchange", "Attribute onbeforeitemchange doesn't work");
        assertEquals(events[1], "itemchange", "Attribute onitemchange doesn't work");
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, panel);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, panel);
    }

    @Test
    public void testOnitemchange() {
        selenium.type(pjq("input[id$=onitemchangeInput]"), "metamerEvents += \"onitemchange \"");
        selenium.waitForPageToLoad(TIMEOUT);

        guardXhr(selenium).click(inactiveHeaders[1]);
        waitGui.failWith("Tab 2 is not displayed.").until(isDisplayed.locator(itemContents[1]));

        waitGui.failWith("onitemchange attribute does not work correctly").until(new EventFiredCondition(new Event("itemchange")));
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, panel);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, panel);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, panel);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, panel);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, panel);
    }

    @Test
    public void testRender() {
        selenium.type(pjq("input[type=text][id$=renderInput]"), "renderChecker");
        selenium.waitForPageToLoad();

        String renderCheckerTime = selenium.getText(renderChecker);
        guardXhr(selenium).click(inactiveHeaders[1]);
        waitGui.failWith("Attribute render doesn't work").waitForChange(renderCheckerTime, retrieveText.locator(renderChecker));
    }

    @Test
    public void testRendered() {
        selenium.click(pjq("input[type=radio][name$=renderedInput][value=false]"));
        selenium.waitForPageToLoad();

        assertFalse(selenium.isElementPresent(panel), "Tab panel should not be rendered when rendered=false.");
    }

    @Test
    public void testStatus() {
        selenium.type(pjq("input[type=text][id$=statusInput]"), "statusChecker");
        selenium.waitForPageToLoad();

        String statusCheckerTime = selenium.getText(statusChecker);
        guardXhr(selenium).click(inactiveHeaders[1]);
        waitGui.failWith("Attribute status doesn't work").waitForChange(statusCheckerTime, retrieveText.locator(statusChecker));
    }

    @Test
    public void testStyle() {
        testStyle(panel, "style");
    }

    @Test
    public void testStyleClass() {
        testStyleClass(panel, "styleClass");
    }

    @Test
    public void testSwitchTypeNull() {
        for (int i = 2; i >= 0; i--) {
            final int index = i;
            guardXhr(selenium).click(inactiveHeaders[index]);
            waitGui.failWith("Tab " + (index + 1) + " is not displayed.").until(isDisplayed.locator(itemContents[index]));
        }
    }

    @Test
    public void testSwitchTypeAjax() {
        selenium.click(pjq("input[name$=switchTypeInput][value=ajax]"));
        selenium.waitForPageToLoad();

        testSwitchTypeNull();
    }

    @Test
    public void testSwitchTypeClient() {
        selenium.click(pjq("input[name$=switchTypeInput][value=client]"));
        selenium.waitForPageToLoad();

        for (int i = 2; i >= 0; i--) {
            final int index = i;
            guardNoRequest(selenium).click(inactiveHeaders[index]);
            waitGui.failWith("Tab " + (index + 1) + " is not displayed.").until(isDisplayed.locator(itemContents[index]));
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10040")
    public void testSwitchTypeServer() {
        selenium.click(pjq("input[name$=switchTypeInput][value=server]"));
        selenium.waitForPageToLoad();

        for (int i = 2; i >= 0; i--) {
            final int index = i;
            guardHttp(selenium).click(inactiveHeaders[index]);
            waitGui.failWith("Tab " + (index + 1) + " is not displayed.").until(isDisplayed.locator(itemContents[index]));
        }
    }

    @Test
    public void testTabContentClass() {
        final String value = "metamer-ftest-class";

        selenium.type(pjq("input[id$=tabContentClassInput]"), value);
        selenium.waitForPageToLoad();

        assertTrue(selenium.belongsClass(itemContents[0], value), "tabContentClass does not work");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9309")
    public void testTabHeaderClass() {
        selenium.type(pjq("input[id$=tabHeaderClassInput]"), "metamer-ftest-class");
        selenium.waitForPageToLoad();

        for (JQueryLocator loc : activeHeaders) {
            assertTrue(selenium.belongsClass(loc, "metamer-ftest-class"), "tabHeaderClass does not work");
        }

        for (JQueryLocator loc : inactiveHeaders) {
            assertTrue(selenium.belongsClass(loc, "metamer-ftest-class"), "tabHeaderClass does not work");
        }

        for (JQueryLocator loc : disabledHeaders) {
            assertTrue(selenium.belongsClass(loc, "metamer-ftest-class"), "tabHeaderClass does not work");
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9309")
    public void testTabHeaderClassActive() {
        selenium.type(pjq("input[id$=tabHeaderClassActiveInput]"), "metamer-ftest-class");
        selenium.waitForPageToLoad();

        for (JQueryLocator loc : activeHeaders) {
            assertTrue(selenium.belongsClass(loc, "metamer-ftest-class"), "tabHeaderClassActive does not work");
        }

        for (JQueryLocator loc : inactiveHeaders) {
            assertFalse(selenium.belongsClass(loc, "metamer-ftest-class"), "tabHeaderClassActive does not work");
        }

        for (JQueryLocator loc : disabledHeaders) {
            assertFalse(selenium.belongsClass(loc, "metamer-ftest-class"), "tabHeaderClassActive does not work");
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9309")
    public void testTabHeaderClassDisabled() {
        selenium.type(pjq("input[id$=tabHeaderClassDisabledInput]"), "metamer-ftest-class");
        selenium.waitForPageToLoad();

        for (JQueryLocator loc : activeHeaders) {
            assertFalse(selenium.belongsClass(loc, "metamer-ftest-class"), "tabHeaderClassDisabled does not work");
        }

        for (JQueryLocator loc : inactiveHeaders) {
            assertFalse(selenium.belongsClass(loc, "metamer-ftest-class"), "tabHeaderClassDisabled does not work");
        }

        for (JQueryLocator loc : disabledHeaders) {
            assertTrue(selenium.belongsClass(loc, "metamer-ftest-class"), "tabHeaderClassDisabled does not work");
        }
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9309")
    public void testTabHeaderClassInactive() {
        selenium.type(pjq("input[id$=tabHeaderClassInactiveInput]"), "metamer-ftest-class");
        selenium.waitForPageToLoad();

        for (JQueryLocator loc : activeHeaders) {
            assertFalse(selenium.belongsClass(loc, "metamer-ftest-class"), "tabHeaderClassInactive does not work");
        }

        for (JQueryLocator loc : inactiveHeaders) {
            assertTrue(selenium.belongsClass(loc, "metamer-ftest-class"), "tabHeaderClassInactive does not work");
        }

        for (JQueryLocator loc : disabledHeaders) {
            assertFalse(selenium.belongsClass(loc, "metamer-ftest-class"), "tabHeaderClassInactive does not work");
        }
    }

    @Test
    public void testTitle() {
        testTitle(panel);
    }
}
