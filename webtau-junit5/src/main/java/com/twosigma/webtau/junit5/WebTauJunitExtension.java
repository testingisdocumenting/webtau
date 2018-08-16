/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twosigma.webtau.junit5;

import com.twosigma.webtau.junit5.report.JavaBasedTest;
import com.twosigma.webtau.junit5.report.JavaReport;
import com.twosigma.webtau.report.ReportGenerators;
import com.twosigma.webtau.report.ReportTestEntry;
import com.twosigma.webtau.reporter.StepReporters;
import com.twosigma.webtau.reporter.TestResultPayloadExtractors;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public class WebTauJunitExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        TestExecutionExceptionHandler {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create("webtau");
    private static final String TEST_KEY = "test";

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        JavaBasedTest test = new JavaBasedTest(
                extensionContext.getUniqueId(),
                fullTestName(extensionContext));

        StepReporters.add(test);
        test.getReportTestEntry().startClock();

        extensionContext.getStore(NAMESPACE).put(TEST_KEY, test);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        JavaBasedTest test = retrieveTest(extensionContext);

        StepReporters.remove(test);

        ReportTestEntry reportTestEntry = test.getReportTestEntry();
        reportTestEntry.setRan(true);
        reportTestEntry.stopClock();
        JavaReport.addTestEntry(reportTestEntry);

        TestResultPayloadExtractors.extract(reportTestEntry.getSteps().stream())
                .forEach(reportTestEntry::addTestResultPayload);

        ShutdownHook.INSTANCE.noOp();
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        JavaBasedTest test = retrieveTest(context);

        ReportTestEntry reportTestEntry = test.getReportTestEntry();
        reportTestEntry.setException(throwable);
        reportTestEntry.stopClock();

        throw throwable;
    }

    private JavaBasedTest retrieveTest(ExtensionContext extensionContext) {
        return extensionContext.getStore(NAMESPACE).get(TEST_KEY, JavaBasedTest.class);
    }

    private String fullTestName(ExtensionContext extensionContext) {
        return extensionContext.getParent()
                .map(ExtensionContext::getDisplayName).orElse("") + " " + extensionContext.getDisplayName();
    }

    private static class ShutdownHook {
        private final static ShutdownHook INSTANCE = new ShutdownHook();

        private ShutdownHook() {
            Runtime.getRuntime().addShutdownHook(new Thread(() ->
                    ReportGenerators.generate(JavaReport.get())));
        }

        void noOp() {
        }
    }
}
