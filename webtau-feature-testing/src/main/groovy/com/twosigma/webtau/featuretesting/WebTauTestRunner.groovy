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

class WebTauTestRunner implements StepReporter, StandaloneTestListener {
    private static final String RUN_DETAILS_FILE_NAME = 'run-details'
    private static final String EXPECTATIONS_DIR_NAME = 'test-expectations'

    private Map capturedStepsSummary
    private final List scenariosDetails = []

    TestServer testServer

    WebTauTestRunner() {
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

        try {
            def args = ['--workingDir=examples', '--config=' + configFileName]
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
        def fileNameWithoutExt = removeExtension(testFileName)
        def expectedPath = Paths.get(EXPECTATIONS_DIR_NAME)
                .resolve(fileNameWithoutExt).resolve(RUN_DETAILS_FILE_NAME + '.json')
        def actualPath = Paths.get(EXPECTATIONS_DIR_NAME)
                .resolve(fileNameWithoutExt).resolve(RUN_DETAILS_FILE_NAME + '.actual.json')

        def serializedTestDetails = JsonUtils.serializePrettyPrint(testDetails)

        if (! Files.exists(expectedPath)) {
            FileUtils.writeTextContent(expectedPath, serializedTestDetails)

            throw new AssertionError('make sure ' + expectedPath + ' is correct. and commit it as a baseline. ' +
                    'test will not fail next time unless output of the test is changed')
        }

        def expectedDetails = JsonUtils.deserializeAsMap(FileUtils.fileTextContent(expectedPath))

        CompareToComparator comparator = CompareToComparator.comparator()
        def isEqual = comparator.compareIsEqual(new ActualPath('testDetails'), testDetails, expectedDetails)

        if (! isEqual) {
            ConsoleOutputs.out('reports are different, you can use IDE to compare files: ', Color.PURPLE, actualPath,
                    Color.BLUE, ' and ', Color.PURPLE, expectedPath)
            FileUtils.writeTextContent(actualPath, serializedTestDetails)
            throw new AssertionError(comparator.generateNotEqualMatchReport())
        } else {
            Files.deleteIfExists(actualPath)
        }
    }

    private static String removeExtension(String fileName) {
        def idx = fileName.indexOf('.')
        return fileName.substring(0, idx)
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
        scenariosDetails.add([scenario    : test.scenario,
                              stepsSummary: capturedStepsSummary])
    }

    @Override
    void afterAllTests() {
    }
}
