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

import com.twosigma.documentation.DocumentationArtifactsLocation
import com.twosigma.webtau.WebTauGroovyDsl
import com.twosigma.webtau.cfg.GroovyRunner
import com.twosigma.webtau.cfg.WebTauConfig
import com.twosigma.webtau.cfg.WebTauGroovyCliArgsConfigHandler
import com.twosigma.webtau.console.ConsoleOutput
import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.AnsiConsoleOutput
import com.twosigma.webtau.console.ansi.NoAnsiConsoleOutput
import com.twosigma.webtau.driver.WebDriverCreator
import com.twosigma.webtau.pdf.Pdf
import com.twosigma.webtau.report.Report
import com.twosigma.webtau.report.ReportGenerator
import com.twosigma.webtau.report.ReportGenerators
import com.twosigma.webtau.reporter.ConsoleStepReporter
import com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder
import com.twosigma.webtau.reporter.ScopeLimitingStepReporter
import com.twosigma.webtau.reporter.ScreenshotStepReporter
import com.twosigma.webtau.reporter.StepReporter
import com.twosigma.webtau.reporter.StepReporters
import com.twosigma.webtau.reporter.TestResultPayloadExtractors
import com.twosigma.webtau.runner.standalone.StandaloneTest
import com.twosigma.webtau.runner.standalone.StandaloneTestListener
import com.twosigma.webtau.runner.standalone.StandaloneTestListeners
import com.twosigma.webtau.runner.standalone.StandaloneTestRunner
import com.twosigma.webtau.runner.standalone.report.StandardConsoleTestListener

import java.nio.file.Path
import java.nio.file.Paths

import static com.twosigma.webtau.cfg.WebTauConfig.getCfg

class WebTauCliApp implements StandaloneTestListener, ReportGenerator {
    private static StandardConsoleTestListener consoleTestReporter = new StandardConsoleTestListener()
    private static ScreenshotStepReporter screenshotStepReporter = new ScreenshotStepReporter()
    private static ConsoleOutput consoleOutput = new AnsiConsoleOutput()

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

        ConsoleOutputs.add(createConsoleOutput())
        stepReporter = createStepReporter()

        DocumentationArtifactsLocation.setRoot(cfg.getDocArtifactsPath())

        runner = new StandaloneTestRunner(
                GroovyRunner.createWithDelegatingEnabled(cfg.workingDir),
                cfg.getWorkingDir())

        report = new Report()
        WebTauGroovyDsl.initWithTestRunner(runner)
    }

    void start(boolean autoCloseWebDrivers) {
        try {
            StandaloneTestListeners.add(consoleTestReporter)
            StandaloneTestListeners.add(this)
            StepReporters.add(stepReporter)
            StepReporters.add(screenshotStepReporter)
            ReportGenerators.add(this)

            cfg.print()
            ConsoleOutputs.out()

            testFiles().forEach {
                runner.process(it, this)
            }

            runTests()
        } finally {
            StandaloneTestListeners.remove(consoleTestReporter)
            StandaloneTestListeners.remove(this)
            StepReporters.remove(stepReporter)
            StepReporters.remove(screenshotStepReporter)
            ReportGenerators.remove(this)

            Pdf.closeAll()

            if (autoCloseWebDrivers) {
                WebDriverCreator.closeAll()
            }
        }
    }

    int getProblemCount() {
        return problemCount
    }

    private void runTests() {
        if (WebTauGroovyCliArgsConfigHandler.getNumberOfThreads() > 1) {
            runner.runTestsInParallel(WebTauGroovyCliArgsConfigHandler.getNumberOfThreads())
        } else {
            runner.runTests()
        }
    }

    private List<Path> testFiles() {
        return cliConfigHandler.testFiles.collect { fileName ->
            return cfg.workingDir.resolve(Paths.get(fileName))
        }
    }

    static void main(String[] args) {
        def cliApp = new WebTauCliApp(args)
        cliApp.start(true)

        System.exit(cliApp.problemCount)
    }

    @Override
    void beforeFirstTest() {
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
        problemCount = (int) (summary.failed + summary.errored + summary.skipped)
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
