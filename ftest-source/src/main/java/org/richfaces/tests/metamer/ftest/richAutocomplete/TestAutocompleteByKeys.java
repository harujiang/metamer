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
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import static org.jboss.test.selenium.RequestTypeModelGuard.guardXhr;
import static org.jboss.test.selenium.RequestTypeModelGuard.guardNoRequest;
import static org.jboss.test.selenium.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.richfaces.tests.metamer.bean.Model;
import org.richfaces.tests.metamer.ftest.AbstractMetamerTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.model.Autocomplete;
import org.richfaces.tests.metamer.model.Capital;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
@IssueTracking("https://issues.jboss.org/browse/RF-10254")
public class TestAutocompleteByKeys extends AbstractMetamerTest {

    AutocompleteAttributes attributes = new AutocompleteAttributes();
    Autocomplete autocomplete = new Autocomplete();
    
    @Inject
    @Use(booleans = { true, false })
    Boolean autofill;

    @Inject
    @Use(booleans = { true, false })
    Boolean selectFirst;

    List<Capital> capitals = Model.unmarshallCapitals();

    StringBuilder partialInput;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/autocomplete.xhtml");
    }

    @BeforeMethod
    public void prepareProperties() {
        attributes.setAutofill(autofill);
        attributes.setSelectFirst(selectFirst);
        if (autofill == null) {
            autofill = false;
        }
        if (selectFirst == null) {
            selectFirst = false;
        }
    }

    @Test
    public void testTypingPrefixAndThenConfirm() {
        assertCompletionVisible(false);
        typePrefix("ala");
        assertCompletionVisible(true);
        confirm();
        assertCompletionVisible(false);
        assertTrue(autocomplete.getInputText().toLowerCase().startsWith(getExpectedStateForPrefix().toLowerCase()));
    }

    @Test
    public void testTypingPrefixAndThenDeleteAll() {
        assertCompletionVisible(false);
        typePrefix("ala");
        assertCompletionVisible(true);
        deleteAll();
        assertCompletionVisible(false);
        typePrefix("ala");
        assertCompletionVisible(true);
    }

    private void assertCompletionVisible(boolean assertCompletionVisible) {
        assertEquals(autocomplete.isCompletionVisible(), assertCompletionVisible);
    }

    public void confirm() {
        autocomplete.confirmByKeys();
        autocomplete.waitForCompletionVisible();
    }

    public void deleteAll() {
        partialInput = new StringBuilder();

        autocomplete.textSelectAll();
        guardNoRequest(autocomplete).pressBackspace();

        assertEquals(autocomplete.getInputText(), getExpectedStateForPrefix());
        assertEquals(autocomplete.getSelectedOptionIndex(), getExpectedSelectedOptionIndex());
    }

    public void typePrefix(String wholeInput) {
        partialInput = new StringBuilder(autocomplete.getInputText());

        for (int i = 0; i < wholeInput.length(); i++) {
            String chr = String.valueOf(wholeInput.charAt(i));

            guardXhr(autocomplete).typeKeys(chr);
            partialInput.append(chr);

            assertEquals(autocomplete.getInputText(), getExpectedStateForPrefix());
            assertEquals(autocomplete.getSelectedOptionIndex(), getExpectedSelectedOptionIndex());
        }
    }

    public String getExpectedStateForPrefix() {
        if (selectFirst && autofill && partialInput.length() > 0) {
            return getStatesByPrefix(partialInput.toString()).get(0).toLowerCase();
        }

        return partialInput.toString();
    }

    public int getExpectedSelectedOptionIndex() {
        return (selectFirst && partialInput.length() > 0) ? 0 : -1;
    }

    public List<String> getStatesByPrefix(String prefix) {
        List<String> states = new LinkedList<String>();

        for (Capital cap : capitals) {
            if (cap.getState().toLowerCase().startsWith(prefix)) {
                states.add(cap.getState());
            }
        }

        return states;
    }
}
