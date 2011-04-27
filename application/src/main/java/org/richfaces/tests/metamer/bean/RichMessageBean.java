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
package org.richfaces.tests.metamer.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.UIRichMessage;
import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple bean for rich:message component example.
 * 
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @version $Revision$
 */
@ManagedBean(name = "richMessageBean")
@ViewScoped
public class RichMessageBean implements Serializable {

    /** Generated UID */
    private static final long serialVersionUID = -5058242586244822846L;
    private static Logger logger;
    private Attributes attributes;
    private String simpleInput1;
    private String simpleInput2;

    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.info("initializing bean " + getClass().getName());
        attributes = Attributes.getComponentAttributesFromFacesConfig(UIRichMessage.class, getClass());
       
        // setting up incorrect values to fire-up validator
        simpleInput1 = "-5";
        simpleInput2 = "-5";
        
        // to get working this component example correctly is required that for
        // property has been initialized
        attributes.setAttribute("for", "simpleInput1");
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("showSummary", true);
        attributes.setAttribute("ajaxRendered", true); // make sense for a4j:commandButton submit

        // workaround for missing attribute def from rich.taglib.xml 
        // to get it in list of attrs in metamer        
        attributes.setAttribute("childCount", 0);
        attributes.setAttribute("children", null);
        attributes.setAttribute("dir", null); // direction
        attributes.setAttribute("facets", null);
        attributes.setAttribute("family", null);
        attributes.setAttribute("lang", null);
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
        // attributes.setAttribute("parent", null);
        attributes.setAttribute("rendererType", null);
        attributes.setAttribute("rendersChildren", null);
        attributes.setAttribute("style", null);
        attributes.setAttribute("styleClass", null);
        attributes.setAttribute("title", null);

    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public String getSimpleInput2() {
        return simpleInput2;
    }

    public void setSimpleInput2(String simpleInput2) {
        this.simpleInput2 = simpleInput2;
    }

    public String getSimpleInput1() {
        return simpleInput1;
    }

    public void setSimpleInput1(String simpleInput1) {
        this.simpleInput1 = simpleInput1;
    }
}
