/*
 * Copyright 2020 webtau maintainers
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

package com.example.tests.junit5

import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.launcher.Launcher
import org.junit.platform.launcher.LauncherDiscoveryRequest
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestIdentifier
import org.junit.platform.launcher.TestPlan
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory
import org.testingisdocumenting.webtau.TestListener
import org.testingisdocumenting.webtau.TestListeners
import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.AutoResetAnsiString
import org.testingisdocumenting.webtau.featuretesting.WebTauEndToEndTestValidator
import org.testingisdocumenting.webtau.javarunner.report.JavaReport
import org.testingisdocumenting.webtau.reporter.StepReporter
import org.testingisdocumenting.webtau.reporter.StepReporters
import org.testingisdocumenting.webtau.reporter.WebTauStep
import org.testingisdocumenting.webtau.reporter.WebTauTest

import static org.junit.platform.engine.discovery.DiscoverySelectors.*
import static org.testingisdocumenting.webtau.WebTauCore.doc
import static org.testingisdocumenting.webtau.browser.Browser.browser
import static org.testingisdocumenting.webtau.cfg.WebTauConfig.cfg

class JUnit5FeatureTestRunner implements StepReporter, TestExecutionListener, TestListener, ConsoleOutput {
    private final List<String> consoleOutputLines = []

    private Map<String, Object> scenariosDetails
    private Map<String, Object> capturedStepsSummary

    void runAndValidate(Class testClass, String baseUrl, String browserBaseUrl = "") {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(testClass))
                .build()

        Launcher launcher = LauncherFactory.create()
        launcher.discover(request)

        scenariosDetails = [:]

        cfg.reset()
        cfg.triggerConfigHandlers()
        cfg.setUrl(baseUrl)
        cfg.reportPath = cfg.fullPath("webtau-reports/" + testClass.canonicalName + ".html")

        if (!browserBaseUrl.isEmpty()) {
            browser.setBaseUrl(browserBaseUrl)
        }

        consoleOutputLines.clear()

        JavaReport.INSTANCE.clear()

        StepReporters.withAdditionalReporter(this) {
            try {
                ConsoleOutputs.add(ConsoleOutputs.defaultOutput)
                ConsoleOutputs.add(this)

                TestListeners.add(this)

                launcher.execute(request, this)
            } finally {
                TestListeners.remove(this)

                ConsoleOutputs.remove(this)
                ConsoleOutputs.remove(ConsoleOutputs.defaultOutput)
            }
        }

        saveConsoleOutput(testClass)
        WebTauEndToEndTestValidator.validateAndSaveTestDetails(testClass.simpleName, scenariosDetails)

        cfg.reportPath = cfg.fullPath("webtau-reports/webtau.report.html")

        // next line is a hack, needs to be reconsidered after a better lifecycle event
        // or when the junit feature tests runner will be excluded from reports
        cfg.triggerConfigHandlers() // trigger handlers again to clean up any data as there will be shutdown handler that will call reports generation again
    }

    @Override
    void testPlanExecutionStarted(TestPlan testPlan) {
    }

    @Override
    void testPlanExecutionFinished(TestPlan testPlan) {
    }

    @Override
    void executionStarted(TestIdentifier testIdentifier) {
        if (!testIdentifier.isTest()) {
            return
        }

        capturedStepsSummary = [:].withDefault { 0 }
    }

    @Override
    void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (!testIdentifier.test) {
            return
        }

        scenariosDetails.put(testIdentifier.displayName, capturedStepsSummary)
    }

    @Override
    void beforeTestRun(WebTauTest test) {
        if (isAfterAllWebTauTests(test)) {
            capturedStepsSummary = [:].withDefault { 0 }
        }
    }

    @Override
    void afterTestRun(WebTauTest test) {
    }

    @Override
    void onStepStart(WebTauStep step) {
    }

    @Override
    void onStepSuccess(WebTauStep step) {
        if (capturedStepsSummary == null) {
            return
        }

        capturedStepsSummary.numberOfSuccessful++
    }

    @Override
    void onStepFailure(WebTauStep step) {
        if (capturedStepsSummary == null) {
            return
        }

        capturedStepsSummary.numberOfFailed++
    }

    @Override
    void out(Object... styleOrValues) {
        consoleOutputLines.add(new AutoResetAnsiString(styleOrValues).toString())
    }

    @Override
    void err(Object... styleOrValues) {

    }

    void saveConsoleOutput(Class<?> testClass) {
       doc.captureText(testClass.canonicalName + "-console-output", String.join("\n", consoleOutputLines))
    }

    private static boolean isAfterAllWebTauTests(WebTauTest test) {
        return test.id == "webtau-after-all-tests"
    }
}
