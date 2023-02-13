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

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.testingisdocumenting.webtau.utils.FileUtils

import java.nio.file.Files
import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.contain
import static org.testingisdocumenting.webtau.cfg.WebTauConfig.cfg

class WebTauConceptFeaturesTest {
    private static WebTauEndToEndTestRunner testRunner

    @BeforeClass
    static void init() {
        testRunner = new WebTauEndToEndTestRunner()
    }

    @Before
    void before() {
        cfg.envConfigValue.set('test', 'local')
    }

    @Test
    void "simple dynamic scenario"() {
        runCli('simpleDynamicScenario.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "data driven scenarios from csv"() {
        runCli('dataDrivenCsv.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "data driven scenarios from table"() {
        runCli('dataDrivenTableData.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "hard tests termination"() {
        runCli('testsTermination.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "setup fail all tests skip"() {
        runCli('setupFailSkipTests.groovy', 'webtau-setup-fail-skip.cfg.groovy')
    }

    @Test
    void "run selected tests only"() {
        runCli('runOnlySelected.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "disable tests"() {
        runCli('skipTests.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "disable tests by file name convention"() {
        runCli('skipTestsByFileName.disabled.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "conditional tests custom condition skip"() {
        runCli('conditionalCustomRegistrationSkip.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "conditional tests based on env registration skip"() {
        runCli('conditionalEnvRegistrationSkip.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "conditional tests based on env registration run"() {
        runCli('conditionalEnvRegistrationRun.groovy', 'experimental.cfg.groovy')
    }

    @Test
    void "custom test metadata driven by raw key value"() {
        runCli('metadataRaw.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "custom test metadata override top level only"() {
        runCli('metadataRawTopLevelOverrides.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "custom test metadata driven by method call"() {
        runCli('metadataMethodBased.groovy', 'webtau.cfg.groovy')
    }

    @Test // second similar test is to make sure we clean current test meta in between tests
    void "custom test metadata driven by method sanity check"() {
        runCli('metadataMethodBasedSanityCheck.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "meta data check test listener"() {
        runCli('metaDataTestListener.groovy', 'metaDataTestListener.cfg.groovy')
    }

    @Test
    void "before all after all test listener"() {
        runCli('beforeAllAfterAllSuccess.groovy', 'beforeAllAfterAllTestListener.cfg.groovy')
    }

    @Test
    void "before all test listener and failed test"() {
        runCli('beforeAllAndFailedTest.groovy', 'beforeAllAfterAllTestListener.cfg.groovy')
    }

    @Test
    void "repeat step"() {
        runCli('repeatStep.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "trace"() {
        runCli('trace.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "warning"() {
        runCli('warning.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "step group"() {
        runCli('stepGroup.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "failed matcher"() {
        runCli('failedMatcher.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "do not sleep as sync mechanism"() {
        runCli('sleepAntiPattern.groovy', 'webtau.cfg.groovy')
    }

    @Test
    void "persona context"() {
        runCli('personaContext.groovy', 'webtau.persona.cfg.groovy')
    }

    @Test
    void "runner should use deprecated config file name when no config file is available"() {
        runCliWithWorkingDir('deprecatedConfigCheck.groovy',
                'examples/scenarios/concept/deprecatedconfig')
    }

    @Test
    void "recursive scenario discovery"() {
        runCli("recursive", "recursive.webtau.cfg.groovy")
    }

    @Test
    void "deferred code block inside test"() {
        runCli("deferredCodeBlock.groovy", "webtau.cfg.groovy")
    }

    @Test
    void "should generate failed report using failed report path when provided"() {
        def failedReportPath = Paths.get("webtau-reports/scenarios/concept/failingTest-failed-webtau-report.html")
        Files.deleteIfExists(failedReportPath)

        runCli('failingTest.groovy', 'webtau.cfg.groovy')

        if (!Files.exists(failedReportPath)) {
            throw new AssertionError("failed report should be generated at $failedReportPath")
        }
    }

    @Test
    void "report name"() {
        runCli('simpleScenarioReportName.groovy', 'webtau.reportname.cfg.groovy')
    }

    @Test
    void "console output capture per test and full"() {
        runCli("recursive", "webtau.consoleoutput.cfg.groovy")

        def output = FileUtils.fileTextContent(Paths.get("examples/console-output-capture")
                .resolve("scenarios.concept.recursive.base.operation.groovy-phase-two.out"))
        output.should contain("scenario phase two (base/operation.groovy)")
        output.should contain( "[.] phase two (base/operation.groovy)")
    }

    private static void runCli(String testName, String configFileName, String... additionalArgs) {
        testRunner.runCli("scenarios/concept/$testName",
                configFileName.isEmpty() ? "" : "scenarios/concept/$configFileName", additionalArgs)
    }

    private static void runCliWithWorkingDir(String testName, String workingDir, String... additionalArgs) {
        testRunner.runCliWithWorkingDir(testName, workingDir, '', additionalArgs)
    }
}
