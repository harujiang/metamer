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

package org.richfaces.tests.metamer.ftest.model;

import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.guardXhr;
import static org.jboss.test.selenium.locator.reference.ReferencedLocator.ref;
import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.framework.AjaxSeleniumProxy;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.locator.reference.ReferencedLocator;

/**
 * Provides DataScroller control methods.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class DataScroller extends AbstractModel<JQueryLocator> {

    protected static final String CLASS_DISABLED = "rf-ds-dis";

    protected AjaxSelenium selenium = AjaxSeleniumProxy.getInstance();

    ReferencedLocator<JQueryLocator> numberedPages = ref(root, "> .rf-ds-nmb-btn");
    ReferencedLocator<JQueryLocator> specificNumberedPage = ref(root, "> .rf-ds-nmb-btn:textEquals('{0}')");

    ReferencedLocator<JQueryLocator> firstPageButton = ref(root, "> .rf-ds-lft[id$=ds_f]");
    ReferencedLocator<JQueryLocator> fastRewindButton = ref(root, "> .rf-ds-lft[id$=ds_fr]");
    ReferencedLocator<JQueryLocator> fastForwardButton = ref(root, "> .rf-ds-rgh[id$=ds_ff]");
    ReferencedLocator<JQueryLocator> lastPageButton = ref(root, "> .rf-ds-rgh[id$=ds_l]");

    ReferencedLocator<JQueryLocator> firstVisiblePage = ref(root, "> .rf-ds-nmb-btn:first");
    ReferencedLocator<JQueryLocator> lastVisiblePage = ref(root, "> .rf-ds-nmb-btn:last");
    ReferencedLocator<JQueryLocator> currentPage = ref(root, "> .rf-ds-act");

    Integer fastStep = null;
    Integer lastPage = null;

    public DataScroller(JQueryLocator root) {
        super(root);
    }

    public DataScroller(String name, JQueryLocator root) {
        super(name, root);
    }

    public void setFastStep(int fastStep) {
        this.fastStep = fastStep;
    }

    public Integer getFastStep() {
        return fastStep;
    }

    public void setLastPage(int pageNumber) {
        this.lastPage = pageNumber;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public void gotoFirstPage() {
        if (!isFirstPage()) {
            clickFirstPageButton();
        }
    }

    public void gotoLastPage() {
        if (!isLastPage()) {
            clickLastPageButton();
        }
    }

    public void gotoPage(int pageNumber) {
        if (lastPage != null && (pageNumber < 1 || pageNumber > lastPage)) {
            throw new IllegalStateException(format("The given pageNumber '{0}' is out of range of pages <1,{1}>", pageNumber, lastPage));
        }
        while (pageNumber > getLastVisiblePage()) {
            fastForward(pageNumber);
        }

        while (pageNumber < getFirstVisiblePage()) {
            fastRewind(pageNumber);
        }
        
        if (pageNumber == getCurrentPage()) {
            return;
        }

        clickPageButton(pageNumber);
    }

    public void fastForward(Integer pageNumber) {
        if (selenium.belongsClass(fastForwardButton, CLASS_DISABLED)) {
            if (fastStep != null && lastPage != null) {
                if (getCurrentPage() + fastStep > lastPage) {
                    gotoPage(getLastVisiblePage());
                } else {
                    throw new AssertionError("fast forward button disabled");
                }
            } else {
                gotoPage(getLastVisiblePage());
            }
        } else {
            if (pageNumber != null && lastPage != null) {
                if (Math.abs(getLastVisiblePage() - pageNumber) > Math.abs(lastPage - pageNumber)) {
                    clickLastPageButton();
                    return;
                }
            }
            if (fastStep == null) {
                gotoPage(getLastVisiblePage());
                return;
            }
            clickFastForward();
        }
    }

    public void fastRewind(Integer pageNumber) {
        if (selenium.belongsClass(fastRewindButton, CLASS_DISABLED)) {
            if (fastStep != null) {
                if (getCurrentPage() - fastStep <= 0) {
                    gotoPage(getFirstVisiblePage());
                } else {
                    throw new AssertionError("fast forward button disabled");
                }
            } else {
                gotoPage(getFirstVisiblePage());
            }
        } else {
            if (pageNumber != null) {
                if (Math.abs(getFirstVisiblePage() - pageNumber) > pageNumber) {
                    clickFirstPageButton();
                    return;
                }
            }
            if (fastStep == null) {
                gotoPage(getFirstVisiblePage());
                return;
            }
            clickFastRewind();

        }
    }

    public int getCountOfVisiblePages() {
        return selenium.getCount(numberedPages);
    }

    public boolean hasPages() {
        return selenium.isElementPresent(lastVisiblePage);
    }

    public int getFirstVisiblePage() {
        if (!hasPages()) {
            return 1;
        }
        return integer(selenium.getText(firstVisiblePage));
    }

    public int getLastVisiblePage() {
        if (!hasPages()) {
            return 1;
        }
        return integer(selenium.getText(lastVisiblePage));
    }

    public int obtainLastPage() {
        if (!hasPages()) {
            return 1;
        }
        int startPage = getCurrentPage();
        clickLastPageButton();
        int lastPage = getCurrentPage();
        if (startPage == 1) {
            clickFirstPageButton();
        } else {
            gotoPage(startPage);
        }
        return lastPage;
    }

    public int getCurrentPage() {
        if (!hasPages()) {
            return 1;
        }
        return integer(selenium.getText(currentPage));
    }

    public boolean isFirstPage() {
        return getCurrentPage() == 1;
    }

    public boolean isLastPage() {
        return getCurrentPage() == getLastVisiblePage();
    }

    public static int integer(String string) {
        return Integer.valueOf(string);
    }

    public void clickLastPageButton() {
        guardXhr(selenium).click(lastPageButton);
    }

    public void clickFirstPageButton() {
        guardXhr(selenium).click(firstPageButton);
    }

    public void clickPageButton(int pageNumber) {
        guardXhr(selenium).click(specificNumberedPage.format(pageNumber));
    }

    public void clickFastForward() {
        guardXhr(selenium).click(fastForwardButton);
    }

    public void clickFastRewind() {
        guardXhr(selenium).click(fastRewindButton);
    }
}
