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
package org.richfaces.tests.metamer.model;

import org.richfaces.model.CalendarDataModelItem;

/**
 * Item of calendar data model.
 *
 * @author Ilya Shaikovsky, Exadel
 * @version $Revision$
 */
public class CalendarModelItem implements CalendarDataModelItem {

    private boolean enabled;
    private String styleClass;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public Object getData() {
        return null;
    }

    public boolean hasToolTip() {
        return false;
    }

    public Object getToolTip() {
        return null;
    }

    public int getDay() {
        return 0;
    }
}
