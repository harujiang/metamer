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
package org.richfaces.tests.metamer.bean.rich;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.UITreeNode;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
@ManagedBean(name = "richTreeNodeBean")
@ViewScoped
public class RichTreeNodeBean implements Serializable {

    private static final long serialVersionUID = 4008175400649809L;
    private static Logger logger;
    private Attributes[] attributes = new Attributes[3];

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = Attributes.getComponentAttributesFromFacesConfig(UITreeNode.class, getClass());
            attributes[i].get("rendered").setValue(true);
            
            // attributes which needs to be tested another way
            attributes[i].remove("type");
            attributes[i].remove("expanded");
        }
    }

    public Attributes[] getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes[] attributes) {
        this.attributes = attributes;
    }
}
