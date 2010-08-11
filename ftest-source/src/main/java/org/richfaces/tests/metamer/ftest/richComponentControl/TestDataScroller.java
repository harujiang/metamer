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
package org.richfaces.tests.metamer.ftest.richComponentControl;

import static org.jboss.test.selenium.utils.URLUtils.buildUrl;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.AbstractMetamerTest;
import org.richfaces.tests.metamer.ftest.richDataScroller.PaginationTester;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TestDataScroller extends AbstractMetamerTest {

    ComponentControlDataScroller dataScroller = new ComponentControlDataScroller();

    PaginationTester paginationTester = new PaginationTester() {

        @Override
        protected void verifyBeforeScrolling() {

        }

        @Override
        protected void verifyAfterScrolling() {

        }
    };

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richComponentControl/simple.xhtml");
    }

    @BeforeMethod
    public void setup() {
        paginationTester.setDataScroller(dataScroller);
        int lastPage = dataScroller.obtainLastPage();
        dataScroller.setLastPage(lastPage);
        paginationTester.initializeTestedPages(lastPage);
    }

    @Test
    public void testScrollerPagination() {
        paginationTester.testNumberedPages();
    }
}
