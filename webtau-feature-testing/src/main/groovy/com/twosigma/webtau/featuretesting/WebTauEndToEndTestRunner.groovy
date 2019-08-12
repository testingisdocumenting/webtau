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

package com.twosigma.webtau.featuretesting

import com.twosigma.webtau.cfg.WebTauConfig
import com.twosigma.webtau.cli.WebTauCliApp
import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.Color
import com.twosigma.webtau.expectation.ActualPath
import com.twosigma.webtau.expectation.equality.CompareToComparator
import com.twosigma.webtau.http.testserver.TestServer
import com.twosigma.webtau.reporter.StepReporter
import com.twosigma.webtau.reporter.StepReporters
import com.twosigma.webtau.reporter.TestStep
import com.twosigma.webtau.runner.standalone.StandaloneTest
import com.twosigma.webtau.runner.standalone.StandaloneTestListener
import com.twosigma.webtau.runner.standalone.StandaloneTestListeners
import com.twosigma.webtau.utils.FileUtils
import com.twosigma.webtau.utils.JsonUtils

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static com.twosigma.webtau.cfg.WebTauConfig.getCfg

class WebTauEndToEndTestRunner implements StepReporter, StandaloneTestListener {
    private Map capturedStepsSummary
    private final List scenariosDetails = []

    TestServer testServer

    WebTauEndToEndTestRunner() {
        testServer = new TestServer()
    }

    void startTestServer() {
        testServer.startRandomPort()
    }

    void stopTestServer() {
        testServer.stop()
    }

    void runCli(String testFileName, String configFileName, String... additionalArgs) {
        def testPath = Paths.get(testFileName)

        StepReporters.add(this)
        StandaloneTestListeners.add(this)

        def reportPath = 'examples/'
        if (testFileName.endsWith('.groovy')) {
            reportPath += testFileName.replace('.groovy', '-webtau-report.html')
        } else {
            reportPath += testFileName + '/webtau-report.html'
        }

        try {
            def args = ['--workingDir=examples', '--config=' + configFileName,
                        '--reportPath=' + reportPath,
                        '--docPath=doc-artifacts/ui']
            args.addAll(Arrays.asList(additionalArgs))
            args.add(testPath.toString())

            WebTauConfig.resetConfigHandlers()
            getCfg().reset()

            def cliApp = new WebTauCliApp(args as String[])

            getCfg().triggerConfigHandlers()

            def testDetails = [scenarioDetails: scenariosDetails,
                               exitCode: 0]

            cliApp.start(WebTauCliApp.WebDriverBehavior.KeepWebDriversOpen) { exitCode ->
                testDetails.exitCode = exitCode
            }

            validateAndSaveTestDetails(testFileName, testDetails)
        } finally {
            StepReporters.remove(this)
            StandaloneTestListeners.remove(this)
        }
    }

    private static void validateAndSaveTestDetails(String testFileName, Map testDetails) {
        WebTauEndToEndTestValidator.validateAndSaveTestDetails(removeExtension(testFileName), testDetails,
                this.&sortTestDetailsByContainerId)
    }

    private static Map sortTestDetailsByContainerId(Map testDetails) {
        def scenarioDetails = testDetails.scenarioDetails
        def sortedScenarioDetails = scenarioDetails.sort {
            it.shortContainerId
        }

        return [*: testDetails, scenarioDetails: sortedScenarioDetails]
    }

    private static String removeExtension(String fileName) {
        def idx = fileName.indexOf('.')
        return idx >= 0 ? fileName.substring(0, idx) : fileName
    }

    @Override
    void onStepStart(TestStep step) {
    }

    @Override
    void onStepSuccess(TestStep step) {
        capturedStepsSummary.numberOfSuccessful++
    }

    @Override
    void onStepFailure(TestStep step) {
        capturedStepsSummary.numberOfFailed++
    }

    @Override
    void beforeFirstTest() {
        scenariosDetails.clear()
    }

    @Override
    void beforeScriptParse(Path scriptPath) {
    }

    @Override
    void beforeTestRun(StandaloneTest test) {
        capturedStepsSummary = [:].withDefault { 0 }
    }

    @Override
    void afterTestRun(StandaloneTest test) {
        scenariosDetails.add([scenario        : test.scenario,
                              shortContainerId: test.shortContainerId,
                              stepsSummary    : capturedStepsSummary])
    }

    @Override
    void afterAllTests() {
    }
}
