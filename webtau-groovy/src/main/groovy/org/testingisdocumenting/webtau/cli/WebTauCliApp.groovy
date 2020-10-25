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

package org.testingisdocumenting.webtau.cli

import org.fusesource.jansi.AnsiConsole
import org.testingisdocumenting.webtau.WebTauGroovyDsl
import org.testingisdocumenting.webtau.browser.driver.WebDriverCreator
import org.testingisdocumenting.webtau.cfg.GroovyConfigBasedHttpConfiguration
import org.testingisdocumenting.webtau.cfg.GroovyRunner
import org.testingisdocumenting.webtau.cfg.WebTauCliArgsConfig
import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.cfg.WebTauGroovyCliArgsConfigHandler

import org.testingisdocumenting.webtau.cli.repl.Repl
import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.AnsiConsoleOutput
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.console.ansi.NoAnsiConsoleOutput
import org.testingisdocumenting.webtau.pdf.Pdf
import org.testingisdocumenting.webtau.report.ReportGenerator
import org.testingisdocumenting.webtau.report.ReportGenerators
import org.testingisdocumenting.webtau.reporter.*
import org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner

import java.nio.file.Files
import java.util.function.Consumer

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg

class WebTauCliApp implements TestListener, ReportGenerator {
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

        setupAnsi()
    }

    static void main(String[] args) {
        def cliApp = new WebTauCliApp(args)

        if (WebTauCliArgsConfig.isReplMode(args)) {
            cliApp.startRepl()
            System.exit(0)
        } else {
            cliApp.start(WebDriverBehavior.AutoCloseWebDrivers) { exitCode ->
                System.exit(exitCode)
            }
        }
    }

    StandaloneTestRunner getRunner() {
        return runner
    }

    void start(WebDriverBehavior webDriverBehavior, Consumer<Integer> exitHandler) {
        prepareTestsAndRun(webDriverBehavior) {
            runTests()
            ReportGenerators.generate(runner.report)
        }

        if (runner.hasExclusiveTests()) {
            ConsoleOutputs.out(Color.YELLOW, 'sscenario is found, only use it during local development')
            exitHandler.accept(1)
        } else {
            exitHandler.accept(problemCount > 0 ? 1 : 0)
        }
    }

    void startRepl() {
        prepareTestsAndRun(WebDriverBehavior.AutoCloseWebDrivers) {
            runner.setIsReplMode(true)
            def repl = new Repl(runner)
            repl.run()
        }
    }

    private void prepareTestsAndRun(WebDriverBehavior webDriverBehavior, Closure code) {
        init()

        try {
            cfg.printEnumerated()
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
            removeListenersAndHandlers()

            Pdf.closeAll()

            if (webDriverBehavior == WebDriverBehavior.AutoCloseWebDrivers) {
                WebDriverCreator.quitAll()
            }
        }
    }

    private void init() {
        consoleOutput = createConsoleOutput()
        stepReporter = createStepReporter()

        registerListenersAndHandlers()

        runner = new StandaloneTestRunner(
                GroovyRunner.createWithDelegatingEnabled(cfg.workingDir),
                cfg.getWorkingDir())

        WebTauGroovyDsl.initWithTestRunner(runner)
    }

    private void registerListenersAndHandlers() {
        StepReporters.add(stepReporter)
        ConsoleOutputs.add(consoleOutput)
        TestListeners.add(consoleTestReporter)
        TestListeners.add(this)
        ReportGenerators.add(this)
    }

    private void removeListenersAndHandlers() {
        StepReporters.remove(stepReporter)
        ConsoleOutputs.remove(consoleOutput)
        TestListeners.clearAdded()
        ReportGenerators.clearAdded()
        GroovyConfigBasedHttpConfiguration.clear()
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
    void generate(WebTauReport report) {
        problemCount = (int) (report.failed + report.errored)
    }

    private static void setupAnsi() {
        System.setProperty('jansi.passthrough', 'true')
        AnsiConsole.systemInstall()
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
