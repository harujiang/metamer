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
package org.richfaces.tests.metamer.ftest.richColumn;

import static org.testng.Assert.assertEquals;

import java.util.Collections;

import org.jboss.test.selenium.request.RequestType;
import org.richfaces.component.SortOrder;
import org.richfaces.tests.metamer.bean.rich.RichColumnBean;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.model.Capital;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public abstract class AbstractColumnSortingTest extends AbstractColumnModelTest {

    @Inject
    @Use(enumeration = true)
    SortOrder sortOrder;
    
    public void testSortingWithSortOrder() {
        attributes.setRequestType(RequestType.XHR);
        attributes.setSortOrder(sortOrder);

        switch (sortOrder) {
            case ascending:
                Collections.sort(capitals, RichColumnBean.STATE_NAME_LENGTH_COMPARATOR);
                break;
            case descending:
                Collections.sort(capitals, Collections.reverseOrder(RichColumnBean.STATE_NAME_LENGTH_COMPARATOR));
                break;
                
            default:
                // default case required by checkstyle
                break;
        }

        for (int i = 0; i < capitals.size(); i++) {
            Capital actualCapital = model.getCapital(i);
            Capital expectedCapital = capitals.get(i);

            assertEquals(actualCapital.getName(), expectedCapital.getName());
            assertEquals(actualCapital.getState(), expectedCapital.getState());
        }
    }
}
