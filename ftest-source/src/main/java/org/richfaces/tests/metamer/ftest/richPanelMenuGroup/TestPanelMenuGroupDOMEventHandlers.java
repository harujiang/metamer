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
package org.richfaces.tests.metamer.ftest.richPanelMenuGroup;

import static org.jboss.test.selenium.dom.Event.CLICK;
import static org.jboss.test.selenium.dom.Event.DBLCLICK;
import static org.jboss.test.selenium.dom.Event.MOUSEDOWN;
import static org.jboss.test.selenium.dom.Event.MOUSEMOVE;
import static org.jboss.test.selenium.dom.Event.MOUSEOUT;
import static org.jboss.test.selenium.dom.Event.MOUSEOVER;
import static org.jboss.test.selenium.dom.Event.MOUSEUP;

import org.jboss.test.selenium.dom.Event;
import org.richfaces.PanelMenuMode;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TestPanelMenuGroupDOMEventHandlers extends AbstractPanelMenuGroupTest {

    @Inject
    @Use("events")
    Event event;
    Event[] events = new Event[] { CLICK, DBLCLICK, MOUSEDOWN, MOUSEMOVE, MOUSEOUT, MOUSEOVER, MOUSEUP };

    @Test
    public void testDOMEventHandler() {
        attributes.setMode(PanelMenuMode.client);
        super.testFireEvent(event, topGroup);
    }
}
