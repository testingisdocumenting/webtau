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

    static void select(Object... selectors) {
        if (testSelected) {
            selectScenariosAndValidate(selectors)
            displaySelectedScenarios()
        } else {
            if (selectors.size() != 1) {
                out(Color.RED, 'only one test file can be selected at one time, received: ' + selectors)
                return
            }

            selectTest(selectors[0])
        }
    }

    static void s(Object... selectors) {
        select(selectors)
    }

    static void getRun() {
        runSelected()
    }

    static void getR() {
        runSelected()
    }

    static void getSelect() {
        displaySelectedScenarios()
    }

    static void getS() {
        getSelect()
    }

    static void run(Object... selectors) {
        if (testSelected) {
            selectScenariosAndValidate(selectors)

            if (testsSelection.scenarios) {
                runSelected()
            }
        } else {
            if (selectors.size() != 1) {
                out(Color.RED, 'only one test file can be selected at one time, received: ' + selectors)
                return
            }

            selectTest(selectors[0])

            if (testsSelection.testFilePath) {
                runSelected()
            }
        }
    }

    static void r(Object... selectors) {
        run(selectors)
    }

    static void getList() {
        listTestFilesOrScenarios()
    }

    static void getLs() {
        getList()
    }

    static void getBack() {
        testsSelection.testFilePath = null
        testsSelection.scenarios = null
        displayTestFiles()
    }

    static void getB() {
        getBack()
    }

    private static void runSelected() {
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

    private static void runTests(List<StandaloneTest> tests) {
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

    private static void listTestFilesOrScenarios() {
        if (!testSelected) {
            displayTestFiles()
        } else {
            displayScenarios(testsSelection.testFilePath)
        }
    }

    private static void selectTest(Object selector) {
        if (selector instanceof String) {
            selectTestByRegexp(selector)
            return
        }

        if (selector instanceof Integer) {
            selectTestByIndex(selector)
            return
        }

        out(Color.RED, "can't select test using: " + selector)
        testsSelection.testFilePath = ''
        testsSelection.scenarios = []
    }

    private static void selectTestByIndex(Integer idx) {
        if (idx < 0 || idx >= interactiveTests.testFilePaths.size()) {
            out(Color.RED, 'enter a test index between 0 and ' + interactiveTests.testFilePaths.size())
            return
        }

        testsSelection.testFilePath = interactiveTests.testFilePathByIdx(idx)
        displayScenarios(testsSelection.testFilePath)
    }

    private static void selectTestByRegexp(String regexp) {
        def pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE)
        def found = interactiveTests.firstTestFilePathByRegexp(pattern)
        if (!found) {
            out(Color.RED, "didn't find a test matching regexp: " + regexp)
            return
        }

        testsSelection.testFilePath = found
        displayScenarios(testsSelection.testFilePath)
    }

    private static void selectScenariosAndValidate(Object... scenarios) {
        def selector = new ReplScenariosSelector(availableScenarios, scenarios)
        def withIssues = selector.inputAndResults.findAll {!it.isFound }

        if (!withIssues.isEmpty()) {
            testsSelection.scenarios = []

            withIssues.each {
                out(Color.RED, "didn't find a test matching: " + it.input)
            }
        } else {
            testsSelection.scenarios = selector.inputAndResults.scenario
        }
    }

    private static void displayTestFiles() {
        out(Color.BLUE, 'Test files:')

        if (interactiveTests.testFilePaths.isEmpty()) {
            out(Color.YELLOW, "[no test files specified]")
        }

        interactiveTests.testFilePaths.eachWithIndex { path, idx ->
            out(Color.YELLOW, idx, Color.PURPLE, ' ', path)
        }
    }

    private static void displayScenarios(String filePath) {
        out(Color.BLUE, 'Test scenarios of ', Color.PURPLE, filePath, Color.BLUE, ':')

        availableScenarios = interactiveTests.refreshScenarios(filePath)

        availableScenarios.eachWithIndex { test, idx ->
            out(Color.YELLOW, idx, Color.PURPLE, ' ', test.scenario)
        }
    }

    private static void displaySelectedScenarios() {
        if (!testsSelection.scenarios) {
            out(Color.YELLOW, '[no scenarios are selected]')
            return
        }

        out(Color.BLUE, 'Selected scenarios:')

        testsSelection.scenarios.each { scenario ->
            out('  ' + scenario)
        }
    }

    private static void displaySelectedScenarios(String prefix, String scenario) {
        out(Color.BLUE, prefix + ': ',
                Color.PURPLE, testsSelection.testFilePath, ' ',
                Color.YELLOW, scenario)
    }
}
