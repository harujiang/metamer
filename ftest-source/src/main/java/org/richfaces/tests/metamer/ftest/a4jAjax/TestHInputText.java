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
package org.richfaces.tests.metamer.ftest.a4jAjax;

import static org.jboss.test.selenium.utils.URLUtils.buildUrl;

import java.net.URL;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;

import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/a4jAjax/hInputText.xhtml
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision$
 */
public class TestHInputText extends AbstractTestTextInput {

    private JQueryLocator input = pjq("input[type=text][id$=input]");

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/a4jAjax/hInputText.xhtml");
    }

    @Test
    public void testSimpleType() {
        super.testType(input, "RichFaces 4");
    }

    @Test
    @IssueTracking("https://issues.jboss.org/browse/RF-9665")
    public void testSimpleTypeUnicode() {
        super.testType(input, "ľščťžýáíéúôň фывацукйешгщь");
    }

    @Test
    public void testBypassUpdates() {
        super.testBypassUpdates(input);
    }

    @Test
    public void testData() {
        super.testData(input);
    }

    @Test
    public void testExecute() {
        super.testExecute(input);
    }

    @Test
    public void testImmediate() {
        super.testImmediate(input);
    }

    @Test
    public void testImmediateBypassUpdates() {
        super.testImmediateBypassUpdates(input);
    }

    @Test
    public void testLimitRender() {
        super.testLimitRender(input);
    }

    @Test
    public void testEvents() {
        super.testEvents(input);
    }

    @Test
    public void testRender() {
        super.testRender(input);
    }
}
