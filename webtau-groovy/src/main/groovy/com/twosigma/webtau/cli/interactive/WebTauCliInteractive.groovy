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
import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.AutoResetAnsiString
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
    enum State {
        TestSelection,
        ScenarioSelection,
        ScenarioActionSelection,
        AfterRun,
        Done
    }

    private StandaloneTestRunner runner
    private LinkedHashSet<String> testFilePaths
    private Map<String, List<StandaloneTest>> testByPath = [:]
    private BufferedReader inReader

    private State state

    private int selectedPathIdx = -1
    private int selectedScenarioIdx = -1

    WebTauCliInteractive(StandaloneTestRunner runner) {
        this.runner = runner
        this.testFilePaths = new LinkedHashSet<>(runner.tests.reportTestEntry.filePath*.toString())

        testByPath = runner.tests.groupBy { it.filePath.toString() }
        inReader = new BufferedReader(new InputStreamReader(System.in))

        state = State.TestSelection
    }

    void start() {
        while (true) {
            switch (state) {
                case State.TestSelection:
                    selectTestFile()
                    break
                case State.ScenarioSelection:
                    selectScenario()
                    break
                case State.ScenarioActionSelection:
                    selectScenarioAction()
                    break
                case State.Done:
                    return
            }
        }
    }

    void selectTestFile() {
        displayTestFiles()

        readAndHandleIdxOrCommand { idx ->
            if (idx < 0 || idx >= testFilePaths.size()) {
                ConsoleOutputs.out(Color.RED, 'enter test index')
                return false
            } else {
                selectedPathIdx = idx
                state = State.ScenarioSelection
                return true
            }
        }
    }

    void selectScenario() {
        def scenarios = displayScenarios(selectedPathIdx)

        readAndHandleIdxOrCommand { idx ->
            if (idx < 0 || idx >= scenarios.size()) {
                ConsoleOutputs.out(Color.RED, 'enter scenario index')
                return false
            } else {
                selectedScenarioIdx = idx
                state = State.ScenarioActionSelection
                return true
            }
        }
    }

    void selectScenarioAction() {
        def scenarioCommands = commandsForCurrentState()
        displaySelectedScenario()
        displayCommands(scenarioCommands)

        readAndHandleIdxOrCommand { idx -> return false }
    }

    void displaySelectedScenario() {
        def filePath = testFilePaths[selectedPathIdx]
        def tests = testByPath[filePath]
        def test = tests[selectedScenarioIdx]

        ConsoleOutputs.out(Color.PURPLE, filePath, ': ', Color.YELLOW, test.scenario)
    }

    void runSelectedScenario() {
        def filePath = testFilePaths[selectedPathIdx]
        def tests = testByPath[filePath]

        def test = tests[selectedScenarioIdx]
        test.run()

        if (test.exception) {
            ConsoleOutputs.out(Color.RED, StackTraceUtils.renderStackTraceWithoutLibCalls(test.exception))
        }

        state = State.ScenarioActionSelection
    }

    IdxOrCommand readIdxOrCommand() {
        def allowedCommands = commandsForCurrentState()

        while (true) {
            def line = readLine()
            def idxOrCommand = new IdxOrCommand(line)

            def command = idxOrCommand.command

            if ((!command || allowedCommands.find { it.matches() == command }) && idxOrCommand.idx == null) {
                ConsoleOutputs.out(Color.RED, 'enter a number or a command')
                displayCommands(allowedCommands)
            } else {
                return idxOrCommand
            }
        }
    }

    String readLine() {
        print new AutoResetAnsiString(Color.GREEN, 'webtau', Color.YELLOW, ' > ')
        return inReader.readLine()
    }

    void readAndHandleIdxOrCommand(Closure idxHandler) {
        while (true) {
            def idxOrCommand = readIdxOrCommand()

            if (idxOrCommand.command) {
                handleCommand(idxOrCommand.command)
                return
            }

            def done = idxHandler(idxOrCommand.idx)
            if (done) {
                return
            }
        }
    }

    private handleCommand(InteractiveCommand command) {
        switch (command) {
            case InteractiveCommand.Back:
                handleBackCommand()
                break
            case InteractiveCommand.Quit:
                state = State.Done
                break
            case InteractiveCommand.Run:
                runSelectedScenario()
                break
        }
    }

    void handleBackCommand() {
        switch (state) {
            case State.ScenarioSelection:
                state = State.TestSelection
                break
            case State.ScenarioActionSelection:
                state = State.ScenarioSelection
                break
            case State.AfterRun:
                state = State.ScenarioSelection
                break
        }
    }

    void displayTestFiles() {
        ConsoleOutputs.out(Color.BLUE, 'Test files:')

        testFilePaths.eachWithIndex { path, idx ->
            ConsoleOutputs.out(Color.YELLOW, idx, Color.PURPLE, ' ', path)
        }
    }

    List<StandaloneTest> displayScenarios(int pathIdx) {
        def filePath = testFilePaths[pathIdx]
        ConsoleOutputs.out(Color.BLUE, 'Test scenarios of ', Color.PURPLE, filePath, Color.BLUE, ':')

        def tests = testByPath[filePath]
        tests.eachWithIndex { test, idx ->
            ConsoleOutputs.out(Color.YELLOW, idx, Color.PURPLE, ' ', test.scenario)
        }

        return tests
    }

    List<InteractiveCommand> commandsForCurrentState() {
        switch (state) {
            case State.TestSelection:
            case State.ScenarioSelection:
                return [InteractiveCommand.Back, InteractiveCommand.Quit]
            case State.AfterRun:
            case State.ScenarioActionSelection:
                return [InteractiveCommand.Run, InteractiveCommand.Watch,
                        InteractiveCommand.Back, InteractiveCommand.Quit]
        }

        return [InteractiveCommand.Quit]
    }

    static void displayCommands(List<InteractiveCommand> commands) {
        commands.each this.&displayCommand
    }

    static void displayCommand(InteractiveCommand command) {
        ConsoleOutputs.out(Color.BLUE, command.prefixes.join(', '), Color.YELLOW, ' - ', command.description)
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
