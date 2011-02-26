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
package org.richfaces.tests.metamer.ftest.richExtendedDataTable;

import static org.jboss.test.selenium.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.richExtendedDataTable.TestExtendedDataTableSelection.Modifier.CTRL;
import static org.richfaces.tests.metamer.ftest.richExtendedDataTable.TestExtendedDataTableSelection.Modifier.SHIFT;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.IntRange;
import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.framework.AjaxSeleniumProxy;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.utils.array.ArrayTransform;
import org.jboss.test.selenium.waiting.retrievers.Retriever;
import org.richfaces.tests.metamer.ftest.abstractions.AbstractDataTableTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.model.ExtendedDataTable;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TestExtendedDataTableSelection extends AbstractDataTableTest {

    SelectionModel selection = new SelectionModel();
    Collection<Integer> selected;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richExtendedDataTable/selection.xhtml");
    }

    @BeforeClass
    public void setupModel() {
        model = new ExtendedDataTable(pjq("div.rf-edt[id$=richEDT]"));
    }

    @BeforeMethod
    public void setupAttributes() {
        attributes.setRows(10);
        selected = new TreeSet<Integer>();
    }

    @Test
    public void testSimpleSelection() {
        selection.click(2);

        assertEquals(selection.getPrevious(), selection());
        assertEquals(selection.getCurrent(), selection(2));
    }

    @Test
    public void testMultiSelectionUsingControl() {
        Collection<Integer> forSelection = order(2, 5, 29, 16, 13, 21);

        try {
            for (int s : forSelection) {
                selection.click(s, CTRL);

                assertEquals(selection.getPrevious(), selected);
                selected.add(s);
                assertEquals(selection.getCurrent(), selected);
            }
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Test
    public void testMultiSelectionUsingShiftOnOnePage() {
        IntRange range = new IntRange(2, 5);

        selection.click(range.getMinimumInteger());
        selection.click(range.getMaximumInteger(), SHIFT);

        assertEquals(selection.getPrevious(), selection(range.getMinimumInteger()));
        assertEquals(selection.getCurrent(), selection(range));
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10256")
    public void testMultiSelectionUsingShiftBetweenPagesInReversedOrder() {
        IntRange range = new IntRange(12, 35);

        selection.click(range.getMaximumInteger());
        selection.click(range.getMinimumInteger(), SHIFT);

        assertEquals(selection.getPrevious(), selection(range.getMaximumInteger()));
        assertEquals(selection.getCurrent(), selection(range));
    }

    @Test(groups = "4.Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-9977")
    public void testMultiSelectionUsingCtrlAndShiftCombinations() {
        IntRange range1 = new IntRange(2, 14);
        IntRange range2 = new IntRange(18, 31);

        selection.click(range1.getMaximumInteger());
        selection.click(range1.getMinimumInteger(), SHIFT);
        selected.addAll(selection(range1));
        verifySelected();

        selection.click(range2.getMaximumInteger(), CTRL);
        selected.addAll(selection(range2.getMaximumInteger()));
        verifySelected();

        selection.click(range2.getMinimumInteger(), CTRL, SHIFT);
        selected.addAll(selection(range2));
        verifySelected();
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-10256")
    public void testMultiSelectionRemovingUsingCtrl() {
        IntRange range1 = new IntRange(2, 14);

        selection.click(range1.getMaximumInteger());
        selection.click(range1.getMinimumInteger(), SHIFT);
        selected.addAll(selection(range1));
        verifySelected();

        selection.click(4, CTRL);
        selected.remove(4);
        verifySelected();

        selection.click(12, CTRL);
        selected.remove(12);
        verifySelected();
    }

    private Collection<Integer> order(int... selection) {
        return Arrays.asList(ArrayUtils.toObject(selection));
    }

    private Collection<Integer> selection(int... selection) {
        return new TreeSet<Integer>(order(selection));
    }

    private Collection<Integer> selection(IntRange range) {
        return selection(range.toArray());
    }

    private void verifySelected() {
        assertEquals(selection.getCurrent(), selected);
    }

    private class SelectionModel {
        Retriever<String> retrieveCurrent = retrieveText.locator(pjq("span.currentSelection"));
        Retriever<String> retrievePrevious = retrieveText.locator(pjq("span.previousSelection"));

        public Collection<Integer> getCurrent() {
            return getSelection(retrieveCurrent.retrieve());
        }

        public Collection<Integer> getPrevious() {
            return getSelection(retrievePrevious.retrieve());
        }

        private Collection<Integer> getSelection(String selectionString) {
            String[] splitted = StringUtils.split(selectionString, "[], ");
            Integer[] transformed = new ArrayTransform<String, Integer>(Integer.class) {
                @Override
                public Integer transformation(String source) {
                    return Integer.valueOf(source);
                }
            }.transform(splitted);
            return new TreeSet<Integer>(Arrays.asList(transformed));
        }

        public void click(int index, Modifier... modifiers) {
            int page = getPageForIndex(index);
            dataScroller2.gotoPage(page);

            int row = getRowForIndex(index);
            JQueryLocator element = model.getElement(1, row);

            retrieveCurrent.initializeValue();
            for (Modifier modifier : modifiers) {
                modifier.down();
            }
            selenium.click(element);
            for (Modifier modifier : modifiers) {
                modifier.up();
            }
            waitAjax.waitForChange(retrieveCurrent);
        }

        private int getPageForIndex(int index) {
            int rows = attributes.getRows();
            return (index / rows) + 1;
        }

        private int getRowForIndex(int index) {
            return 1 + index - ((dataScroller2.getCurrentPage() - 1) * attributes.getRows());
        }
    }

    enum Modifier {
        CTRL, SHIFT;

        AjaxSelenium selenium = AjaxSeleniumProxy.getInstance();

        public void up() {
            switch (this) {
                case CTRL:
                    selenium.controlKeyUp();
                    break;
                case SHIFT:
                    selenium.shiftKeyUp();
                    break;
            }
        }

        public void down() {
            switch (this) {
                case CTRL:
                    selenium.controlKeyDown();
                    break;
                case SHIFT:
                    selenium.shiftKeyDown();
                    break;
            }
        }
    }

}
