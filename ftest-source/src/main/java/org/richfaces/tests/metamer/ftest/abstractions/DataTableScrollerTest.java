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

import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.model.DataScroller;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public abstract class DataTableScrollerTest extends AbstractDataTableTest {

    @Test
    public void testRowCountFooterScroller() {
        testRowCount(dataScroller2);
    }

    @Test
    @Templates(exclude = { "a4jRepeat1", "a4jRepeat2", "hDataTable1", "hDataTable2", "richDataTable1,redDiv",
        "richDataTable2,redDiv", "uiRepeat1", "uiRepeat2" })
    public void testRowCountOutsideTable() {
        testRowCount(dataScroller1);
    }

    private void testRowCount(DataScroller dataScroller) {
        for (Integer rowsPerPage : COUNTS) {
            if (rowsPerPage != null) {
                attributes.setRows(rowsPerPage);
                selenium.waitForPageToLoad();
            }

            dataScroller.gotoFirstPage();
            int rowCountPreset = attributes.getRows();
            int rowCountActual = model.getRows();
            assertEquals(rowCountActual, Math.min(ELEMENTS_TOTAL, rowCountPreset));

            assertEquals(dataScroller.hasPages(), rowCountActual < ELEMENTS_TOTAL);
            if (dataScroller.hasPages()) {
                dataScroller.gotoLastPage();

                int pagesExpected = pageCountActualExpected(rowCountActual);
                int countOfVisiblePages = dataScroller.getCountOfVisiblePages();

                if (countOfVisiblePages < pagesExpected) {
                    int lastVisiblePage = dataScroller.getLastVisiblePage();
                    assertEquals(lastVisiblePage, pageCountActualExpected(rowCountActual));
                } else {
                    assertEquals(countOfVisiblePages, pagesExpected);
                }

                int rowCountExpected = rowCountLastPageExpected(rowCountPreset);
                rowCountActual = model.getRows();
                assertEquals(rowCountActual, rowCountExpected);
            }
        }
    }

    private int pageCountActualExpected(int rowCountActual) {
        return Double.valueOf(Math.ceil((double) ELEMENTS_TOTAL / rowCountActual)).intValue();
    }

    private int rowCountLastPageExpected(int rowCountPreset) {
        int result = ELEMENTS_TOTAL % rowCountPreset;
        return (result == 0) ? rowCountPreset : result;
    }
}
