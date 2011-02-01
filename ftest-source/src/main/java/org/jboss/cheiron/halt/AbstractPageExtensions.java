/*
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
 */
package org.jboss.cheiron.halt;

import java.util.List;

import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.framework.AjaxSeleniumProxy;
import org.jboss.test.selenium.waiting.Wait;

import static org.jboss.test.selenium.encapsulated.JavaScript.*;

/**
 * Defines methods for installing JavaScript page extension to the target page.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public abstract class AbstractPageExtensions {

    /** The JavaScript for extending of the page. */
    JavaScript pageExtensions;

    /** Evaluates if the body is loaded */
    final JavaScript isBodyLoaded = js("(selenium.browserbot.getCurrentWindow() != null) "
        + " && (selenium.browserbot.getCurrentWindow().document != null) "
        + " && (selenium.browserbot.getCurrentWindow().document.body != null)");

    /** The associated selenium object. */
    AjaxSelenium selenium = AjaxSeleniumProxy.getInstance();

    public abstract JavaScript isExtensionInstalledScript();

    /**
     * Install.
     */
    public void install() {
        if (!isInstalled()) {
            waitForBodyLoaded();
            installPageExtension();
        }
    }

    /**
     * Checks if RichFacesSelenium is already installed
     * 
     * @return true, if is installed
     */
    public boolean isInstalled() {
        return Boolean.valueOf(selenium.getEval(isExtensionInstalledScript()));
    }

    /**
     * Install the page extensions
     */
    void installPageExtension() {
        selenium.runScript(pageExtensions);
    }

    /**
     * Wait for body to be loaded.
     */
    void waitForBodyLoaded() {
        selenium.waitForCondition(isBodyLoaded, Wait.DEFAULT_TIMEOUT);
    }

    public void loadFromResource(String resourceName) {
        this.pageExtensions = fromResource(resourceName);
    }

    /**
     * Loads the page JS extensions from resources defined by list of resource names.
     * 
     * @param resourceNames
     *            the list of full paths to resources
     */
    public void loadFromResources(List<String> resourceNames) {
        JavaScript extensions = null;
        for (String resourceName : resourceNames) {
            JavaScript partial = fromResource(resourceName);
            extensions = (extensions == null) ? partial : extensions.join(partial);
        }
        this.pageExtensions = extensions;
    }
}
