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
import org.testingisdocumenting.webtau.TestListeners
import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.featuretesting.WebTauEndToEndTestValidator
import org.testingisdocumenting.webtau.reporter.StepReporter
import org.testingisdocumenting.webtau.reporter.StepReporters
import org.testingisdocumenting.webtau.reporter.WebTauStep

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass

class JUnit5FeatureTestRunner implements StepReporter, TestExecutionListener {
    private Map<String, Object> scenariosDetails
    private Map<String, Object> capturedStepsSummary

    void runAndValidate(Class testClass, String baseUrl) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(testClass))
                .build()

        Launcher launcher = LauncherFactory.create()

        launcher.discover(request)
        launcher.registerTestExecutionListeners(this)

        scenariosDetails = [:]
        WebTauConfig.cfg.setBaseUrl(baseUrl)

        StepReporters.withAdditionalReporter(this) {
            launcher.execute(request)
        }

        TestListeners.afterAllTests()
        WebTauEndToEndTestValidator.validateAndSaveTestDetails(testClass.simpleName, scenariosDetails)
    }

    @Override
    void executionStarted(TestIdentifier testIdentifier) {
        if (!testIdentifier.test) {
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
    void onStepStart(WebTauStep step) {

    }

    @Override
    void onStepSuccess(WebTauStep step) {
        capturedStepsSummary.numberOfSuccessful++
    }

    @Override
    void onStepFailure(WebTauStep step) {
        capturedStepsSummary.numberOfFailed++
    }
}
