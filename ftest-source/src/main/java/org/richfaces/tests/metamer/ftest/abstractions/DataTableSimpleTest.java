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
package org.richfaces.tests.metamer.ftest.abstractions;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.jboss.test.selenium.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.model.Capital;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public abstract class DataTableSimpleTest extends AbstractDataTableTest {

    protected static final int COLUMNS_TOTAL = 2;

    @Inject
    protected Integer first = null;

    @Inject
    protected Integer rows = 30;

    private int expectedFirst;
    private int expectedRows;
    private List<Capital> expectedElements;

    @BeforeMethod
    public void setup() {
        attributes.setFirst(first);
        attributes.setRows(rows);

        if (first == null) {
            expectedFirst = 0;
        } else {
            expectedFirst = Math.min(ELEMENTS_TOTAL, first);
        }

        if (rows == null) {
            expectedRows = ELEMENTS_TOTAL - first;
        } else {
            expectedRows = rows - first;
        }

        expectedElements = getExpectedElements();
    }

    @Test
    public void testRendered() {
        assertFalse(model.isVisible());
        assertFalse(model.isNoData());
        assertEquals(model.getColumns(), 0);
        assertEquals(model.getRows(), 0);
    }

    @Test
    public void testNoDataLabel() {
        assertTrue(model.isVisible());
        assertTrue(model.isNoData());
        assertEquals(model.getColumns(), 0);
        assertEquals(model.getRows(), 0);
        assertEquals(selenium.getText(model.getNoData()), "There is no data.");
    }

    @Test
    @Use(field = "first", value = "COUNTS")
    public void testFirst() {
        verifyTable();
    }

    @Test
    @Use(field = "rows", value = "COUNTS")
    public void testRows() {
        verifyTable();
    }

    public void verifyTable() {
        assertTrue(model.isVisible());
        assertFalse(model.isNoData());
        assertEquals(model.getColumns(), COLUMNS_TOTAL);
        assertEquals(model.getRows(), expectedRows);
    }

    public void verifyElements() {
        for (int i = 0; i < expectedRows; i++) {
            Capital expectedCapital = expectedElements.get(i);
            JQueryLocator stateLocator = model.getElement(1, i);
            JQueryLocator capitalLocator = model.getElement(1, i);

            assertEquals(selenium.getText(stateLocator), expectedCapital.getState());
            assertEquals(selenium.getText(capitalLocator), expectedCapital.getState());
        }
    }

    public List<Capital> getExpectedElements() {
        return CAPITALS.subList(expectedFirst, expectedFirst + expectedRows);
    }
}
