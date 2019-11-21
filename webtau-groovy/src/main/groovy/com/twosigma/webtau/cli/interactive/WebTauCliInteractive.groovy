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

package com.twosigma.webtau.cli.interactive

import com.twosigma.webtau.WebTauGroovyDsl
import com.twosigma.webtau.cfg.GroovyRunner
import com.twosigma.webtau.console.ansi.Color
import com.twosigma.webtau.reporter.ConsoleStepReporter
import com.twosigma.webtau.reporter.ConsoleTestListener
import com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder
import com.twosigma.webtau.reporter.StepReporters
import com.twosigma.webtau.reporter.TestListeners
import com.twosigma.webtau.reporter.stacktrace.StackTraceUtils
import com.twosigma.webtau.runner.standalone.StandaloneTest
import com.twosigma.webtau.runner.standalone.StandaloneTestRunner

import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

import static com.twosigma.webtau.cfg.WebTauConfig.getCfg

class WebTauCliInteractive {
    // explicit and watch test execution must be in the same thread because browser instance is thread bound
    private final ExecutorService executorService = Executors.newSingleThreadExecutor()

    private AtomicReference<InteractiveState> atomicState = new AtomicReference<>()

    private TestsSelection currentUserSelection

    private InteractiveConsole console
    private InteractiveTests interactiveTests
    private BackgroundFileWatcher watcher

    private AtomicBoolean isRunningWatchedTest
    private final StandaloneTestRunner runner

    WebTauCliInteractive(StandaloneTestRunner runner) {
        this.runner = runner

        setDefaultReportPath()

        console = new InteractiveConsole(new BufferedReader(new InputStreamReader(System.in)))
        interactiveTests = new InteractiveTests(runner)
        currentUserSelection = new TestsSelection()

        isRunningWatchedTest = new AtomicBoolean(false)
        watcher = new BackgroundFileWatcher(cfg.workingDir, this.&onFileChangeDuringWatch)

        state = InteractiveState.TestSelection
    }

    InteractiveState getState() {
        return atomicState.get()
    }

    void setState(InteractiveState state) {
        this.atomicState.set(state)
    }

    void start() {
        while (true) {
            switch (state) {
                case InteractiveState.TestSelection:
                    selectTestFile()
                    break
                case InteractiveState.ScenarioSelection:
                    selectScenarios()
                    break
                case InteractiveState.RunSelectedScenario:
                    runSelectedScenario()
                    break
                case InteractiveState.AfterScenarioRun:
                    selectScenarioAction()
                    break
                case InteractiveState.WatchingSelectedScenario:
                    watchSelectedScenario()
                    break
                case InteractiveState.Done:
                    cleanup()
                    return
            }
        }
    }

    private static void setDefaultReportPath() {
        if (!cfg.reportPathConfigValue.isDefault()) {
            return
        }

        cfg.reportPathConfigValue.set('interative-cli',
                cfg.reportPath.toAbsolutePath().parent.resolve('webtau.interactive.report.html'))
    }

    private void selectTestFile() {
        displayTestFiles()

        console.readAndHandleParsedCommand(commandsForCurrentState(), { handleCommand(it) }, { indexes ->
            if (indexes.size() > 1 || indexes.find { idx -> idx < 0 || idx >= interactiveTests.testFilePaths.size() }) {
                console.println(Color.RED, 'enter a single test index')
                return false
            } else {
                currentUserSelection.testFilePath = interactiveTests.testFilePathByIdx(indexes.first())
                state = InteractiveState.ScenarioSelection
                return true
            }
        })
    }

    private void selectScenarios() {
        def tests = displayScenarios(currentUserSelection.testFilePath)

        console.readAndHandleParsedCommand(commandsForCurrentState(), { handleCommand(it) }, { List<Integer> indexes ->
            if (indexes.find { idx -> idx < 0 || idx >= tests.size() }) {
                console.println(Color.RED, 'enter scenario index(es)')
                return false
            } else {
                currentUserSelection.scenarios = indexes.collect { idx -> tests[idx].scenario }
                state = InteractiveState.RunSelectedScenario
                return true
            }
        })
    }

    private void runSelectedScenario() {
        runSelectedScenariosUsingExecutor()
        state = InteractiveState.AfterScenarioRun
    }

    private void runScenarios(TestsSelection testSelection) {
        interactiveTests.refreshScenarios(testSelection.testFilePath)

        def tests = interactiveTests.findSelectedTests(testSelection)
        if (tests.size() != testSelection.scenarios.size()) {
            console.println(Color.RED, 'Not all scenarios found "' + testSelection.scenarios + '"')
            state = InteractiveState.ScenarioSelection
            return
        }

        runner.resetAndWithListeners {
            tests.each { test ->
                displaySelectedScenarios('running', test.scenario)
                runner.runTestAndNotifyListeners(test)

                def exception = test.exception
                if (exception) {
                    console.println(Color.RED, 'failed test: ',
                            Color.PURPLE, currentUserSelection.testFilePath, ' ',
                            Color.YELLOW, test.scenario)
                    console.println(Color.RED, StackTraceUtils.renderStackTraceWithoutLibCalls(exception))
                }
            }
        }
    }

    void runSelectedScenariosUsingExecutor() {
        def futureResult = executorService.submit { runScenarios(currentUserSelection) }
        futureResult.get()
    }

    private void watchSelectedScenario() {
        watcher.startIfRequired()
        selectScenarioAction()
    }

    private void selectScenarioAction() {
        displaySelectedScenarioPrompt()
        console.readAndHandleParsedCommand(commandsForCurrentState(),
                { handleCommand(it)}, { idx -> return false })
    }

    private void displaySelectedScenarioPrompt() {
        displaySelectedScenarios('select actions for')
        console.displayCommands(commandsForCurrentState())
    }

    private void displaySelectedScenarios(String prefix) {
        displaySelectedScenarios(prefix, currentUserSelection.scenarios.join(', '))
    }

    private void displaySelectedScenarios(String prefix, String scenario) {
        console.println(Color.BLUE, prefix + ': ',
                Color.PURPLE, currentUserSelection.testFilePath, ' ',
                Color.YELLOW, scenario)
    }

    private List<InteractiveCommand> commandsForCurrentState() {
        return state.availableCommands
    }

    private handleCommand(ParsedCommand idxAndCommand) {
        def command = idxAndCommand.commands[0]
        switch (command) {
            case InteractiveCommand.Back:
                displayWatchOffIfRequired()
                handleBackCommand()
                break
            case InteractiveCommand.Quit:
                state = InteractiveState.Done
                break
            case InteractiveCommand.Run:
                displayWatchOffIfRequired()
                updateSelectedScenariosIfRequired(idxAndCommand.indexes)

                state = InteractiveState.RunSelectedScenario
                break
            case InteractiveCommand.Watch:
                displayWatchOn()
                state = InteractiveState.WatchingSelectedScenario
                break
            case InteractiveCommand.StopWatch:
                displayWatchOff()
                state = InteractiveState.AfterScenarioRun
                break
        }
    }

    private void handleBackCommand() {
        switch (state) {
            case InteractiveState.ScenarioSelection:
                state = InteractiveState.TestSelection
                break
            case InteractiveState.AfterScenarioRun:
            case InteractiveState.WatchingSelectedScenario:
                state = InteractiveState.ScenarioSelection
                break
        }
    }

    private displayWatchOn() {
        console.println(Color.YELLOW, 'watch is on')
    }

    private displayWatchOff() {
        console.println(Color.YELLOW, 'watch is off')
    }

    private displayWatchOffIfRequired() {
        if (state == InteractiveState.WatchingSelectedScenario) {
            displayWatchOff()
        }
    }

    private void displayTestFiles() {
        console.println(Color.BLUE, 'Test files:')

        interactiveTests.testFilePaths.eachWithIndex { path, idx ->
            console.println(Color.YELLOW, idx, Color.PURPLE, ' ', path)
        }
    }

    private List<StandaloneTest> displayScenarios(String filePath) {
        console.println(Color.BLUE, 'Test scenarios of ', Color.PURPLE, filePath, Color.BLUE, ':')

        def tests = interactiveTests.refreshScenarios(filePath)

        tests.eachWithIndex { test, idx ->
            console.println(Color.YELLOW, idx, Color.PURPLE, ' ', test.scenario)
        }

        return tests
    }

    private void updateSelectedScenariosIfRequired(List<Integer> indexes) {
        if (indexes.isEmpty()) {
            return
        }

        def tests = interactiveTests.refreshScenarios(currentUserSelection.testFilePath)
        currentUserSelection.scenarios = indexes.collect { idx -> tests[idx].scenario }
    }

    private void onFileChangeDuringWatch(Path filePath) {
        if (state != InteractiveState.WatchingSelectedScenario || isRunningWatchedTest.get()) {
            return
        }

        console.println(Color.YELLOW, '\nchange detected: ', Color.PURPLE, filePath)
        isRunningWatchedTest.set(true)
        try {
            runSelectedScenariosUsingExecutor()
        } finally {
            isRunningWatchedTest.set(false)

            displaySelectedScenarioPrompt()
            console.displayPrompt()
        }
    }

    private void cleanup() {
        watcher.stop()
    }

    /*
    this entry point is for manual testing only
     */
    static void main(String[] args) {
        System.setProperty('workingDir', '../webtau-feature-testing/examples/')

        def consoleTestReporter = new ConsoleTestListener()
        TestListeners.add(consoleTestReporter)
        StepReporters.add(new ConsoleStepReporter(IntegrationTestsMessageBuilder.converter))

        def runner = new StandaloneTestRunner(
                GroovyRunner.createWithDelegatingEnabled(cfg.workingDir),
                cfg.getWorkingDir())

        WebTauGroovyDsl.initWithTestRunner(runner)

        ['basic.groovy', 'findersFilters.groovy'].collect {
            Paths.get('scenarios/ui/').resolve(it)
        }.each {
            runner.process(it, this)
        }

        def interactive = new WebTauCliInteractive(runner)
        interactive.start()
    }
}
