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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.reflect.Method;

/**
 * JUnit 5 extension to enable html report generation
 */
public class WebTauJunitExtension implements
        BeforeAllCallback,
        AfterAllCallback,
        BeforeEachCallback,
        AfterEachCallback,
        TestExecutionExceptionHandler,
        InvocationInterceptor
{

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create("webtau");
    private static final String TEST_KEY = "test";
    private static final String BEFORE_ALL_ID = "beforeAll";
    private static final String AFTER_ALL_ID = "afterAll";

    @Override
    public void interceptBeforeAllMethod(Invocation<Void> invocation,
                                         ReflectiveInvocationContext<Method> invocationContext,
                                         ExtensionContext extensionContext) throws Throwable {
        invokeAsTest(invocation, invocationContext, extensionContext, BEFORE_ALL_ID);
    }

    @Override
    public void interceptAfterAllMethod(Invocation<Void> invocation,
                                        ReflectiveInvocationContext<Method> invocationContext,
                                        ExtensionContext extensionContext) throws Throwable {
        invokeAsTest(invocation, invocationContext, extensionContext, AFTER_ALL_ID);
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        ConsoleReporterRegistrator.register();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        JavaBasedTest test = new JavaBasedTest(
                extensionContext.getUniqueId(),
                testNameFromExtensionContext(extensionContext));

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

    private void invokeAsTest(Invocation<Void> invocation,
                              ReflectiveInvocationContext<Method> invocationContext,
                              ExtensionContext extensionContext,
                              String testNamePrefix) throws Throwable {
        String testMethodName = testNameFromInvocationContext(invocationContext);

        JavaBasedTest test = new JavaBasedTest(
                testNamePrefix + testMethodName,
                testMethodName);

        startTest(extensionContext, test);

        try {
            invocation.proceed();
        } finally {
            stopTest(extensionContext, test);
            JavaReportShutdownHook.INSTANCE.noOp();
        }
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

    private String testNameFromInvocationContext(ReflectiveInvocationContext<Method> invocationContext) {
        Method method = invocationContext.getExecutable();
        return AnnotationSupport.findAnnotation(method, DisplayName.class)
                .map(DisplayName::value)
                .orElseGet(method::getName);
    }

    private String testNameFromExtensionContext(ExtensionContext extensionContext) {
        String displayName = extensionContext.getDisplayName();
        return displayName.endsWith("()") ?
                displayName.substring(0, displayName.length() - 2):
                displayName;
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
