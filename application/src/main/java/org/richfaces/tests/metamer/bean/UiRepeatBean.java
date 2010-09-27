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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.tests.metamer.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed bean for a4j:repeat.
 * 
 * <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * 
 * @version $Revision$
 */
@ManagedBean(name = "uiRepeatBean")
@ViewScoped
public class UiRepeatBean implements Serializable {

    private static final long serialVersionUID = 4864439475400649809L;
    private static Logger logger;
    private Attributes attributes;
    private List<Data> dataList;
    private Data selectedDataItem = null;

    private Object binding;

    /**
     * Initializes the managed bean.
     */
    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(getClass());
        logger.debug("initializing bean " + getClass().getName());

        // initialize model for page simple.xhtml
        dataList = new ArrayList<Data>();
        for (int i = 0; i < 20; i++) {
            Data data = new Data();
            data.setText(MessageFormat.format("Item {0}", i));
            dataList.add(data);
        }

        // initialize attributes
        attributes = Attributes.getUIComponentAttributes(com.sun.faces.facelets.component.UIRepeat.class, getClass());
        attributes.setAttribute("rendered", true);
        attributes.setAttribute("step", 1);
        attributes.setAttribute("end", 20);

        // should be hiddens
        attributes.remove("size");
        attributes.remove("offset");
        attributes.remove("value");
        attributes.remove("var");
        attributes.remove("varStatus");
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the data
     */
    public List<Data> getDataList() {
        return dataList;
    }

    /**
     * @return the selectedDataItem
     */
    public Data getSelectedDataItem() {
        return selectedDataItem;
    }

    /**
     * @param selectedDataItem
     *            the selectedDataItem to set
     */
    public void setSelectedDataItem(Data selectedDataItem) {
        this.selectedDataItem = selectedDataItem;
    }

    public static final class Data implements Serializable {

        private static final long serialVersionUID = -1461777632529492912L;
        private String text;

        /**
         * @return the text
         */
        public String getText() {
            return text;
        }

        /**
         * @param text
         *            the text to set
         */
        public void setText(String text) {
            this.text = text;
        }
    }
}
