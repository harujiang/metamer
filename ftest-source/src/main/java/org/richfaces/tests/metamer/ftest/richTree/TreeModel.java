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
package org.richfaces.tests.metamer.ftest.richTree;

import org.jboss.test.selenium.locator.JQueryLocator;
import org.richfaces.component.SwitchType;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TreeModel extends AbstractTreeNodeModel {

    SwitchType toggleType;
    SwitchType selectionType;

    public TreeModel(JQueryLocator root) {
        super(root);
        this.setTree(this);
    }

    public void setToggleType(SwitchType toggleType) {
        this.toggleType = toggleType;
    }

    public SwitchType getToggleType() {
        return toggleType;
    }

    public void setSelectionType(SwitchType selectionType) {
        this.selectionType = selectionType;
    }

    public SwitchType getSelectionType() {
        return selectionType;
    }
    
    public TreeNodeModel getAnyNode() {
        return new TreeNodeModel(root.getLocator().getDescendant(treeNode));
    }
}
