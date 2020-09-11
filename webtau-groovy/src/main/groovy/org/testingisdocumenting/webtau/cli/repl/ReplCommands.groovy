/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.cli.repl

import groovy.transform.PackageScope
import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.documentation.DocumentationArtifacts
import org.testingisdocumenting.webtau.reporter.stacktrace.StackTraceUtils
import org.testingisdocumenting.webtau.runner.standalone.StandaloneTest

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.regex.Pattern

import static org.testingisdocumenting.webtau.console.ConsoleOutputs.out

class ReplCommands {
    // explicit and watch test execution must be in the same thread because browser instance is thread bound
    private final ExecutorService executorService = Executors.newSingleThreadExecutor()

    @PackageScope
    static InteractiveTests interactiveTests

    private static TestsSelection testsSelection = new TestsSelection()
    private static List<StandaloneTest> availableScenarios = []

    static WebTauConfig getReloadConfig() {
        return reloadConfig()
    }

    static WebTauConfig reloadConfig() {
        WebTauConfig.resetConfigHandlers()
        WebTauConfig.getCfg().reset()
        WebTauConfig.getCfg().triggerConfigHandlers()

        return WebTauConfig.cfg
    }

    static boolean getTestSelected() {
        return testsSelection.testFilePath != null
    }

    static select(Integer idx) {
        if (testSelected) {
            selectScenario(idx)
        } else {
            selectTest(idx)
        }
    }

    static selectTest(Integer idx) {
        if (idx < 0 || idx >= interactiveTests.testFilePaths.size()) {
            out(Color.RED, 'enter a test index between 0 and ' + interactiveTests.testFilePaths.size())
            return
        }

        testsSelection.testFilePath = interactiveTests.testFilePathByIdx(idx)
        displayScenarios(testsSelection.testFilePath)
    }

    static selectScenario(Integer idx) {
        if (idx < 0 || idx >= availableScenarios.size()) {
            out(Color.RED, 'enter a scenario index between 0 and ' + availableScenarios.size())
            return
        }

        testsSelection.scenarios = [availableScenarios[idx].scenario]
        runSelected()
    }

    static s(Integer idx) {
        select(idx)
    }

    static select(String regexp) {
        if (testSelected) {
            selectScenario(regexp)
        } else {
            selectTest(regexp)
        }
    }

    static selectTest(String regexp) {
        def pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE)
        def found = interactiveTests.firstTestFilePathByRegexp(pattern)
        if (!found) {
            out(Color.RED, "didn't find a test matching regexp: " + regexp)
            return
        }

        testsSelection.testFilePath = found
        displayScenarios(testsSelection.testFilePath)

        null
    }

    static selectScenario(String regexp) {
        def pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE)
        def found = availableScenarios.find { pattern.matcher(it.scenario) }
        if (!found) {
            out(Color.RED, "didn't find a scenario matching regexp: " + regexp)
            return
        }

        testsSelection.scenarios = [found.scenario]
        runSelected()

        null
    }

    static s(String regexp) {
        select(regexp)
    }

    static getRun() {
        runSelected()
    }

    static getR() {
        getRun()
    }

    static run(Integer idx) {
        if (testSelected) {
            selectScenario(idx)
        } else {
            selectTest(idx)
            runSelected()
        }
    }

    static r(Integer idx) {
        run(idx)
    }

    static run(String regexp) {
        if (testSelected) {
            selectScenario(regexp)
        } else {
            selectTest(regexp)
            runSelected()
        }
    }

    static r(String regexp) {
        run(regexp)
    }

    static getList() {
        listTestFilesOrScenarios()
    }

    static getLs() {
        getList()
    }

    static getBack() {
        testsSelection.testFilePath = null
        testsSelection.scenarios = null
        displayTestFiles()
    }

    static getB() {
        getBack()
    }

    static runSelected() {
        if (!testsSelection.testFilePath) {
            out(Color.RED, 'no test file selected to run')
            listTestFilesOrScenarios()
            return
        }

        def allTests = interactiveTests.refreshScenarios(testsSelection.testFilePath)

        if (!testsSelection.scenarios) {
            runTests(allTests)
            return
        }

        def tests = interactiveTests.findSelectedTests(testsSelection)

        if (tests.size() != testsSelection.scenarios.size()) {
            out(Color.RED, 'Not all selected scenarios found "' + testsSelection.scenarios + '"')
            listTestFilesOrScenarios()
            return
        }

        runTests(tests)
    }

    static private void runTests(List<StandaloneTest> tests) {
        DocumentationArtifacts.clearRegisteredNames()

        interactiveTests.runner.resetAndWithListeners {
            tests.each { test ->
                displaySelectedScenarios('running', test.scenario)
                interactiveTests.runner.runTestAndNotifyListeners(test)

                def exception = test.exception
                if (exception) {
                    out(Color.RED, 'failed test: ',
                            Color.PURPLE, testsSelection.testFilePath, ' ',
                            Color.YELLOW, test.scenario)
                    out(Color.RED, StackTraceUtils.renderStackTraceWithoutLibCalls(exception))
                }
            }
        }
    }

    static private void listTestFilesOrScenarios() {
        if (!testSelected) {
            displayTestFiles()
        } else {
            displayScenarios(testsSelection.testFilePath)
        }
    }

    static private void displayTestFiles() {
        out(Color.BLUE, 'Test files:')

        if (interactiveTests.testFilePaths.isEmpty()) {
            out(Color.YELLOW, "[no test files specified]")
        }

        interactiveTests.testFilePaths.eachWithIndex { path, idx ->
            out(Color.YELLOW, idx, Color.PURPLE, ' ', path)
        }
    }

    static private void displayScenarios(String filePath) {
        out(Color.BLUE, 'Test scenarios of ', Color.PURPLE, filePath, Color.BLUE, ':')

        availableScenarios = interactiveTests.refreshScenarios(filePath)

        availableScenarios.eachWithIndex { test, idx ->
            out(Color.YELLOW, idx, Color.PURPLE, ' ', test.scenario)
        }
    }

    static private void displaySelectedScenarios(String prefix) {
        displaySelectedScenarios(prefix, testsSelection.scenarios.join(', '))
    }

    static private void displaySelectedScenarios(String prefix, String scenario) {
        out(Color.BLUE, prefix + ': ',
                Color.PURPLE, testsSelection.testFilePath, ' ',
                Color.YELLOW, scenario)
    }
}
