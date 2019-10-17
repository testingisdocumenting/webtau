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

package com.twosigma.webtau.junit4;

import com.twosigma.webtau.javarunner.report.JavaBasedTest;
import com.twosigma.webtau.javarunner.report.JavaReport;
import com.twosigma.webtau.javarunner.report.JavaReportShutdownHook;
import com.twosigma.webtau.report.ReportTestEntry;
import com.twosigma.webtau.reporter.StepReporters;
import com.twosigma.webtau.reporter.TestResultPayloadExtractors;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.util.concurrent.atomic.AtomicInteger;

public class WebTauRunner extends BlockJUnit4ClassRunner {
    private static final AtomicInteger idGenerator = new AtomicInteger();

    public WebTauRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        JavaBasedTest javaBasedTest = new JavaBasedTest(
                method.getName() + idGenerator.incrementAndGet(),
                method.getName());

        ReportTestEntry reportTestEntry = javaBasedTest.getReportTestEntry();
        reportTestEntry.setClassName(method.getDeclaringClass().getCanonicalName());

        notifier.addListener(new RunListener() {
            @Override
            public void testFailure(Failure failure) {
                reportTestEntry.setExceptionIfNotSet(failure.getException());
            }
        });

        beforeTestRun(javaBasedTest);
        try {
            super.runChild(method, notifier);
        } catch (Throwable e) {
            reportTestEntry.setExceptionIfNotSet(e);
            throw e;
        } finally {
            afterTestRun(javaBasedTest);
        }
    }

    private void beforeTestRun(JavaBasedTest javaBasedTest) {
        javaBasedTest.getReportTestEntry().startClock();
        StepReporters.add(javaBasedTest);
    }

    private void afterTestRun(JavaBasedTest javaBasedTest) {
        ReportTestEntry reportTestEntry = javaBasedTest.getReportTestEntry();
        reportTestEntry.setRan(true);
        reportTestEntry.stopClock();
        JavaReport.addTestEntry(reportTestEntry);

        StepReporters.remove(javaBasedTest);

        TestResultPayloadExtractors.extract(reportTestEntry.getSteps().stream())
                .forEach(reportTestEntry::addTestResultPayload);

        JavaReportShutdownHook.INSTANCE.noOp();
    }
}
