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
package org.richfaces.tests.metamer.ftest;

import org.jboss.test.selenium.dom.Event;
import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.framework.AjaxSeleniumProxy;
import org.jboss.test.selenium.locator.Attribute;
import org.jboss.test.selenium.locator.AttributeLocator;
import org.jboss.test.selenium.locator.ElementLocator;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.locator.ExtendedLocator;
import org.jboss.test.selenium.locator.reference.LocatorReference;
import org.jboss.test.selenium.locator.reference.ReferencedLocator;

import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardHttp;
import static org.richfaces.tests.metamer.ftest.AbstractMetamerTest.pjq;
import static org.jboss.test.selenium.locator.reference.ReferencedLocator.referenceInferred;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class AbstractComponentAttributes {

    protected AjaxSelenium selenium = AjaxSeleniumProxy.getInstance();
    LocatorReference<ExtendedLocator<JQueryLocator>> root = new LocatorReference<ExtendedLocator<JQueryLocator>>(
            pjq(""));
    ReferencedLocator<JQueryLocator> propertyLocator = referenceInferred(root, "input[id*={0}Input]{1}");

    public AbstractComponentAttributes() {
    }

    public <T extends ExtendedLocator<JQueryLocator>> AbstractComponentAttributes(T root) {
        this.root.setLocator(root);
    }

    protected String getProperty(String propertyName) {
        final ElementLocator<?> locator = propertyLocator.format(propertyName);
        return selenium.getValue(locator);
    }

    protected void setProperty(String propertyName, Object value) {
        ElementLocator<?> locator = propertyLocator.format(propertyName);
        final AttributeLocator<?> typeLocator = locator.getAttribute(Attribute.TYPE);

        String inputType = null;
        if (selenium.getCount(propertyLocator.format(propertyName)) > 1) {
            inputType = "radio";
        } else {
            inputType = selenium.getAttribute(typeLocator);
        }

        if (value == null) {
            value = "";
        }
        String valueAsString = value.toString();

        if ("text".equals(inputType)) {
            applyText(locator, valueAsString);
        } else if ("checkbox".equals(inputType)) {
            boolean checked = Boolean.valueOf(valueAsString);
            applyCheckbox(locator, checked);
        } else if ("radio".equals(inputType)) {
            locator = propertyLocator.format(propertyName, "[value=" + ("".equals(value) ? "null" : value) + "]");
            guardHttp(selenium).click(locator);
        }
    }

    protected void applyText(ElementLocator<?> locator, String value) {
        guardHttp(selenium).type(locator, value);
    }

    protected void applyCheckbox(ElementLocator<?> locator, boolean checked) {
        selenium.check(locator, checked);
        guardHttp(selenium).fireEvent(locator, Event.CHANGE);
    }
}
