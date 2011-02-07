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
package org.richfaces.tests.metamer.ftest.richPanelMenuGroup;

import static org.jboss.test.selenium.utils.URLUtils.buildUrl;
import static org.richfaces.PanelMenuMode.ajax;

import java.net.URL;

import org.richfaces.tests.metamer.ftest.AbstractMetamerTest;
import org.richfaces.tests.metamer.ftest.model.PanelMenu;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public abstract class AbstractPanelMenuGroupTest extends AbstractMetamerTest {

    PanelMenuGroupAttributes attributes = new PanelMenuGroupAttributes();
    PanelMenu menu = new PanelMenu(pjq("div.rf-pm[id$=panelMenu]"));
    PanelMenu.Group topGroup = menu.getGroupContains("Group 2");
    PanelMenu.Group subGroup = topGroup.getGroupContains("Group 2.3");
    PanelMenu.Icon leftIcon = topGroup.getLeftIcon();
    PanelMenu.Icon rightIcon = topGroup.getRightIcon();

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanelMenuGroup/simple.xhtml");
    }
    
    @BeforeMethod
    public void setupModes() {
        attributes.setMode(ajax);
        menu.setGroupMode(ajax);
    }
}
