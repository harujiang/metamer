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
package org.richfaces.tests.metamer.ftest;

import static org.jboss.test.selenium.dom.Event.CLICK;
import static org.jboss.test.selenium.dom.Event.DBLCLICK;
import static org.jboss.test.selenium.dom.Event.MOUSEDOWN;
import static org.jboss.test.selenium.dom.Event.MOUSEMOVE;
import static org.jboss.test.selenium.dom.Event.MOUSEOUT;
import static org.jboss.test.selenium.dom.Event.MOUSEOVER;
import static org.jboss.test.selenium.dom.Event.MOUSEUP;
import static org.jboss.test.selenium.encapsulated.JavaScript.js;
import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardHttp;
import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardXhr;
import static org.jboss.test.selenium.locator.LocatorFactory.id;
import static org.jboss.test.selenium.locator.LocatorFactory.jq;
import static org.jboss.test.selenium.locator.reference.ReferencedLocator.ref;
import static org.jboss.test.selenium.utils.URLUtils.buildUrl;
import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.faces.event.PhaseId;

import org.apache.commons.lang.LocaleUtils;
import org.jboss.cheiron.retriever.ScriptEvaluationRetriever;
import org.jboss.test.selenium.AbstractTestCase;
import org.jboss.test.selenium.SystemProperties;
import org.jboss.test.selenium.browser.BrowserType;
import org.jboss.test.selenium.dom.Event;
import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.locator.Attribute;
import org.jboss.test.selenium.locator.AttributeLocator;
import org.jboss.test.selenium.locator.ElementLocator;
import org.jboss.test.selenium.locator.ExtendedLocator;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.locator.reference.LocatorReference;
import org.jboss.test.selenium.locator.reference.ReferencedLocator;
import org.jboss.test.selenium.waiting.EventFiredCondition;
import org.jboss.test.selenium.waiting.retrievers.Retriever;
import org.jboss.test.selenium.waiting.retrievers.TextRetriever;
import org.richfaces.tests.metamer.TemplatesList;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Abstract test case used as a basis for majority of test cases.
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision$
 */
public abstract class AbstractMetamerTest extends AbstractTestCase {
    
    /**
     * timeout in miliseconds
     */
    public static final long TIMEOUT = 5000;

    protected JQueryLocator time = jq("span[id$=requestTime]");
    protected JQueryLocator renderChecker = jq("span[id$=renderChecker]");
    protected JQueryLocator statusChecker = jq("span[id$=statusCheckerOutput]");
    protected TextRetriever retrieveRequestTime = retrieveText.locator(time);
    protected Retriever<String> retrieveWindowData = new ScriptEvaluationRetriever().script(js("window.data"));
    protected TextRetriever retrieveRenderChecker = retrieveText.locator(jq("#renderChecker"));
    protected TextRetriever retrieveStatusChecker = retrieveText.locator(jq("#statusCheckerOutput"));
    protected PhaseInfo phaseInfo = new PhaseInfo();
    protected LocatorReference<JQueryLocator> attributesRoot = new LocatorReference<JQueryLocator>(
            pjq("span[id$=:attributes:panel]"));
    
    @Inject
    @Templates({ "plain", "richAccordion", "richDataTable", "richCollapsibleSubTable", "richExtendedDataTable",
        "richDataGrid", "richList", "richCollapsiblePanel", "richPanel", "richTabPanel", "richTogglePanel",
        "richPopupPanel", "a4jRegion", "a4jRepeat", "hDataTable", "hPanelGrid", "uiRepeat" })
    private TemplatesList template;

    /**
     * Returns the url to test page to be opened by Selenium
     * 
     * @return absolute url to the test page to be opened by Selenium
     */
    public abstract URL getTestUrl();

    /**
     * Opens the tested page. If templates is not empty nor null, it appends url parameter with templates.
     * 
     * @param templates
     *            templates that will be used for test, e.g. "red_div"
     */
    @BeforeMethod(alwaysRun = true)
    public void loadPage(Object[] templates) {
        if (selenium == null) {
            throw new SkipException("selenium isn't initialized");
        }
        selenium.open(buildUrl(getTestUrl() + "?templates=" + template.toString()));
        selenium.waitForPageToLoad(TIMEOUT);
    }

    /**
     * Invalidates session by clicking on a button on tested page.
     */
    @AfterMethod(alwaysRun = true)
    public void invalidateSession() {
        selenium.deleteAllVisibleCookies();
    }

    /**
     * Forces the current thread sleep for given time.
     * 
     * @param millis
     *            number of miliseconds for which the thread will sleep
     */
    @Deprecated
    protected void waitFor(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Do a full page refresh (regular HTTP request) by triggering a command with no action bound.
     */
    public void fullPageRefresh() {
        guardHttp(selenium).click(id("controlsForm:fullPageRefreshImage"));
    }

    /**
     * Rerender all content of the page (AJAX request) by trigerring a command with no action but render bound.
     */
    public void rerenderAll() {
        guardXhr(selenium).click(id("controlsForm:reRenderAllImage"));
    }

    /**
     * Factory method for creating instances of class JQueryLocator which locates the element using <a
     * href="http://api.jquery.com/category/selectors/">JQuery Selector</a> syntax. It adds "div.content " in front of
     * each selector.
     * 
     * @param jquerySelector
     *            the jquery selector
     * @return the j query locator
     * @see JQueryLocator
     */
    public static JQueryLocator pjq(String jquerySelector) {
        return new JQueryLocator("div.content " + jquerySelector);
    }

    /**
     * A helper method for testing javascripts events. It sets alert('testedevent') to the input field for given event
     * and fires the event. Then it checks the message in the alert dialog.
     * 
     * @param event
     *            JavaScript event to be tested
     * @param element
     *            locator of tested element
     */
    protected void testFireEvent(Event event, ElementLocator<?> element) {
        testFireEvent(event, element, event.getEventName());
    }

    /**
     * A helper method for testing javascripts events. It sets alert('testedevent') to the input field for given event
     * and fires the event. Then it checks the message in the alert dialog.
     * 
     * @param event
     *            JavaScript event to be tested
     * @param element
     *            locator of tested element
     * @param attributeName
     *            name of the attribute that should be set
     */
    protected void testFireEvent(Event event, ElementLocator<?> element, String attributeName) {
        ElementLocator<?> eventInput = pjq("input[id$=on" + attributeName + "Input]");
        String value = "metamerEvents += \"" + event.getEventName() + " \"";

        guardHttp(selenium).type(eventInput, value);

        selenium.fireEvent(element, event);

        waitGui.failWith("Attribute on" + attributeName + " does not work correctly").until(
                new EventFiredCondition(event));
    }

    /**
     * Returns the locale of the tested page
     * 
     * @return the locale of the tested page
     */
    public Locale getLocale() {
        String localeString = selenium.getText(id("locale"));
        return LocaleUtils.toLocale(localeString);
    }

    /**
     * A helper method for testing attribute "style". It sets "background-color: yellow; font-size: 1.5em;" to the input
     * field and checks that it was changed on the page.
     * 
     * @param element
     *            locator of tested element
     * @param attribute
     *            name of the attribute that will be set (e.g. style, headerStyle, itemContentStyle)
     */
    protected void testStyle(ElementLocator<?> element, String attribute) {
        ElementLocator<?> styleInput = ref(attributesRoot, "input[id$=" + attribute + "Input]");
        final String value = "background-color: yellow; font-size: 1.5em;";

        selenium.type(styleInput, value);
        selenium.waitForPageToLoad();

        AttributeLocator<?> styleAttr = element.getAttribute(Attribute.STYLE);
        assertTrue(selenium.getAttribute(styleAttr).contains(value), "Attribute style should contain \"" + value + "\"");
    }

    /**
     * A helper method for testing attribute "class". It sets "metamer-ftest-class" to the input field and checks that
     * it was changed on the page.
     * 
     * @param element
     *            locator of tested element
     * @param attribute
     *            name of the attribute that will be set (e.g. styleClass, headerClass, itemContentClass)
     */
    protected void testStyleClass(ExtendedLocator<JQueryLocator> element, String attribute) {
        final String styleClass = "metamer-ftest-class";
        selenium.type(ref(attributesRoot, "input[id$=" + attribute + "Input]"), styleClass);
        selenium.waitForPageToLoad();

        JQueryLocator elementWhichHasntThatClass = jq(element.getRawLocator() + ":not(.{0})").format(styleClass);
        assertTrue(selenium.isElementPresent(element));
        assertFalse(selenium.isElementPresent(elementWhichHasntThatClass));
    }

    public void testRequestEventsBefore(String... events) {
        for (String event : events) {
            ReferencedLocator<JQueryLocator> input = ref(attributesRoot, "input[type=text][id$=on{0}Input]");
            input = input.format(event);
            selenium.type(input, format("metamerEvents += \"{0} \"", event));
            selenium.waitForPageToLoad();
        }

        selenium.getEval(new JavaScript("window.metamerEvents = \"\";"));
    }

    public void testRequestEventsBeforeByAlert(String... events) {
        for (String event : events) {
            ReferencedLocator<JQueryLocator> input = ref(attributesRoot, "input[type=text][id$=on{0}Input]");
            input = input.format(event);
            selenium.type(input, format("alert('{0}')", event));
            selenium.waitForPageToLoad();
        }
    }

    public void testRequestEventsAfter(String... events) {
        String[] actualEvents = selenium.getEval(new JavaScript("window.metamerEvents")).split(" ");
        assertEquals(
                actualEvents,
                events,
                format("The events ({0}) don't came in right order ({1})", Arrays.deepToString(actualEvents),
                Arrays.deepToString(events)));
    }

    public void testRequestEventsAfterByAlert(String... events) {
        List<String> list = new LinkedList<String>();

        for (int i = 0; i < events.length; i++) {
            waitGui.dontFail().until(alertPresent);
            if (selenium.isAlertPresent()) {
                list.add(selenium.getAlert());
            }
        }

        String[] actualEvents = list.toArray(new String[list.size()]);
        assertEquals(
                actualEvents,
                events,
                format("The events ({0}) don't came in right order ({1})", Arrays.deepToString(actualEvents),
                Arrays.deepToString(events)));
    }

    /**
     * A helper method for testing attribute "dir". It tries null, ltr and rtl.
     * 
     * @param element
     *            locator of tested element
     */
    protected void testDir(ElementLocator<?> element) {
        ElementLocator<?> ltrInput = ref(attributesRoot, "input[type=radio][name$=dirInput][value=ltr]");
        ElementLocator<?> rtlInput = ref(attributesRoot, "input[type=radio][name$=dirInput][value=rtl]");

        AttributeLocator<?> dirAttribute = element.getAttribute(new Attribute("dir"));

        // dir = null
        assertFalse(selenium.isAttributePresent(dirAttribute), "Attribute dir should not be present.");

        // dir = ltr
        selenium.click(ltrInput);
        selenium.waitForPageToLoad();
        assertTrue(selenium.isAttributePresent(dirAttribute), "Attribute dir should be present.");
        String value = selenium.getAttribute(dirAttribute);
        assertEquals(value.toLowerCase(), "ltr", "Attribute dir");

        // dir = rtl
        selenium.click(rtlInput);
        selenium.waitForPageToLoad();
        assertTrue(selenium.isAttributePresent(dirAttribute), "Attribute dir should be present.");
        value = selenium.getAttribute(dirAttribute);
        assertEquals(value.toLowerCase(), "rtl", "Attribute dir");
    }

    /**
     * A helper method for testing attribute "lang".
     * 
     * @param element
     *            locator of tested element
     */
    protected void testLang(ElementLocator<?> element) {
        JavaScript getAttributeLang = null;
        if (SystemProperties.getBrowser().getType() == BrowserType.FIREFOX) {
            getAttributeLang = new JavaScript("window.jQuery('" + element.getRawLocator() + "').attr('lang')");
        } else {
            getAttributeLang = new JavaScript("window.jQuery('" + element.getRawLocator() + "').attr('xml:lang')");
        }

        // lang = null
        String langAttr = selenium.getEval(getAttributeLang);
        assertTrue("null".equals(langAttr) || "".equals(langAttr), "Attribute xml:lang should not be present.");

        selenium.type(pjq("input[type=text][id$=langInput]"), "sk");
        selenium.waitForPageToLoad();

        // lang = sk
        assertEquals(selenium.getEval(getAttributeLang), "sk", "Attribute xml:lang should be present.");
    }

    /**
     * A helper method for testing attribute "title".
     * 
     * @param element
     *            locator of tested element
     */
    protected void testTitle(ElementLocator<?> element) {
        ElementLocator<?> input = ref(attributesRoot, "input[type=text][id$=titleInput]");
        AttributeLocator<?> attribute = element.getAttribute(new Attribute("title"));

        // title = null
        assertFalse(selenium.isAttributePresent(attribute), "Attribute title should not be present.");

        // title = "RichFaces 4"
        selenium.type(input, "RichFaces 4");
        selenium.waitForPageToLoad(TIMEOUT);

        assertTrue(selenium.isAttributePresent(attribute), "Attribute title should be present.");
        String value = selenium.getAttribute(attribute);
        assertEquals(value, "RichFaces 4", "Attribute title");
    }

    /**
     * A helper method for testing standard HTML attributes (RichFaces attributes that are directly put into markup),
     * e.g. hreflang.
     * 
     * @param element
     *            locator of tested element
     * @param attribute
     *            tested attribute, e.g. "hreflang"
     * @param value
     *            value that should be set, e.g. "cs"
     */
    protected void testHtmlAttribute(ElementLocator<?> element, String attribute, String value) {
        AttributeLocator<?> attr = element.getAttribute(new Attribute(attribute));

        selenium.type(pjq("input[id$=" + attribute + "Input]"), value);
        selenium.waitForPageToLoad();

        assertTrue(selenium.getAttribute(attr).contains(value), "Attribute " + attribute + " should contain \"" + value
                + "\".");
    }

    /**
     * Hides header, footer and inputs for attributes.
     */
    protected void hideControls() {
        selenium.getEval(new JavaScript("window.hideControls()"));
    }

    /**
     * Shows header, footer and inputs for attributes.
     */
    protected void showControls() {
        selenium.getEval(new JavaScript("window.showControls()"));
    }

    /**
     * Verifies that only given phases were executed. It uses the list of phases in the header of the page.
     * 
     * @param phases
     *            phases that are expected to have been executed
     */
    @Deprecated
    protected void assertPhases(PhaseId... phases) {
        phaseInfo.assertPhases(phases);
    }

    protected void fireEventNatively(ElementLocator<?> target, Event event) {
        if (event == CLICK) {
            selenium.click(target);
        } else if (event == DBLCLICK) {
            selenium.doubleClick(target);
        } else if (event == MOUSEMOVE) {
            selenium.mouseMove(target);
        } else if (event == MOUSEDOWN) {
            selenium.mouseDown(target);
        } else if (event == MOUSEUP) {
            selenium.mouseUp(target);
        } else if (event == MOUSEOVER) {
            selenium.mouseOver(target);
        } else if (event == MOUSEOUT) {
            selenium.mouseOut(target);
        } else {
            selenium.fireEvent(target, event);
        }
    }

    /**
     * Abstract ReloadTester for testing
     * 
     * @param <T>
     *            the type of input values which will be set, sent and then verified
     */
    public abstract class ReloadTester<T> {

        public abstract void doRequest(T inputValue);

        public abstract void verifyResponse(T inputValue);

        public abstract T[] getInputValues();

        public void testRerenderAll() {
            for (T inputValue : getInputValues()) {
                doRequest(inputValue);
                verifyResponse(inputValue);
                AbstractMetamerTest.this.rerenderAll();
                verifyResponse(inputValue);
            }
        }

        public void testFullPageRefresh() {
            for (T inputValue : getInputValues()) {
                doRequest(inputValue);
                verifyResponse(inputValue);
                AbstractMetamerTest.this.fullPageRefresh();
                verifyResponse(inputValue);
            }
        }
    }
}
