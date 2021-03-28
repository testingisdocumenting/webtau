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
import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.data.render.PrettyPrintable
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

    @PackageScope
    static TestsSelection testsSelection = new TestsSelection()

    @PackageScope
    static List<StandaloneTest> availableScenarios = []

    static WebTauConfig getReloadConfig() {
        return reloadConfig()
    }

    static WebTauConfig reloadConfig() {
        WebTauConfig.resetConfigHandlers()
        WebTauConfig.getCfg().reset()
        WebTauConfig.getCfg().triggerConfigHandlers()

        return WebTauConfig.cfg
    }

    static boolean getTestFileSelected() {
        return testsSelection.testFilePath != null
    }

    static void select(Object... selectors) {
        if (testFileSelected) {
            selectScenariosAndValidate(selectors)
            displaySelectedScenarios()
        } else {
            if (selectors.size() != 1) {
                out(Color.RED, 'only one test file can be selected at one time, received: ' + selectors)
                return
            }

            selectTestFile(selectors[0])
        }
    }

    static void s(Object... selectors) {
        select(selectors)
    }

    static Object getRun() {
        return new ProxyToHandleNegativeIndexAndRunScenarios()
    }

    static Object getR() {
        return getRun()
    }

    static Object getSelect() {
        return new ProxyToHandleNegativeIndexAndDisplayScenarios()
    }

    static Object getS() {
        return getSelect()
    }

    static void run(Object... selectors) {
        if (testFileSelected) {
            selectScenariosAndValidate(selectors)

            if (testsSelection.scenarios) {
                runSelected()
            }
        } else {
            if (selectors.size() != 1) {
                out(Color.RED, 'only one test file can be selected at one time, received: ' + selectors)
                return
            }

            selectTestFile(selectors[0])

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
        if (!testFileSelected) {
            displayTestFiles()
        } else {
            displayScenarios(testsSelection.testFilePath)
        }
    }

    private static void selectTestFile(Object selector) {
        if (selector instanceof String) {
            selectTestFileByRegexp(selector)
            return
        }

        if (selector instanceof Integer) {
            selectTestFileByIndex(selector)
            return
        }

        out(Color.RED, "can't select test file using: " + selector)
        testsSelection.testFilePath = ''
        testsSelection.scenarios = []
    }

    private static void selectTestFileByIndex(Integer idx) {
        def size = interactiveTests.testFilePaths.size()
        idx = IndexSelection.convertNegativeIdxToAbsolute(size, idx)
        if (idx < 0 || idx >= size) {
            out(Color.RED, 'enter a test file index between 0 and ' + (size - 1))
            return
        }

        testsSelection.testFilePath = interactiveTests.testFilePathByIdx(idx)
        displayScenarios(testsSelection.testFilePath)
    }

    private static void selectTestFileByRegexp(String regexp) {
        def pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE)
        def found = interactiveTests.firstTestFilePathByRegexp(pattern)
        if (!found) {
            out(Color.RED, "didn't find a test file matching regexp: " + regexp)
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
                out(Color.RED, "didn't find a scenario matching: " + it.input)
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

    // <s 1> is treated as method "s" call with parameter <1>
    // <s -1> is treated as "getS() - 1" so we need to handle this in a way to make it feel like <s 1>
    // <s> still needs to display selected scenarios, so when <s> without minus index is called we need to render
    private static class ProxyToHandleNegativeIndexAndDisplayScenarios implements PrettyPrintable {
        Object minus(Integer indexToMakeNegative) {
            select(indexToMakeNegative * -1)
            return null
        }

        @Override
        void prettyPrint(ConsoleOutput console) {
            displaySelectedScenarios()
        }
    }

    private static class ProxyToHandleNegativeIndexAndRunScenarios implements PrettyPrintable {
        Object minus(Integer indexToMakeNegative) {
            run(indexToMakeNegative * -1)
            return null
        }

        @Override
        void prettyPrint(ConsoleOutput console) {
            runSelected()
        }
    }
}
