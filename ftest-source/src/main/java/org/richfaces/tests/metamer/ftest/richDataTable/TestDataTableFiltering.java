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
package org.richfaces.tests.metamer.ftest.richDataTable;

import static org.jboss.test.selenium.utils.URLUtils.buildUrl;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.abstractions.DataTableFilteringTest;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.model.DataTable;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TestDataTableFiltering extends DataTableFilteringTest {

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richDataTable/filtering.xhtml");
    }

    @BeforeClass
    public void setupModel() {
        model = new DataTable(pjq("table.rf-dt[id$=richDataTable]"));
    }
    
    @Test
    @Override
    public void testFilterSex() {
        super.testFilterSex();
    }

    @Test
    @Override
    public void testFilterName() {
        super.testFilterName();
    }

    @Test
    @Override
    public void testFilterTitle() {
        super.testFilterTitle();
    }

    @Test
    @Override
    public void testFilterNumberOfKids1() {
        super.testFilterNumberOfKids1();
    }

    @Test
    @Override
    public void testFilterCombinations() {
        super.testFilterCombinations();
    }

    @Test
    @Override
    @IssueTracking("http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-790")
    public void testRerenderAll() {
        super.testRerenderAll();
    }

    @Test
    @Override
    public void testFullPageRefresh() {
        super.testFullPageRefresh();
    }
}
