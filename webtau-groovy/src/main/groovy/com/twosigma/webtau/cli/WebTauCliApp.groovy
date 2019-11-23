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

import com.twosigma.webtau.WebTauGroovyDsl
import com.twosigma.webtau.browser.driver.WebDriverCreator
import com.twosigma.webtau.cfg.GroovyRunner
import com.twosigma.webtau.cfg.WebTauConfig
import com.twosigma.webtau.cfg.WebTauGroovyCliArgsConfigHandler
import com.twosigma.webtau.cli.interactive.WebTauCliInteractive
import com.twosigma.webtau.console.ConsoleOutput
import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.AnsiConsoleOutput
import com.twosigma.webtau.console.ansi.Color
import com.twosigma.webtau.console.ansi.NoAnsiConsoleOutput
import com.twosigma.webtau.documentation.DocumentationArtifactsLocation
import com.twosigma.webtau.pdf.Pdf
import com.twosigma.webtau.report.ReportGenerators
import com.twosigma.webtau.reporter.*
import com.twosigma.webtau.runner.standalone.StandaloneTestRunner

import java.nio.file.Files
import java.util.function.Consumer

import static com.twosigma.webtau.cfg.WebTauConfig.getCfg

class WebTauCliApp implements TestListener {
    enum WebDriverBehavior {
        AutoCloseWebDrivers,
        KeepWebDriversOpen
    }

    private static ConsoleTestListener consoleTestReporter = new ConsoleTestListener()
    private static ConsoleOutput consoleOutput

    private static StepReporter stepReporter
    private StandaloneTestRunner runner

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

        if (runner.hasExclusiveTests()) {
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

            def testFiles = cliConfigHandler.testFilesWithFullPath()
            def missing = testFiles.findAll { testFile -> !Files.exists(testFile.path)}
            if (!missing.isEmpty()) {
                throw new RuntimeException('Missing test files:\n  ' + missing.join('  \n'))
            }

            testFiles.forEach {
                runner.process(it, this)
            }

            code()
        } finally {
            removeListeners()

            Pdf.closeAll()

            if (webDriverBehavior == WebDriverBehavior.AutoCloseWebDrivers) {
                WebDriverCreator.quitAll()
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
        TestListeners.add(consoleTestReporter)
        TestListeners.add(this)
        StepReporters.add(stepReporter)
    }

    private void removeListeners() {
        ConsoleOutputs.remove(consoleOutput)
        TestListeners.remove(consoleTestReporter)
        TestListeners.remove(this)
        StepReporters.remove(stepReporter)
    }

    private void runTests() {
        def numThreads = WebTauGroovyCliArgsConfigHandler.getNumberOfThreads()
        if (numThreads > 1 || numThreads == -1) {
            runner.runTestsInParallel(numThreads)
        } else {
            runner.runTests()
        }
    }

    @Override
    void beforeFirstTest() {
    }

    @Override
    void beforeTestRun(WebTauTest test) {
    }

    @Override
    void afterTestRun(WebTauTest test) {
    }

    @Override
    void afterAllTests(WebTauReport report) {
        problemCount = (int) (report.failed + report.errored)
        ReportGenerators.generate(report)
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
