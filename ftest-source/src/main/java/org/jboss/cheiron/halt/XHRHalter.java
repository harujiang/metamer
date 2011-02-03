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
package org.jboss.cheiron.halt;

import static org.jboss.cheiron.halt.XHRState.*;
import static org.jboss.test.selenium.encapsulated.JavaScript.js;

import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.framework.AjaxSeleniumProxy;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class XHRHalter {

    private static AjaxSelenium selenium = AjaxSeleniumProxy.getInstance();

    private static final AbstractPageExtensions HALTER_EXTENSIONS = new AbstractPageExtensions() {
        {
            loadFromResource("javascript/cheiron/XHRHalter.js");
        }

        public JavaScript isExtensionInstalledScript() {
            return js("selenium.browserbot.getCurrentWindow().XHRHalter != undefined");
        }
    };

    private static JavaScript isHandleAvailable = js("selenium.browserbot.getCurrentWindow().XHRHalter.isHandleAvailable()");
    private static JavaScript isWaitingForSend = js("selenium.browserbot.getCurrentWindow().XHRHalter.isWaitingForSend({0})");
    private static JavaScript getHandle = js("selenium.browserbot.getCurrentWindow().XHRHalter.getHandle()");
    private static JavaScript continueTo = js("selenium.browserbot.getCurrentWindow().XHRHalter.continueTo({0}, selenium.browserbot.getCurrentWindow().XHRHalter.STATE_{1})");
    private static JavaScript setEnabled = js("selenium.browserbot.getCurrentWindow().XHRHalter.setEnabled({0})");

    int handle;

    private XHRHalter(int handle) {
        this.handle = handle;
    }

    public static void enable() {
        selenium.getPageExtensions().install();
        HALTER_EXTENSIONS.install();
        selenium.getEval(setEnabled.parametrize(true));
    }

    public static void disable() {
        selenium.getEval(setEnabled.parametrize(false));
    }

    public static boolean isHandleAvailable() {
        return Boolean.valueOf(selenium.getEval(isHandleAvailable));
    }

    public static void waitForHandleAvailable() {
        selenium.waitForCondition(isHandleAvailable);
    }

    public static XHRHalter getHandle() {
        int handle = Integer.valueOf(selenium.getEval(getHandle));
        if (handle < 0) {
            throw new IllegalStateException("Handle is not available");
        }
        return new XHRHalter(handle);
    }

    public static XHRHalter getHandleBlocking() {
        waitForHandleAvailable();
        return getHandle();
    }

    public void continueBefore(XHRState phase) {
        Validate.notNull(phase);
        XHRState phaseToContinueAfter = XHRState.values()[phase.ordinal() - 1];
        continueAfter(phaseToContinueAfter);
    }

    public void continueAfter(XHRState phase) {
        Validate.notNull(phase);
        selenium.getEval(continueTo.parametrize(handle, phase.toString()));
    }

    public boolean isWaitingForSend() {
        return Boolean.valueOf(selenium.getEval(isWaitingForSend.parametrize(handle)));
    }

    public void waitForOpen() {
        selenium.waitForCondition(isWaitingForSend.parametrize(handle));
    }

    public void send() {
        continueAfter(SEND);
    }

    public void initialize() {
        continueAfter(UNITIALIZED);
    }

    public void loading() {
        continueAfter(LOADING);
    }

    public void loaded() {
        continueAfter(LOADED);
    }

    public void interactive() {
        continueAfter(INTERACTIVE);
    }

    public void complete() {
        continueAfter(COMPLETE);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + ": handle " + handle;
    }
}
