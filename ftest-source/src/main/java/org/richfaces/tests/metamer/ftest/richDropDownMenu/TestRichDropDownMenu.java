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
package org.richfaces.tests.metamer.ftest.richDropDownMenu;

import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardHttp;
import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardNoRequest;
import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardXhr;
import static org.jboss.test.selenium.locator.LocatorFactory.jq;
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
 * Test case for page /faces/components/richDropDownMenu/topMenu.xhtml
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision$
 */
public class TestRichDropDownMenu extends AbstractMetamerTest {

    private JQueryLocator fileMenu = pjq("div[id$=menu1]");
    private JQueryLocator fileMenuLabel = pjq("div[id$=menu1] div.rf-ddm-lbl-dec");
    private JQueryLocator fileMenuList = pjq("div[id$=menu1_list]");
    private JQueryLocator group = pjq("div[id$=menuGroup4]");
    private JQueryLocator groupList = pjq("div[id$=menuGroup4_list]");
    private JQueryLocator icon = fileMenu.getDescendant(jq("img.pic"));
    private JQueryLocator emptyIcon = fileMenu.getDescendant(jq("> span.rf-ddm-itm-ic > div.rf-ddm-emptyIcon"));
    private JQueryLocator menuItem41 = pjq("div[id$=menuItem41]");
    private JQueryLocator output = pjq("span[id$=output]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDropDownMenu/topMenu.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(selenium.isElementPresent(fileMenu), "Drop down menu \"File\" should be present on the page.");
        assertTrue(selenium.isVisible(fileMenu), "Drop down menu \"File\" should be visible on the page.");

        assertTrue(selenium.isElementPresent(group), "Menu group \"Save As...\" should be present on the page.");
        assertFalse(selenium.isVisible(group), "Menu group \"Save As...\" should not be visible on the page.");

        assertFalse(selenium.isDisplayed(fileMenuList), "Menu should not be expanded.");
        guardNoRequest(selenium).mouseOver(fileMenuLabel);
        assertTrue(selenium.isDisplayed(fileMenuList), "Menu should be expanded.");

        assertTrue(selenium.isElementPresent(group), "Menu group \"Save As...\" should be present on the page.");
        assertTrue(selenium.isVisible(group), "Menu group \"Save As...\" should be visible on the page.");

        assertTrue(selenium.isElementPresent(menuItem41), "Menu item \"Save\" should be present on the page.");
        assertFalse(selenium.isVisible(menuItem41), "Menu item \"Save\" should not be visible on the page.");

        assertFalse(selenium.isDisplayed(groupList), "Submenu should not be expanded.");
        guardNoRequest(selenium).mouseOver(group);
        assertTrue(selenium.isDisplayed(groupList), "Submenu should be expanded.");

        assertTrue(selenium.isElementPresent(menuItem41), "Menu item \"Save\" should be present on the page.");
        assertTrue(selenium.isVisible(menuItem41), "Menu item \"Save\" should be visible on the page.");

        assertTrue(selenium.isElementPresent(icon), "Icon of menu group should not be present on the page.");

        assertTrue(selenium.isElementPresent(fileMenuLabel), "Label of menu should be present on the page.");
        assertTrue(selenium.isVisible(fileMenuLabel), "Label of menu should be visible on the page.");

        assertEquals(selenium.getText(fileMenuLabel), "File", "Label of the menu");
    }

    @Test
    public void testDir() {
        testDir(fileMenu);
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10305")
    public void testDisabled() {
        selenium.click(pjq("input[type=radio][name$=disabledInput][value=true]"));
        selenium.waitForPageToLoad();

        assertTrue(selenium.belongsClass(fileMenu, "rf-ddm-dis"), "Menu should have class \"rf-ddm-dis\".");
        assertTrue(selenium.isElementPresent(icon), "Icon should be present on the page.");
        assertFalse(selenium.isElementPresent(fileMenuLabel), "File menu should have a disabled label.");
        assertTrue(selenium.isElementPresent(pjq("div[id$=menu1] div.rf-ddm-lbl-dis")), "File menu should have a disabled label.");
    }

    @Test
    public void testHideDelay() {
        selenium.type(pjq("input[id$=hideDelayInput]"), "3000");
        selenium.waitForPageToLoad();

        selenium.mouseOver(fileMenuLabel);
        selenium.mouseOut(fileMenuLabel);
        assertTrue(selenium.isDisplayed(fileMenuList), "Menu should be expanded.");
        waitFor(3000);
        assertFalse(selenium.isDisplayed(fileMenuList), "Menu should not be expanded.");
    }

    @Test
    public void testLang() {
        testLang(fileMenu);
    }

    @Test
    public void testModeAjax() {
        selenium.mouseOver(fileMenuLabel);
        guardXhr(selenium).click(menuItem41);

        waitGui.failWith("Output did not change.").until(textEquals.locator(output).text("Save"));
    }

    @Test
    public void testModeNone() {
        selenium.type(pjq("input[id$=modeInput]"), "");
        selenium.waitForPageToLoad();

        testModeServer();
    }

    @Test
    public void testModeServer() {
        selenium.type(pjq("input[id$=modeInput]"), "server");
        selenium.waitForPageToLoad();

        selenium.mouseOver(fileMenuLabel);
        guardHttp(selenium).click(menuItem41);
        assertEquals(selenium.getText(output), "Save", "Output did not change");
    }

    @Test
    public void testModeClient() {
        selenium.type(pjq("input[id$=modeInput]"), "client");
        selenium.waitForPageToLoad();

        selenium.mouseOver(fileMenuLabel);
        guardNoRequest(selenium).click(menuItem41);
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, fileMenu);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, fileMenu);
    }

    @Test
    public void testOngrouphide() {
        selenium.type(pjq("input[id$=ongrouphideInput]"), "metamerEvents += \"grouphide \"");
        selenium.waitForPageToLoad();

        selenium.mouseOver(fileMenuLabel);
        selenium.mouseOver(group);
        waitGui.interval(2000).withDelay(true).failWith("Menu group was not opened.").until(isDisplayed.locator(groupList));
        selenium.mouseOut(group);

        waitGui.failWith("Attribute ongrouphide does not work correctly").until(new EventFiredCondition(new Event("grouphide")));
    }

    @Test
    public void testOngroupshow() {
        selenium.type(pjq("input[id$=ongroupshowInput]"), "metamerEvents += \"groupshow \"");
        selenium.waitForPageToLoad();

        selenium.mouseOver(fileMenuLabel);
        selenium.mouseOver(group);
        waitGui.interval(2000).withDelay(true).failWith("Menu group was not opened.").until(isDisplayed.locator(group));

        waitGui.failWith("Attribute ongroupshow does not work correctly").until(new EventFiredCondition(new Event("groupshow")));
    }

    @Test
    public void testOnhide() {
        selenium.type(pjq("input[id$=onhideInput]"), "metamerEvents += \"hide \"");
        selenium.waitForPageToLoad();

        selenium.mouseOver(fileMenuLabel);
        selenium.mouseOut(fileMenuLabel);

        waitGui.failWith("Attribute onhide does not work correctly").until(new EventFiredCondition(new Event("hide")));
    }

    @Test
    public void testOnitemclick() {
        testFireEvent(Event.CLICK, menuItem41, "itemclick");
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, fileMenu);
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, fileMenu);
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, fileMenu);
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, fileMenu);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, fileMenu);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, fileMenu);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, fileMenu);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, fileMenu);
    }

    @Test
    public void testOnshow() {
        selenium.type(pjq("input[id$=onshowInput]"), "metamerEvents += \"show \"");
        selenium.waitForPageToLoad();

        selenium.mouseOver(fileMenuLabel);

        waitGui.failWith("Attribute onshow does not work correctly").until(new EventFiredCondition(new Event("show")));
    }

    @Test
    public void testPopupWidth() {
        assertEquals(selenium.getStyle(fileMenuList, CssProperty.WIDTH), "250px", "Default width of menu");

        selenium.type(pjq("input[id$=popupWidthInput]"), "150");
        selenium.waitForPageToLoad();
        assertEquals(selenium.getStyle(fileMenuList, CssProperty.WIDTH), "150px", "Width of menu");

        selenium.type(pjq("input[id$=popupWidthInput]"), "423");
        selenium.waitForPageToLoad();
        assertEquals(selenium.getStyle(fileMenuList, CssProperty.WIDTH), "423px", "Width of menu");
    }

    @Test
    public void testRendered() {
        selenium.click(pjq("input[type=radio][name$=renderedInput][value=false]"));
        selenium.waitForPageToLoad();

        assertFalse(selenium.isElementPresent(fileMenu), "Menu should not be rendered when rendered=false.");
        assertTrue(selenium.isDisplayed(fileMenu), "Menu should be displayed when item 1 is not rendered.");
    }

    @Test
    public void testShowDelay() {
        selenium.type(pjq("input[id$=showDelayInput]"), "3000");
        selenium.waitForPageToLoad();

        selenium.mouseOver(fileMenuLabel);
        assertFalse(selenium.isDisplayed(fileMenuList), "Menu should not be expanded.");
        waitGui.interval(3000).withDelay(true).failWith("Menu was not opened.").until(isDisplayed.locator(fileMenuList));
    }

    @Test
    public void testShowEvent() {
        selenium.type(pjq("input[id$=showEventInput]"), "click");
        selenium.waitForPageToLoad();

        selenium.mouseOver(fileMenuLabel);
        assertFalse(selenium.isDisplayed(fileMenuList), "Menu should not be expanded.");

        selenium.click(fileMenuLabel);
        waitGui.withDelay(true).failWith("Menu was not expanded.").until(isDisplayed.locator(fileMenuList));
    }

    @Test
    public void testStyle() {
        testStyle(fileMenu, "style");
    }

    @Test
    public void testStyleClass() {
        testStyleClass(fileMenu, "styleClass");
    }

    @Test
    public void testTitle() {
        testTitle(fileMenu);
    }
}
