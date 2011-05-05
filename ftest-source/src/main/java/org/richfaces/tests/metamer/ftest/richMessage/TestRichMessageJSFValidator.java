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
package org.richfaces.tests.metamer.ftest.richMessage;

import static org.jboss.test.selenium.utils.URLUtils.buildUrl;

import java.net.URL;

import org.jboss.test.selenium.locator.JQueryLocator;

/**
 * Test case for page /faces/components/richMessage/jsfValidator.xhtml
 *
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision$
 */
public class TestRichMessageJSFValidator extends RichMessageTest {

    /* (non-Javadoc)
     * @see org.richfaces.tests.metamer.ftest.AbstractMetamerTest#getTestUrl()
     */
    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richMessage/jsfValidator.xhtml");
    }
    
    @Override
    public JQueryLocator getTestElemLocator() {
        return mainMessage;
    }

    @Override
    public JQueryLocator getSummaryElemLocator() {
        return summary;
    }

    @Override
    public JQueryLocator getDetailElemLocator() {
        return detail;
    }

}
