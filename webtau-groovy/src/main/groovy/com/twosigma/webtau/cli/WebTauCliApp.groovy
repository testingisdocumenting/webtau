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

package com.twosigma.webtau.cli

import com.twosigma.webtau.console.ConsoleOutput
import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.AnsiConsoleOutput
import com.twosigma.webtau.console.ansi.Color
import com.twosigma.documentation.DocumentationArtifactsLocation
import com.twosigma.webtau.http.HttpRequestHeader
import com.twosigma.webtau.http.HttpValidationResult
import com.twosigma.webtau.http.config.HttpConfiguration
import com.twosigma.webtau.http.config.HttpConfigurations
import com.twosigma.webtau.reporter.ConsoleStepReporter
import com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder
import com.twosigma.webtau.reporter.StepReporter
import com.twosigma.webtau.reporter.StepReporters
import com.twosigma.webtau.runner.standalone.GroovyStandaloneEngine
import com.twosigma.webtau.runner.standalone.StandaloneTest
import com.twosigma.webtau.runner.standalone.StandaloneTestListener
import com.twosigma.webtau.runner.standalone.StandaloneTestListeners
import com.twosigma.webtau.runner.standalone.StandaloneTestRunner
import com.twosigma.webtau.runner.standalone.report.StandardConsoleTestListener
import com.twosigma.webtau.WebTauGroovyDsl
import com.twosigma.webtau.cfg.WebTauConfig
import com.twosigma.webtau.driver.WebDriverCreator
import com.twosigma.webtau.reporter.HtmlReportGenerator
import com.twosigma.webtau.reporter.ScreenshotStepPayload
import com.twosigma.webtau.reporter.ScreenshotStepReporter
import com.twosigma.webtau.utils.FileUtils
import com.twosigma.webtau.utils.JsonUtils

import java.nio.file.Path
import java.nio.file.Paths

class WebTauCliApp implements StandaloneTestListener {
    private static def staticImports = ["com.twosigma.webtau.WebTauDsl"]

    private static WebTauConfig cfg = WebTauConfig.INSTANCE
    private static StandardConsoleTestListener consoleTestReporter = new StandardConsoleTestListener()
    private static StepReporter stepReporter = new ConsoleStepReporter(IntegrationTestsMessageBuilder.converter)
    private static ScreenshotStepReporter screenshotStepReporter = new ScreenshotStepReporter()
    private static ConsoleOutput consoleOutput = new AnsiConsoleOutput()

    private WebTauTestCliConfig cliConfig
    private StandaloneTestRunner runner

    private List<StandaloneTest> tests = []

    WebTauCliApp(String[] args) {
        ConsoleOutputs.add(consoleOutput)

        cliConfig = new WebTauTestCliConfig(args)

        cliConfig.parseConfig(GroovyStandaloneEngine.createWithoutDelegating(cfg.workingDir, staticImports))
        DocumentationArtifactsLocation.setRoot(cfg.getDocArtifactsPath())

        runner = new StandaloneTestRunner(
                GroovyStandaloneEngine.createWithDelegatingEnabled(cfg.workingDir, staticImports),
                cfg.getWorkingDir())

        StandaloneTestListeners.add(consoleTestReporter)
        StandaloneTestListeners.add(this)
        WebTauGroovyDsl.initWithTestRunner(runner)
    }

    void start(boolean autoCloseWebDrivers) {
        try {
            StepReporters.add(stepReporter)
            StepReporters.add(screenshotStepReporter)

            cliConfig.print()

            initHttpConfigurationFromConfig()

            testFiles().forEach {
                runner.process(it, this)
            }

            runner.runTests()
        } finally {
            StandaloneTestListeners.remove(consoleTestReporter)
            StandaloneTestListeners.remove(this)

            if (autoCloseWebDrivers) {
                WebDriverCreator.closeAll()
            }
        }
    }

    private List<Path> testFiles() {
        return cliConfig.getTestFiles().collect { Paths.get(it) }
    }

    void initHttpConfigurationFromConfig() {
        def headersProvider = cliConfig.httpHeaderProvider()
        if (! headersProvider) {
            return
        }

        HttpConfigurations.add(new HttpConfiguration() {
            @Override
            String fullUrl(String url) {
                return url
            }

            @Override
            HttpRequestHeader fullHeader(HttpRequestHeader given) {
                try {
                    HttpConfigurations.disable()
                    return headersProvider.call(given) as HttpRequestHeader
                } finally {
                    HttpConfigurations.enable()
                }
            }
        })
    }

    static void main(String[] args) {
        new WebTauCliApp(args).start(true)
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
        def steps = test.steps
        def listOfMaps = steps.collect { it.toMap() }

        test.addResultPayload({ [steps: listOfMaps ]})

        def payloads = steps.combinedPayloads.flatten()

        def screenshotsPayloads = payloads.findAll { it instanceof ScreenshotStepPayload }
        if (! screenshotsPayloads.isEmpty()) {
            test.addResultPayload({ [screenshot: screenshotsPayloads[0].base64png] })
        }

        def httpPayloads = payloads.findAll { it instanceof HttpValidationResult }
        if (! httpPayloads.isEmpty()) {
            test.addResultPayload({ [httpCalls: httpPayloads*.toMap()] })
        }

        tests.add(test)
    }

    @Override
    void afterAllTests() {
        generateReport()
    }

    void generateReport() {
        def summary = [
                total: consoleTestReporter.total,
                passed: consoleTestReporter.passed,
                failed: consoleTestReporter.failed,
                skipped: consoleTestReporter.skipped,
                errored: consoleTestReporter.errored]

        def report = [summary: summary, tests: tests*.toMap()]
        def json = JsonUtils.serializePrettyPrint(report)

        def reportPath = cfg.getReportPath().toAbsolutePath()
        FileUtils.writeTextContent(reportPath, new HtmlReportGenerator().generate(json))

        ConsoleOutputs.out(Color.BLUE, "report is generated: ", Color.PURPLE, " ", reportPath)
    }
}
