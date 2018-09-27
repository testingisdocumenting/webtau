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

package com.twosigma.webtau.cli.interactive

import com.twosigma.webtau.WebTauGroovyDsl
import com.twosigma.webtau.cfg.GroovyRunner
import com.twosigma.webtau.console.ansi.Color
import com.twosigma.webtau.reporter.ConsoleStepReporter
import com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder
import com.twosigma.webtau.reporter.StepReporters
import com.twosigma.webtau.reporter.stacktrace.StackTraceUtils
import com.twosigma.webtau.runner.standalone.StandaloneTest
import com.twosigma.webtau.runner.standalone.StandaloneTestListeners
import com.twosigma.webtau.runner.standalone.StandaloneTestRunner
import com.twosigma.webtau.runner.standalone.report.StandardConsoleTestListener

import java.nio.file.Paths

import static com.twosigma.webtau.cfg.WebTauConfig.getCfg

class WebTauCliInteractive {
    private InteractiveState state

    private CurrentUserSelection currentUserSelection

    private InteractiveConsole console
    private InteractiveTests interactiveTests

    WebTauCliInteractive(StandaloneTestRunner runner) {
        console = new InteractiveConsole(new BufferedReader(new InputStreamReader(System.in)))
        interactiveTests = new InteractiveTests(runner)
        currentUserSelection = new CurrentUserSelection()

        state = InteractiveState.TestSelection
    }

    void start() {
        while (true) {
            switch (state) {
                case InteractiveState.TestSelection:
                    selectTestFile()
                    break
                case InteractiveState.ScenarioSelection:
                    selectScenario()
                    break
                case InteractiveState.RunSelectedScenario:
                    runSelectedScenario()
                    break
                case InteractiveState.AfterScenarioRun:
                    selectScenarioAction()
                    break
                case InteractiveState.Done:
                    return
            }
        }
    }

    private void selectTestFile() {
        displayTestFiles()

        console.readAndHandleIdxOrCommand(commandsForCurrentState(), { handleCommand(it) }, { idx ->
            if (idx < 0 || idx >= interactiveTests.testFilePaths.size()) {
                console.println(Color.RED, 'enter test index')
                return false
            } else {
                currentUserSelection.testFilePath = interactiveTests.testFilePathByIdx(idx)
                state = InteractiveState.ScenarioSelection
                return true
            }
        })
    }

    private void selectScenario() {
        def tests = displayScenarios(currentUserSelection.testFilePath)

        console.readAndHandleIdxOrCommand(commandsForCurrentState(), { handleCommand(it) }, { idx ->
            if (idx < 0 || idx >= tests.size()) {
                console.println(Color.RED, 'enter scenario index')
                return false
            } else {
                currentUserSelection.scenario = tests[idx].scenario
                state = InteractiveState.RunSelectedScenario
                return true
            }
        })
    }

    private void runSelectedScenario() {
        interactiveTests.refreshScenarios(currentUserSelection.testFilePath)

        def test = interactiveTests.findSelectedTest(currentUserSelection)
        if (!test) {
            console.println(Color.RED, 'No scenario found "' + currentUserSelection.scenario + '"')
            state = InteractiveState.ScenarioSelection
            return
        }

        displaySelectedScenario('running')
        test.run()

        if (test.exception) {
            console.println(Color.RED, StackTraceUtils.renderStackTraceWithoutLibCalls(test.exception))
        }

        state = InteractiveState.AfterScenarioRun
    }

    private void selectScenarioAction() {
        displaySelectedScenario('select actions for')
        console.displayCommands(commandsForCurrentState())

        console.readAndHandleIdxOrCommand(commandsForCurrentState(),
                { handleCommand(it)}, { idx -> return false })
    }

    private void displaySelectedScenario(String prefix) {
        console.println(Color.BLUE, prefix + ': ',
                Color.PURPLE, currentUserSelection.testFilePath, ' ',
                Color.YELLOW, currentUserSelection.scenario)
    }

    private List<InteractiveCommand> commandsForCurrentState() {
        return state.availableCommands
    }

    private handleCommand(InteractiveCommand command) {
        switch (command) {
            case InteractiveCommand.Back:
                handleBackCommand()
                break
            case InteractiveCommand.Quit:
                state = InteractiveState.Done
                break
            case InteractiveCommand.Run:
                state = InteractiveState.RunSelectedScenario
                break
        }
    }

    private void handleBackCommand() {
        switch (state) {
            case InteractiveState.ScenarioSelection:
                state = InteractiveState.TestSelection
                break
            case InteractiveState.AfterScenarioRun:
                state = InteractiveState.ScenarioSelection
                break
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

        def tests =  interactiveTests.refreshScenarios(filePath)

        tests.eachWithIndex { test, idx ->
            console.println(Color.YELLOW, idx, Color.PURPLE, ' ', test.scenario)
        }

        return tests
    }

    /*
    this entry point is for manual testing only
     */
    static void main(String[] args) {
        System.setProperty('workingDir', '../webtau-feature-testing/examples/')

        def consoleTestReporter = new StandardConsoleTestListener()
        StandaloneTestListeners.add(consoleTestReporter)
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
