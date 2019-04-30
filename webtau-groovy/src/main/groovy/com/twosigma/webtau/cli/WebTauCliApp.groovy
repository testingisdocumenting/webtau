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

package com.twosigma.webtau.cli

import com.twosigma.webtau.documentation.DocumentationArtifactsLocation
import com.twosigma.webtau.WebTauGroovyDsl
import com.twosigma.webtau.browser.driver.WebDriverCreator
import com.twosigma.webtau.browser.reporter.ScreenshotStepReporter
import com.twosigma.webtau.cfg.GroovyRunner
import com.twosigma.webtau.cfg.WebTauConfig
import com.twosigma.webtau.cfg.WebTauGroovyCliArgsConfigHandler
import com.twosigma.webtau.cli.interactive.WebTauCliInteractive
import com.twosigma.webtau.console.ConsoleOutput
import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.AnsiConsoleOutput
import com.twosigma.webtau.console.ansi.Color
import com.twosigma.webtau.console.ansi.NoAnsiConsoleOutput
import com.twosigma.webtau.pdf.Pdf
import com.twosigma.webtau.report.Report
import com.twosigma.webtau.report.ReportGenerator
import com.twosigma.webtau.report.ReportGenerators
import com.twosigma.webtau.reporter.*
import com.twosigma.webtau.runner.standalone.StandaloneTest
import com.twosigma.webtau.runner.standalone.StandaloneTestListener
import com.twosigma.webtau.runner.standalone.StandaloneTestListeners
import com.twosigma.webtau.runner.standalone.StandaloneTestRunner
import com.twosigma.webtau.runner.standalone.report.StandardConsoleTestListener

import java.nio.file.Files
import java.nio.file.Path
import java.util.function.Consumer

import static com.twosigma.webtau.cfg.WebTauConfig.getCfg

class WebTauCliApp implements StandaloneTestListener, ReportGenerator {
    enum WebDriverBehavior {
        AutoCloseWebDrivers,
        KeepWebDriversOpen
    }

    private static StandardConsoleTestListener consoleTestReporter = new StandardConsoleTestListener()
    private static ScreenshotStepReporter screenshotStepReporter = new ScreenshotStepReporter()
    private static ConsoleOutput consoleOutput

    private static StepReporter stepReporter
    private StandaloneTestRunner runner
    private Report report

    private int problemCount = 0
    private WebTauGroovyCliArgsConfigHandler cliConfigHandler

    WebTauCliApp(String[] args) {
        System.setProperty("java.awt.headless", "true")

        cliConfigHandler = new WebTauGroovyCliArgsConfigHandler(args)
        WebTauConfig.registerConfigHandlerAsFirstHandler(cliConfigHandler)
        WebTauConfig.registerConfigHandlerAsLastHandler(cliConfigHandler)
    }

    static void main(String[] args) {
        def cliApp = new WebTauCliApp(args)

        if (getCfg().isInteractive()) {
            cliApp.startInteractive()
            System.exit(0)
        } else {
            cliApp.start(WebDriverBehavior.AutoCloseWebDrivers) { exitCode ->
                System.exit(exitCode)
            }
        }
    }

    void start(WebDriverBehavior webDriverBehavior, Consumer<Integer> exitHandler) {
        prepareTestsAndRun(webDriverBehavior) {
            runTests()
        }

        if (!runner.exclusiveTests.isEmpty()) {
            ConsoleOutputs.out(Color.YELLOW, 'sscenario is found, only use it during local development')
            exitHandler.accept(1)
        } else {
            exitHandler.accept(problemCount > 0 ? 1 : 0)
        }
    }

    void startInteractive() {
        prepareTestsAndRun(WebDriverBehavior.AutoCloseWebDrivers) {
            def interactive = new WebTauCliInteractive(runner)
            interactive.start()
        }
    }

    private void prepareTestsAndRun(WebDriverBehavior webDriverBehavior, Closure code) {
        init()

        try {
            cfg.print()
            ConsoleOutputs.out()

            def fullPaths = cliConfigHandler.testFilesWithFullPath()
            def missing = fullPaths.findAll { path -> !Files.exists(path)}
            if (!missing.isEmpty()) {
                throw new RuntimeException('Missing test files:\n  ' + missing.join('  \n'))
            }

            fullPaths.forEach {
                runner.process(it, this)
            }

            code()
        } finally {
            removeListeners()

            Pdf.closeAll()

            if (webDriverBehavior == WebDriverBehavior.AutoCloseWebDrivers) {
                WebDriverCreator.closeAll()
            }
        }
    }

    private void init() {
        consoleOutput = createConsoleOutput()
        stepReporter = createStepReporter()

        registerListeners()

        DocumentationArtifactsLocation.setRoot(cfg.getDocArtifactsPath())

        runner = new StandaloneTestRunner(
                GroovyRunner.createWithDelegatingEnabled(cfg.workingDir),
                cfg.getWorkingDir())

        WebTauGroovyDsl.initWithTestRunner(runner)
    }

    private void registerListeners() {
        ConsoleOutputs.add(consoleOutput)
        StandaloneTestListeners.add(consoleTestReporter)
        StandaloneTestListeners.add(this)
        StepReporters.add(stepReporter)
        StepReporters.add(screenshotStepReporter)
        ReportGenerators.add(this)
    }

    private void removeListeners() {
        ConsoleOutputs.remove(consoleOutput)
        StandaloneTestListeners.remove(consoleTestReporter)
        StandaloneTestListeners.remove(this)
        StepReporters.remove(stepReporter)
        StepReporters.remove(screenshotStepReporter)
        ReportGenerators.remove(this)
    }

    private void runTests() {
        if (WebTauGroovyCliArgsConfigHandler.getNumberOfThreads() > 1) {
            runner.runTestsInParallel(WebTauGroovyCliArgsConfigHandler.getNumberOfThreads())
        } else {
            runner.runTests()
        }
    }

    @Override
    void beforeFirstTest() {
        report = new Report()
        report.startTimer()
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

        TestResultPayloadExtractors.extract(steps.stream()).each { p ->
            test.addResultPayload(p)
        }

        report.addTestEntry(test.reportTestEntry)
    }

    @Override
    void afterAllTests() {
        report.stopTimer()

        ReportGenerators.generate(report)
    }

    @Override
    void generate(Report report) {
        def summary = report.createSummary()
        problemCount = (int) (summary.failed + summary.errored)
    }

    private static ConsoleOutput createConsoleOutput() {
        if (cfg.getVerbosityLevel() == 0) {
            return new SilentConsoleOutput()
        }

        return getCfg().isAnsiEnabled() ?
                new AnsiConsoleOutput():
                new NoAnsiConsoleOutput()
    }

    private static StepReporter createStepReporter() {
        return new ScopeLimitingStepReporter(new ConsoleStepReporter(IntegrationTestsMessageBuilder.converter),
                cfg.getVerbosityLevel() - 1)
    }
}
