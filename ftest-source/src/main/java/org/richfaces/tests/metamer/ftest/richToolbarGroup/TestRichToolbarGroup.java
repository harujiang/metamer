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
package org.richfaces.tests.metamer.ftest.richToolbarGroup;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.locator.option.OptionLocatorFactory.optionLabel;
import static org.jboss.test.selenium.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.test.selenium.dom.Event;
import org.jboss.test.selenium.locator.Attribute;
import org.jboss.test.selenium.locator.AttributeLocator;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.AbstractMetamerTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richToolbarGroup/simple.xhtml
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision$
 */
public class TestRichToolbarGroup extends AbstractMetamerTest {

    private JQueryLocator toolbar = pjq("table[id$=toolbar]");
    private JQueryLocator separator = pjq("td.rf-tb-sep");
    private JQueryLocator[] items = {pjq("td[id$=createDocument_itm]"), pjq("td[id$=createFolder_itm]"),
        pjq("td[id$=copy_itm]"), pjq("td[id$=save_itm]"), pjq("td[id$=saveAs_itm]"), pjq("td[id$=saveAll_itm]")};
    private JQueryLocator input = pjq("td[id$=input_itm]");
    private JQueryLocator button = pjq("td[id$=button_itm]");
    private String[] separators = {"disc", "grid", "line", "square"};
    @Inject
    @Use(empty = true)
    private JQueryLocator item;
    @Inject
    @Use(empty = true)
    private String itemSeparator;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richToolbarGroup/simple.xhtml");
    }

    @Test
    public void testInit() {
        assertTrue(selenium.isElementPresent(toolbar), "Toolbar should be present on the page.");
        assertTrue(selenium.isVisible(toolbar), "Toolbar should be visible.");
        assertFalse(selenium.isElementPresent(separator), "No item separator should be present on the page.");
        assertTrue(selenium.isElementPresent(input), "Input should be present on the page.");
        assertTrue(selenium.isVisible(input), "Input should be visible.");
        assertTrue(selenium.isElementPresent(button), "Button should be present on the page.");
        assertTrue(selenium.isVisible(button), "Button should be visible.");
    }

    @Test
    @Use(field = "item", value = "items")
    public void testInitItems() {
        assertTrue(selenium.isElementPresent(item), "Item (" + item + ") should be present on the page.");
        assertTrue(selenium.isVisible(item), "Item (" + item + ") should be visible.");
    }

    @Test
    @Use(field = "item", value = "items")
    @RegressionTest("https://issues.jboss.org/browse/RF-9976")
    public void testItemClass() {
        testStyleClass(item, "itemClass");
    }

    @Test
    @Use(field = "itemSeparator", value = "separators")
    public void testItemSeparatorCorrect() {
        selenium.select(pjq("select[id$=itemSeparatorInput]"), optionLabel(itemSeparator));
        selenium.waitForPageToLoad();

        JQueryLocator separatorDiv = separator.getDescendant(jq("div.rf-tb-sep-" + itemSeparator));

        assertTrue(selenium.isElementPresent(separator), "Item separator should be present on the page.");
        assertEquals(selenium.getCount(separator), 5, "Number of separators.");
        assertTrue(selenium.isElementPresent(separatorDiv), "Item separator does not work correctly.");
        assertEquals(selenium.getCount(separatorDiv), 5, "Number of separators.");
    }

    @Test
    public void testItemSeparatorNone() {
        selenium.select(pjq("select[id$=itemSeparatorInput]"), optionLabel("none"));
        selenium.waitForPageToLoad();

        assertFalse(selenium.isElementPresent(separator), "No item separator should be present on the page.");

        selenium.select(pjq("select[id$=itemSeparatorInput]"), optionLabel("null"));
        selenium.waitForPageToLoad();

        assertFalse(selenium.isElementPresent(separator), "No item separator should be present on the page.");
    }

    @Test
    public void testItemSeparatorCustom() {
        selenium.select(pjq("select[id$=itemSeparatorInput]"), optionLabel("star"));
        selenium.waitForPageToLoad();

        JQueryLocator separatorImg = separator.getDescendant(jq("> img"));
        AttributeLocator attr = separatorImg.getAttribute(Attribute.SRC);

        assertTrue(selenium.isElementPresent(separator), "Item separator should be present on the page.");
        assertEquals(selenium.getCount(separator), 5, "Number of separators.");
        assertTrue(selenium.isElementPresent(separatorImg), "Item separator do not work correctly.");
        assertEquals(selenium.getCount(separatorImg), 5, "Number of separators.");

        String src = selenium.getAttribute(attr);
        assertTrue(src.contains("star.png"), "Separator's image should link to picture star.png.");
    }

    @Test
    public void testItemSeparatorNonExisting() {
        JQueryLocator input = pjq("select[id$=itemSeparatorInput]");
        selenium.select(input, optionLabel("non-existing"));
        selenium.waitForPageToLoad();

        JQueryLocator separatorImg = separator.getDescendant(jq("> img"));
        AttributeLocator attr = separatorImg.getAttribute(Attribute.SRC);

        assertTrue(selenium.isElementPresent(separator), "Item separators should be present on the page.");
        assertEquals(selenium.getCount(separator), 5, "Number of separators.");
        assertTrue(selenium.isElementPresent(separatorImg), "Item separators do not work correctly.");
        assertEquals(selenium.getCount(separatorImg), 5, "Number of separators.");

        String src = selenium.getAttribute(attr);
        assertTrue(src.contains("non-existing"), "Separator's image should link to \"non-existing\".");
    }

    @Test
    @Use(field = "item", value = "items")
    @RegressionTest("https://issues.jboss.org/browse/RF-9976")
    public void testItemStyle() {
        testStyle(item, "itemStyle");
    }

    @Test
    public void testLocation() {
        JQueryLocator emptyCellBefore = toolbar.getDescendant(jq("td.rf-tb-emp:nth-child(1)"));
        JQueryLocator emptyCellAfter = toolbar.getDescendant(jq("td.rf-tb-emp:nth-child(7)"));

        assertFalse(selenium.isElementPresent(emptyCellBefore), "Toolbar group should  be located on the left.");
        assertTrue(selenium.isElementPresent(emptyCellAfter), "Toolbar group should  be located on the left.");

        selenium.type(pjq("input[id$=locationInput]"), "right");
        selenium.waitForPageToLoad();

        assertTrue(selenium.isElementPresent(emptyCellBefore), "Toolbar group should  be located on the right.");
        assertFalse(selenium.isElementPresent(emptyCellAfter), "Toolbar group should  be located on the right.");

        selenium.type(pjq("input[id$=locationInput]"), "wrong");
        selenium.waitForPageToLoad();

        assertFalse(selenium.isElementPresent(emptyCellBefore), "Toolbar group should  be located on the left.");
        assertTrue(selenium.isElementPresent(emptyCellAfter), "Toolbar group should  be located on the left.");
    }

    @Test
    @Use(field = "item", value = "items")
    public void testOnitemclick() {
        testFireEvent(Event.CLICK, item, "itemclick");
    }

    @Test
    @Use(field = "item", value = "items")
    public void testOnitemdblclick() {
        testFireEvent(Event.DBLCLICK, item, "itemdblclick");
    }

    @Test
    @Use(field = "item", value = "items")
    public void testOnitemkeydown() {
        testFireEvent(Event.KEYDOWN, item, "itemkeydown");
    }

    @Test
    @Use(field = "item", value = "items")
    public void testOnitemkeypress() {
        testFireEvent(Event.KEYPRESS, item, "itemkeypress");
    }

    @Test
    @Use(field = "item", value = "items")
    public void testOnitemkeyup() {
        testFireEvent(Event.KEYUP, item, "itemkeyup");
    }

    @Test
    @Use(field = "item", value = "items")
    public void testOnitemmousedown() {
        testFireEvent(Event.MOUSEDOWN, item, "itemmousedown");
    }

    @Test
    @Use(field = "item", value = "items")
    public void testOnitemmousemove() {
        testFireEvent(Event.MOUSEMOVE, item, "itemmousemove");
    }

    @Test
    @Use(field = "item", value = "items")
    public void testOnitemmouseout() {
        testFireEvent(Event.MOUSEOUT, item, "itemmouseout");
    }

    @Test
    @Use(field = "item", value = "items")
    public void testOnitemmouseover() {
        testFireEvent(Event.MOUSEOVER, item, "itemmouseover");
    }

    @Test
    @Use(field = "item", value = "items")
    public void testOnitemmouseup() {
        testFireEvent(Event.MOUSEUP, item, "itemmouseup");
    }

    @Test
    public void testRendered() {
        selenium.click(pjq("input[type=radio][name$=renderedInput][value=false]"));
        selenium.waitForPageToLoad();

        assertTrue(selenium.isElementPresent(toolbar), "Toolbar should be present on the page.");
        assertTrue(selenium.isVisible(toolbar), "Toolbar should be visible.");
        assertFalse(selenium.isElementPresent(separator), "No item separator should be present on the page.");
        assertTrue(selenium.isElementPresent(input), "Input should be present on the page.");
        assertTrue(selenium.isVisible(input), "Input should be visible.");
        assertTrue(selenium.isElementPresent(button), "Button should be present on the page.");
        assertTrue(selenium.isVisible(button), "Button should be visible.");

        for (int i = 0; i < 6; i++) {
            assertFalse(selenium.isElementPresent(items[i]), "Item " + (i + 1) + " should not be rendered.");
        }
    }
}
