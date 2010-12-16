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
package org.richfaces.tests.metamer.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.richfaces.component.UIPanel;

import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for rich:panel.
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision$
 */
@ManagedBean(name = "richPanelBean")
@SessionScoped
public class RichPanelBean implements Serializable {

    private static final long serialVersionUID = 48122475400649801L;
    private static Logger logger;
    private Attributes attributes;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        attributes = Attributes.getComponentAttributesFromClass(UIPanel.class, getClass());

        attributes.setAttribute("rendered", true);

        // FIXME these attributes were not found automatically
        attributes.setAttribute("bodyClass", null);
        attributes.setAttribute("header", null);
        attributes.setAttribute("headerClass", null);
        attributes.setAttribute("onclick", null);
        attributes.setAttribute("ondblclick", null);
        attributes.setAttribute("onkeydown", null);
        attributes.setAttribute("onkeypress", null);
        attributes.setAttribute("onkeyup", null);
        attributes.setAttribute("onmousedown", null);
        attributes.setAttribute("onmousemove", null);
        attributes.setAttribute("onmouseout", null);
        attributes.setAttribute("onmouseover", null);
        attributes.setAttribute("onmouseup", null);
        attributes.setAttribute("style", null);
        attributes.setAttribute("styleClass", null);
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }
}
