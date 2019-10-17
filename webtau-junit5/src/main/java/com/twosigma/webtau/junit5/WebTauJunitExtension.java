/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

import com.twosigma.webtau.javarunner.report.JavaBasedTest;
import com.twosigma.webtau.javarunner.report.JavaReport;
import com.twosigma.webtau.javarunner.report.JavaReportShutdownHook;
import com.twosigma.webtau.report.ReportTestEntry;
import com.twosigma.webtau.reporter.ConsoleStepReporter;
import com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder;
import com.twosigma.webtau.reporter.StepReporters;
import com.twosigma.webtau.reporter.TestResultPayloadExtractors;
import org.junit.jupiter.api.extension.*;

/**
 * JUnit 5 extension to enable html report generation
 * Junit 5 relevant github issues: https://github.com/junit-team/junit5/issues/1769
 */
public class WebTauJunitExtension implements
        BeforeAllCallback,
        AfterAllCallback,
        BeforeEachCallback,
        AfterEachCallback,
        TestExecutionExceptionHandler {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create("webtau");
    private static final String TEST_KEY = "test";
    private static final String BEFORE_ALL_ID = "beforeAll";

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        ConsoleReporterRegistrator.register();

        JavaBasedTest test = new JavaBasedTest(
                BEFORE_ALL_ID,
                "init");

        startTest(extensionContext, test);
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        stopAndRemoveBeforeAllIfRequired(extensionContext);

        JavaBasedTest test = new JavaBasedTest(
                extensionContext.getUniqueId(),
                fullTestName(extensionContext));

        startTest(extensionContext, test);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        JavaBasedTest test = retrieveTest(extensionContext);
        stopTest(extensionContext, test);
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        JavaBasedTest test = retrieveTest(context);

        ReportTestEntry reportTestEntry = test.getReportTestEntry();
        reportTestEntry.setException(throwable);
        reportTestEntry.stopClock();

        throw throwable;
    }

    private void startTest(ExtensionContext extensionContext, JavaBasedTest test) {
        StepReporters.add(test);
        test.getReportTestEntry().startClock();

        storeTestInContext(extensionContext, test);
    }

    private void stopTest(ExtensionContext extensionContext, JavaBasedTest test) {
        removeTestFromContext(extensionContext);

        StepReporters.remove(test);

        ReportTestEntry reportTestEntry = test.getReportTestEntry();
        reportTestEntry.setRan(true);
        reportTestEntry.stopClock();
        reportTestEntry.setClassName(extensionContext.getTestClass()
                .map(Class::getCanonicalName)
                .orElse(null));
        JavaReport.addTestEntry(reportTestEntry);

        TestResultPayloadExtractors.extract(reportTestEntry.getSteps().stream())
                .forEach(reportTestEntry::addTestResultPayload);

        JavaReportShutdownHook.INSTANCE.noOp();
    }

    // there is no official callback for @BeforeAll, so we deduce code ran before any of the actual tests
    // we create a fake test called "init" and listen for all the test steps that happen
    // if there are no actual steps were created before first test then we remove the fake test from report
    private void stopAndRemoveBeforeAllIfRequired(ExtensionContext extensionContext) {
        if (! extensionContext.getParent().isPresent()) {
            return;
        }

        ExtensionContext parentContext = extensionContext.getParent().get();
        JavaBasedTest test = retrieveTest(parentContext);
        if (test == null) {
            return;
        }

        if (test.getReportTestEntry().getSteps().isEmpty()) {
            StepReporters.remove(test);
            return;
        }

        stopTest(parentContext, test);
    }

    private void storeTestInContext(ExtensionContext extensionContext, JavaBasedTest test) {
        extensionContext.getStore(NAMESPACE).put(TEST_KEY, test);
    }

    private void removeTestFromContext(ExtensionContext extensionContext) {
        extensionContext.getStore(NAMESPACE).remove(TEST_KEY, JavaBasedTest.class);
    }

    private JavaBasedTest retrieveTest(ExtensionContext extensionContext) {
        return extensionContext.getStore(NAMESPACE).get(TEST_KEY, JavaBasedTest.class);
    }

    private String fullTestName(ExtensionContext extensionContext) {
        return extensionContext.getParent()
                .map(ExtensionContext::getDisplayName).orElse("") + " " + extensionContext.getDisplayName();
    }

    // add ConsoleStepReporter only once if the WebTau extension was used
    private static class ConsoleReporterRegistrator {
        static {
            actualRegister();
        }

        static void register() {
            // no-op to trigger class load
        }

        private static void actualRegister() {
            StepReporters.add(new ConsoleStepReporter(IntegrationTestsMessageBuilder.getConverter()));
        }
    }
}
