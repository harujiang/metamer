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
package org.richfaces.tests.metamer.ftest.richDataScroller;

import org.jboss.test.selenium.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.model.AssertingDataScroller;
import org.richfaces.tests.metamer.ftest.model.DataScroller;

import static org.richfaces.tests.metamer.ftest.AbstractMetamerTest.pjq;
import static java.lang.Math.*;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public abstract class PaginationTester {

    public static final JQueryLocator DATA_SCROLLER_OUTSIDE_TABLE = pjq("span.rf-ds[id$=scroller1]");
    public static final JQueryLocator DATA_SCROLLER_IN_TABLE_FOOTER = pjq("span.rf-ds[id$=scroller2]");

    protected AssertingDataScroller dataScroller = new AssertingDataScroller(null);
    private int[] testPages;

    public void initializeTestedPages(int lastPage) {
        int l = lastPage;
        testPages = new int[] { 3, l, 1, l - 2, l, 2, l - 2, l - 1 };
        for (int i = 0; i < testPages.length; i++) {
            testPages[i] = min(l, max(1, testPages[i]));
        }
    }

    public DataScroller getDataScroller() {
        return dataScroller;
    }

    public void testNumberedPages() {
        Integer lastNumber = null;
        for (int pageNumber : testPages) {
            if (lastNumber == (Integer) pageNumber) {
                continue;
            }
            verifyBeforeScrolling();
            dataScroller.gotoPage(pageNumber);
            verifyAfterScrolling();
            lastNumber = pageNumber;
        }
    }

    protected abstract void verifyBeforeScrolling();

    protected abstract void verifyAfterScrolling();
}
