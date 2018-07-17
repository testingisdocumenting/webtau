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

package com.twosigma.webtau.featuretesting

import com.twosigma.webtau.cli.WebTauCliApp
import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.Color
import com.twosigma.webtau.http.testserver.TestServer
import com.twosigma.webtau.reporter.StepReporter
import com.twosigma.webtau.reporter.StepReporters
import com.twosigma.webtau.reporter.TestStep
import com.twosigma.webtau.runner.standalone.StandaloneTest
import com.twosigma.webtau.runner.standalone.StandaloneTestListener
import com.twosigma.webtau.runner.standalone.StandaloneTestListeners
import com.twosigma.webtau.utils.FileUtils
import com.twosigma.webtau.utils.StringUtils
import org.junit.Assert

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static com.twosigma.webtau.featuretesting.FeaturesDocArtifactsExtractor.extractScenarioBody

class WebTauTestRunner implements StepReporter, StandaloneTestListener {
    private static final int testServerPort = 8180
    private static final String STEPS_OUTPUT_FILE_NAME = 'steps-output.txt'

    private String testScenario
    private List<String> capturedStepsOutput = new ArrayList<>()

    TestServer testServer

    WebTauTestRunner() {
        testServer = new TestServer()
    }

    void startTestServer() {
        testServer.start(testServerPort)
    }

    void stopTestServer() {
        testServer.stop()
    }

    void runCli(String testFileName) {
        def testPath = Paths.get("examples/" + testFileName)
        def script = FileUtils.fileTextContent(testPath)
        def example = extractScenarioBody(script)

        StepReporters.add(this)
        StandaloneTestListeners.add(this)
        capturedStepsOutput.clear()

        try {
            def cliApp = new WebTauCliApp("--url=http://localhost:" + testServerPort, testPath.toString())
            cliApp.start(false)
        } finally {
            def testArtifact = [scenario: testScenario, example: example, stepsOutput: capturedStepsOutput]

            validateAndSaveTestArtifact(testFileName, testArtifact)

            StepReporters.remove(this)
            StandaloneTestListeners.remove(this)
        }
    }

    private static void validateAndSaveTestArtifact(String testFileName, Map artifact) {
        def fileNameWithoutExt = removeExtension(testFileName)
        def expectedPath = Paths.get("test-artifacts").resolve(fileNameWithoutExt).resolve(STEPS_OUTPUT_FILE_NAME)
        def actualPath = Paths.get("test-artifacts").resolve(fileNameWithoutExt).resolve(STEPS_OUTPUT_FILE_NAME + ".actual")

        String actualStepsOutput = artifact.stepsOutput.join('\n')

        if (! Files.exists(expectedPath)) {
            FileUtils.writeTextContent(expectedPath, actualStepsOutput)

            throw new AssertionError("make sure " + expectedPath + " is correct. and commit it as a baseline. " +
                    "test will not fail next time unless output of the test is changed")
        }

        def expectedStepsOutput = FileUtils.fileTextContent(expectedPath)

        if (! expectedStepsOutput.equals(actualStepsOutput)) {
            ConsoleOutputs.out("reports are different, you can use IDE to compare files: ", Color.PURPLE, actualPath,
                    Color.BLUE, " and ", Color.PURPLE, expectedPath)
            FileUtils.writeTextContent(actualPath, actualStepsOutput)
            Assert.assertEquals(expectedStepsOutput.join("\n"), artifact.report.join("\n"))
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
        def stepAsText = "> " + StringUtils.createIndentation(step.numberOfParents * 2) + step.getInProgressMessage().toString()
        capturedStepsOutput.add(stepAsText)
    }

    @Override
    void onStepSuccess(TestStep step) {
        def stepAsText = ". " + StringUtils.createIndentation(step.numberOfParents * 2)
        capturedStepsOutput.add(stepAsText + step.getCompletionMessage().toString())
    }

    @Override
    void onStepFailure(TestStep step) {
        def stepAsText = "X " + StringUtils.createIndentation(step.numberOfParents * 2) + step.getCompletionMessage().toString()
        capturedStepsOutput.add(stepAsText)
    }

    @Override
    void beforeFirstTest() {
    }

    @Override
    void beforeScriptParse(Path scriptPath) {
    }

    @Override
    void beforeTestRun(StandaloneTest test) {
    }

    @Override
    void afterTestRun(StandaloneTest test) {
        testScenario = test.description
    }

    @Override
    void afterAllTests() {
    }
}
