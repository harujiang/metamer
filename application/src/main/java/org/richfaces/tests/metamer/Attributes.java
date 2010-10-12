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
package org.richfaces.tests.metamer;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.BehaviorBase;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import org.richfaces.tests.metamer.bean.RichBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Representation of all attributes of a JSF component.
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision$
 */
public final class Attributes implements Map<String, Attribute>, Serializable {

    private static final long serialVersionUID = -1L;
    private static Logger logger = LoggerFactory.getLogger(Attributes.class);
    // K - name of a component attribute, V - value of the component attribute
    private Map<String, Attribute> attributes;
    // class object of managed bean
    private Class<?> beanClass;

    /**
     * Constructor for class Attributes.
     * 
     * @param componentClass
     *            class object of a JSF component whose attributes will be stored
     * @param beanClass
     *            class object of a managed bean
     */
    private Attributes(Class<?> componentClass, Class<?> beanClass) {
        logger.debug("creating attributes map for " + componentClass);

        this.beanClass = beanClass;

        PropertyDescriptor[] descriptors = null;
        try {
            descriptors = Introspector.getBeanInfo(componentClass).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            logger.error("Could not get a list with attributes of class" + componentClass);
            attributes = Collections.emptyMap();
            return;
        }

        attributes = new TreeMap<String, Attribute>();
        // not all attributes of given class are needed
        Set<String> excludeSet = getExcludeSet();
        Attribute attribute = null;

        // create list of all attributes and their types
        for (PropertyDescriptor descriptor : descriptors) {
            if (!excludeSet.contains(descriptor.getName())) {
                attribute = new Attribute(descriptor.getName());
                attribute.setType(descriptor.getPropertyType());
                attributes.put(descriptor.getName(), attribute);
            }
        }

        logger.debug(attributes.keySet().toString());

        loadHelp();
        loadSelectOptions();
    }

    /**
     * Factory method for creating instances of class Attributes.
     * 
     * @param componentClass
     *            class object of a JSF component whose attributes will be stored
     * @param beanClass
     *            class object of a managed bean
     */
    public static Attributes getUIComponentAttributes(Class<? extends UIComponent> componentClass, Class<?> beanClass) {
        return new Attributes(componentClass, beanClass);
    }

    /**
     * Factory method for creating instances of class Attributes.
     * 
     * @param behaviorClass
     *            class object of a JSF behavior whose attributes will be stored
     * @param beanClass
     *            class object of a managed bean
     */
    public static Attributes getBehaviorAttributes(Class<? extends BehaviorBase> behaviorClass, Class<?> beanClass) {
        return new Attributes(behaviorClass, beanClass);
    }

    /**
     * Factory method for creating instances of class Attributes.
     *
     * @param behaviorClass
     *            class object of a JSF behavior whose attributes will be stored
     * @param beanClass
     *            class object of a managed bean
     */
    public static Attributes getFaceletsComponentAttributes(String componentClass, Class<?> beanClass) {
        Class<?> faceletsClass = null;

        try {
            faceletsClass = Class.forName(componentClass);
        } catch (ClassNotFoundException cnfe1) {
            try {
                if (componentClass.startsWith("com.sun.faces.facelets")) {
                    faceletsClass = Class.forName(componentClass.replace("com.sun.faces.facelets", "org.apache.myfaces.view.facelets"));
                } else {
                    faceletsClass = Class.forName(componentClass.replace("org.apache.myfaces.view.facelets", "com.sun.faces.facelets"));
                }
            } catch (ClassNotFoundException cnfe2) {
                logger.error(cnfe2.getMessage());
            }
        }

        return new Attributes(faceletsClass, beanClass);
    }

    /**
     * Loads help strings from property file.
     */
    private void loadHelp() {
        ResourceBundle rb = null;
        try {
            rb = ResourceBundle.getBundle(beanClass.getName());
        } catch (MissingResourceException mre) {
            return;
        }

        Enumeration<String> keys = rb.getKeys();
        String key = null;
        Attribute attribute = null;

        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            if (key.startsWith("testapp.help.")) {
                attribute = attributes.get(key.replaceFirst("testapp.help.", ""));
                if (attribute != null) {
                    attribute.setHelp(rb.getString(key));
                }
            }
        }
    }

    /**
     * Loads select options used on the page for selecting attribute value.
     * 
     * @return map where key is attribute's name and value is list of select items usable to select attribute value
     */
    private void loadSelectOptions() {
        ResourceBundle rb = null;
        try {
            rb = ResourceBundle.getBundle(beanClass.getName());
        } catch (MissingResourceException mre) {
            return;
        }

        Enumeration<String> keys = rb.getKeys();
        String key = null;

        // e.g. attr.action.toUpperCaseAction
        Pattern pattern = Pattern.compile("(.*)\\.(.*)\\.(.*)");
        Matcher matcher = null;
        SelectItem item = null;
        Attribute attribute = null;

        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            if (key.startsWith("attr.")) {
                matcher = pattern.matcher(key);
                matcher.find();

                attribute = attributes.get(matcher.group(2));
                if (attribute == null) {
                    continue;
                }

                if (attribute.getSelectOptions() == null) {
                    attribute.setSelectOptions(new ArrayList<SelectItem>());
                }

                item = new SelectItem(rb.getString(key), matcher.group(3));
                attribute.getSelectOptions().add(item);
            }
        }

        // sort all select options
        for (String aKey : attributes.keySet()) {
            List<SelectItem> selectOptions = attributes.get(aKey).getSelectOptions();
            if (selectOptions != null) {
                Collections.sort(selectOptions, new SelectItemComparator());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        attributes.clear();

    }

    /**
     * {@inheritDoc}
     */
    public boolean containsKey(String key) {
        return attributes.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    public boolean containsValue(String value) {
        return attributes.containsKey(value);
    }

    /**
     * {@inheritDoc}
     */
    public Set<Map.Entry<String, Attribute>> entrySet() {
        return attributes.entrySet();
    }

    /**
     * {@inheritDoc}
     */
    public Attribute get(String key) {
        return attributes.get(key);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public Set<String> keySet() {
        return attributes.keySet();
    }

    /**
     * {@inheritDoc}
     */
    public Attribute put(String key, Attribute value) {
        return attributes.put(key, value);
    }

    /**
     * {@inheritDoc}
     */
    public void putAll(Map<? extends String, ? extends Attribute> m) {
        attributes.putAll(m);
    }

    /**
     * {@inheritDoc}
     */
    public Attribute remove(Object key) {
        return attributes.remove((String) key);
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        return attributes.size();
    }

    /**
     * {@inheritDoc}
     */
    public Collection<Attribute> values() {
        return attributes.values();
    }

    /**
     * Getter for exclude set.
     * 
     * @return set containing all attributes of a JSF component that cannot/shouldn't be set on page.
     */
    private Set<String> getExcludeSet() {
        Set<String> set = new HashSet<String>();

        set.add("attributes");
        set.add("actionExpression");
        set.add("actionListeners");
        set.add("children");
        set.add("childCount");
        set.add("class");
        set.add("clientBehaviors");
        set.add("clientId");
        set.add("defaultEventName");
        set.add("eventNames");
        set.add("facetCount");
        set.add("facets");
        set.add("facetsAndChildren");
        set.add("family");
        set.add("hints");
        set.add("id");
        set.add("inView");
        set.add("namingContainer");
        set.add("parent");
        set.add("rendererType");
        set.add("rendersChildren");
        set.add("resourceBundleMap");
        set.add("stateHelper");
        set.add("transient");
        return set;
    }

    /**
     * Determines whether given object represents an EL expression, e.g. #{bean.property}.
     * 
     * @param value
     *            value of a property of tested JSF component
     * @return true if object is a string representing an expression, e.g. #{bean.property}, false otherwise
     */
    private boolean isStringEL(Object value) {
        if (!(value instanceof String)) {
            return false;
        }

        return ((String) value).matches("#\\{.*\\}");
    }

    /**
     * An action for tested JSF component. Can be modified dynamically.
     * 
     * @return outcome of an action or null if no navigation should be performed
     */
    public String action() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        MethodExpression method = null;

        if (attributes.get("action") == null) {
            return null;
        }

        String outcome = (String) attributes.get("action").getValue();

        if (outcome == null) {
            return null;
        }

        RichBean.logToPage("* action invoked");

        // if no select options for "action" are defined in property file and it is an EL expression
        if (!hasSelectOptions("action") && isStringEL(outcome)) {
            method = getExpressionFactory().createMethodExpression(elContext, outcome, String.class, new Class[0]);
            return (String) method.invoke(elContext, null);
        }

        // if select options for "action" are defined in property file
        if (hasSelectOptions("action")) {
            method =
                    getExpressionFactory().createMethodExpression(elContext, getMethodEL(outcome), String.class,
                    new Class[0]);

            return (String) method.invoke(elContext, null);
        }

        return outcome;

    }

    /**
     * An action listener for tested JSF component. Can be modified dynamically.
     * 
     * @param event
     *            event representing the activation of a user interface component
     */
    public void actionListener(ActionEvent event) {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        MethodExpression method = null;

        if (attributes.get("actionListener") == null) {
            return;
        }

        String listener = (String) attributes.get("actionListener").getValue();

        if (listener == null) {
            return;
        }

        RichBean.logToPage("* action listener invoked");

        // if no select options for "actionListener" are defined in property file and it is an EL expression
        if (!hasSelectOptions("actionListener") && isStringEL(listener)) {
            method =
                    getExpressionFactory().createMethodExpression(elContext, listener, void.class,
                    new Class[]{ActionEvent.class});
            method.invoke(elContext, new Object[]{event});
        }

        // if select options for "actionListener" are defined in property file
        if (hasSelectOptions("actionListener")) {
            method =
                    getExpressionFactory().createMethodExpression(elContext, getMethodEL(listener), void.class,
                    new Class[]{ActionEvent.class});
            method.invoke(elContext, new Object[]{event});
        }
    }

    /**
     * An action listener for tested JSF component. Can be modified dynamically.
     *
     * @param event
     *            event representing the activation of a user interface component
     */
    public void listener(AjaxBehaviorEvent event) {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        MethodExpression method = null;

        if (attributes.get("listener") == null) {
            return;
        }

        String listener = (String) attributes.get("listener").getValue();

        if (listener == null) {
            return;
        }

        RichBean.logToPage("* listener invoked");

        // if no select options for "listener" are defined in property file and it is an EL expression
        if (!hasSelectOptions("listener") && isStringEL(listener)) {
            method =
                    getExpressionFactory().createMethodExpression(elContext, listener, void.class,
                    new Class[]{AjaxBehaviorEvent.class});
            method.invoke(elContext, new Object[]{event});
        }

        // if select options for "listener" are defined in property file
        if (hasSelectOptions("listener")) {
            method =
                    getExpressionFactory().createMethodExpression(elContext, getMethodEL(listener), void.class,
                    new Class[]{AjaxBehaviorEvent.class});
            method.invoke(elContext, new Object[]{event});
        }
    }

    /**
     * Method used for creating EL expressions for methods.
     * 
     * @param methodName
     *            name of the action or action listener, e.g. toUpperCaseAction
     * @return string containing an expression for an action or action listener, e.g. #{bean.toUpperCaseAction}
     */
    private String getMethodEL(String methodName) {
        // get name of the managed bean
        String el = beanClass.getAnnotation(ManagedBean.class).name();

        if ("".equals(el)) {
            // create name of a managed bean according to standard, i.e. MyBean -> myBean
            el = beanClass.getSimpleName().substring(0, 1).toLowerCase() + beanClass.getSimpleName().substring(1);
        }

        StringBuilder methodEL = new StringBuilder("#{");
        methodEL.append(el);
        methodEL.append(".");
        methodEL.append(methodName);
        methodEL.append("}");

        return methodEL.toString();
    }

    /**
     * Decides if there are any select options for given attribute. If true, radio buttons should be rendered on a page.
     * 
     * @param attributeName
     *            name of a component attribute
     * @return true if select options were defined, false otherwise
     */
    public boolean hasSelectOptions(String attributeName) {
        List<SelectItem> options = attributes.get(attributeName).getSelectOptions();
        return options != null && options.size() != 0;
    }

    public boolean containsKey(Object key) {
        return attributes.containsKey((String) key);
    }

    public boolean containsValue(Object value) {
        return attributes.containsValue((Attribute) value);
    }

    public Attribute get(Object key) {
        return attributes.get((String) key);
    }

    public void setAttribute(String name, Object value) {
        Attribute attr = attributes.get(name);
        if (attr == null) {
            attr = new Attribute(name);
        }
        attr.setValue(value);
        attributes.put(name, attr);
    }

    /**
     * Obtains the ExpressionFactory instance from current context.
     * 
     * @return the ExpressionFactory instance from current context
     */
    private ExpressionFactory getExpressionFactory() {
        return FacesContext.getCurrentInstance().getApplication().getExpressionFactory();
    }
}
