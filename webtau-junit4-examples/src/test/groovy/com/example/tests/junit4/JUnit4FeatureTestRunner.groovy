/*
 * Copyright 2020 webtau maintainers
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

package com.example.tests.junit4

import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.featuretesting.WebTauEndToEndTestValidator
import org.testingisdocumenting.webtau.reporter.StepReporter
import org.testingisdocumenting.webtau.reporter.StepReporters
import org.junit.runner.Description
import org.junit.runner.JUnitCore
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunListener
import org.testingisdocumenting.webtau.reporter.WebTauStep

class JUnit4FeatureTestRunner extends RunListener implements StepReporter {
    private Map<String, Object> scenariosDetails
    private Map<String, Object> capturedStepsSummary

    void runAndValidate(Class testClass, String baseUrl) {
        JUnitCore junit = new JUnitCore()

        junit.addListener(this)
        scenariosDetails = [:]

        WebTauConfig.cfg.setBaseUrl(baseUrl)

        StepReporters.withAdditionalReporter(this) {
            junit.run(testClass)
        }

        WebTauEndToEndTestValidator.validateAndSaveTestDetails(testClass.simpleName, scenariosDetails)
    }

    @Override
    void testStarted(Description description) throws Exception {
        println "started: " + description.methodName
        capturedStepsSummary = [:].withDefault { 0 }
    }

    @Override
    void testFinished(Description description) throws Exception {
        scenariosDetails.put(description.methodName, capturedStepsSummary)
    }

    @Override
    void testFailure(Failure failure) throws Exception {
        super.testFailure(failure)
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
