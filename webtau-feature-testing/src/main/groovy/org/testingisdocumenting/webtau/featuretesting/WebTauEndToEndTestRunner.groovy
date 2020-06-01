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

import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.cli.WebTauCliApp
import org.testingisdocumenting.webtau.documentation.DocumentationArtifactsLocation
import org.testingisdocumenting.webtau.http.testserver.TestServer
import org.testingisdocumenting.webtau.reporter.*

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg

class WebTauEndToEndTestRunner  {
    private Map capturedStepsSummary

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

        def targetClassesLocation = DocumentationArtifactsLocation.classBasedLocation(WebTauEndToEndTestRunner)
        def reportPath = targetClassesLocation
                .resolve(testFileName.endsWith('.groovy') ?
                        testFileName.replace('.groovy', '-webtau-report.html'):
                        testFileName + '/webtau-report.html')

        def args = ['--workingDir=examples', '--config=' + configFileName,
                    '--docPath=' + targetClassesLocation.resolve('doc-artifacts'),
                    '--reportPath=' + reportPath]
        args.addAll(Arrays.asList(additionalArgs))
        args.add(testPath.toString())

        WebTauConfig.resetConfigHandlers()
        getCfg().reset()

        def cliApp = new WebTauCliApp(args as String[])

        getCfg().triggerConfigHandlers()

        def testDetails = [scenarioDetails: [], exitCode: 0]

        capturedStepsSummary = [:].withDefault { 0 }

        cliApp.start(WebTauCliApp.WebDriverBehavior.KeepWebDriversOpen) { exitCode ->
            testDetails.exitCode = exitCode
        }

        testDetails.scenarioDetails = buildScenarioDetails(cliApp.runner.report)

        validateAndSaveTestDetails(testFileName, testDetails)
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
}
