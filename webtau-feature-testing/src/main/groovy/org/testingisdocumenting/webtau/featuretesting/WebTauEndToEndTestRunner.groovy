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

package org.testingisdocumenting.webtau.featuretesting

import org.eclipse.jetty.server.Handler
import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.app.WebTauCliApp
import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.AutoResetAnsiString
import org.testingisdocumenting.webtau.documentation.DocumentationArtifacts
import org.testingisdocumenting.webtau.http.testserver.TestServer
import org.testingisdocumenting.webtau.report.ReportGenerator
import org.testingisdocumenting.webtau.report.ReportGenerators
import org.testingisdocumenting.webtau.report.WarningsReportDataProvider
import org.testingisdocumenting.webtau.reporter.*

import java.nio.file.Path
import java.nio.file.Paths

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg

class WebTauEndToEndTestRunner implements ReportGenerator, ConsoleOutput {
    private final List<String> consoleOutputLines = []
    private final List<Map<String, Object>> warnings = []
    private Map capturedStepsSummary

    TestServer testServer
    private String classifier = ""

    WebTauEndToEndTestRunner(Handler handler) {
        this.testServer = new TestServer(handler)
        ConsoleOutputs.add(this)
        ReportGenerators.add(this)
    }

    void setClassifier(String classifier) {
        this.classifier = classifier
    }

    void startTestServer() {
        testServer.startRandomPort()
    }

    void stopTestServer() {
        testServer.stop()
    }

    void runCli(String testFileName, String configFileName, String... additionalArgs) {
        runCliWithWorkingDir(testFileName, 'examples', configFileName, additionalArgs)
    }

    void runCliWithWorkingDir(String testFileName, String workingDir, String configFileName, String... additionalArgs) {
        def testPath = Paths.get(testFileName)

        def args = ['--workingDir=' + workingDir]

        if (!configFileName.isEmpty()) {
            args.add('--config=' + configFileName)
        }
        args.add('--docPath=' + Paths.get('doc-artifacts'))

        def reportsRoot = Paths.get('webtau-reports')
        args.add('--reportPath=' + buildReportPath(testFileName, '', reportsRoot))
        args.add('--failedReportPath=' + buildReportPath(testFileName, 'failed', reportsRoot))

        args.addAll(Arrays.asList(additionalArgs))
        args.add(testPath.toString())

        WebTauConfig.resetConfigHandlers()
        getCfg().reset()

        def cliApp = new WebTauCliApp(args as String[])

        getCfg().triggerConfigHandlers()

        def testDetails = [scenarioDetails: [], exitCode: 0]

        capturedStepsSummary = [:].withDefault { 0 }

        consoleOutputLines.clear()
        warnings.clear()

        cliApp.start { exitCode ->
            testDetails.exitCode = exitCode
        }

        saveConsoleOutput(testFileName)

        testDetails.scenarioDetails = buildScenarioDetails(cliApp.runner.report)
        if (!warnings.isEmpty()) {
            testDetails.warnings = warnings.collect { it.message }
        }

        validateAndSaveTestDetails(testFileName, testDetails)
    }

    private Path buildReportPath(String testFileName, String passedPrefix, Path reportsRoot) {
        if (testFileName.endsWith('.groovy')) {
            def reportPrefix = (classifier.isEmpty() ? '' : "-${classifier}") +
                    (passedPrefix.isEmpty() ? '' :  "-${passedPrefix}")
            return reportsRoot.resolve(
                    testFileName.replace('.groovy', reportPrefix + '-webtau-report.html')).toAbsolutePath()
        }

        def reportPrefix = classifier.isEmpty() ? '' : "${classifier}-"
        return reportsRoot.resolve(testFileName + '/' + reportPrefix + 'webtau-report.html').toAbsolutePath()
    }

    private void validateAndSaveTestDetails(String testFileName, Map testDetails) {
        WebTauEndToEndTestValidator.validateAndSaveTestDetails(removeExtension(testFileName), classifier, testDetails,
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

    static List<Map<String, ?>> buildScenarioDetails(WebTauReport report) {
        return report.tests.stream().collect { test ->
            def result = [
                    scenario: test.scenario,
                    shortContainerId: test.shortContainerId,
                    stepsSummary: [:]
            ]

            def numberOfSuccessfulSteps = test.calcNumberOfSuccessfulSteps()
            if (numberOfSuccessfulSteps > 0) {
                result.stepsSummary.numberOfSuccessful = numberOfSuccessfulSteps
            }

            def numberOfFailedSteps = test.calcNumberOfFailedSteps()
            if (numberOfFailedSteps > 0) {
                result.stepsSummary.numberOfFailed = numberOfFailedSteps
            }

            if (!test.metadata.isEmpty()) {
                result.metadata = test.metadata.toMap()
            }

            return result
        }
    }

    void saveConsoleOutput(String testFileName) {
        def fileNameOnly = Paths.get(testFileName).fileName.toString()
        def artifactName = fileNameOnly + "-console-output"
        if (DocumentationArtifacts.isRegistered(artifactName)) {
            return
        }

        Path artifactPath = DocumentationArtifacts.captureText(artifactName, String.join("\n", consoleOutputLines))
        println("captured output of $testFileName: $artifactPath")
    }

    @Override
    void out(Object... styleOrValues) {
        consoleOutputLines.add(new AutoResetAnsiString(styleOrValues).toString())
    }

    @Override
    void err(Object... styleOrValues) {
    }

    @Override
    void generate(WebTauReport report) {
        warnings.addAll(report.findCustomData(WarningsReportDataProvider.ID).getData())
    }
}
