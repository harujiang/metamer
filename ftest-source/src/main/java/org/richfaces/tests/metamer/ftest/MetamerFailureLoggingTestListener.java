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
package org.richfaces.tests.metamer.ftest;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.test.selenium.listener.FailureLoggingTestListener;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.ITestResult;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class MetamerFailureLoggingTestListener extends FailureLoggingTestListener {
    @Override
    protected String getFilenameIdentification(ITestResult result) {
        return MetamerTestInfo.getAssociatedFilename(result);
    }

    @Override
    protected String getSeleniumLogIdentification(ITestResult result) {
        String id = super.getSeleniumLogIdentification(result);
        return id + " " + MetamerTestInfo.getConfigurationInfoInParenthesses();
    }

    @Override
    protected void onFailure(ITestResult result) {
        super.onFailure(result);
        IssueTracking issueTracking = result.getMethod().getMethod().getAnnotation(IssueTracking.class);
        if (issueTracking != null && issueTracking.value().length > 0) {
            String issues = StringUtils.join(issueTracking.value(), "\n");
            String filenameIdentification = getFilenameIdentification(result);
            File issueTrackingOutputFile = new File(failuresOutputDir, filenameIdentification + "/issues.txt");
            try {
                FileUtils.writeStringToFile(issueTrackingOutputFile, issues);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
