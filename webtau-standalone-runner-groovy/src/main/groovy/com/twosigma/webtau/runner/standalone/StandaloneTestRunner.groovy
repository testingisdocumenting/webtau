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

package com.twosigma.webtau.runner.standalone

import java.nio.file.Path

class StandaloneTestRunner {
    private List<StandaloneTest> tests
    private List<StandaloneTest> exclusiveTests

    private Path workingDir
    private Path currentTestPath
    private GroovyScriptEngine groovy

    StandaloneTestRunner(GroovyScriptEngine groovy, Path workingDir) {
        this.workingDir = workingDir.toAbsolutePath()
        this.tests = []
        this.exclusiveTests = []
        this.groovy = groovy
    }

    void process(Path scriptPath, delegate) {
        currentTestPath = scriptPath.isAbsolute() ? scriptPath : workingDir.resolve(scriptPath)

        def script = groovy.createScript((workingDir.relativize(currentTestPath)).toString(), new Binding())

        script.setDelegate(delegate)
        script.setProperty("scenario", this.&scenario)

        StandaloneTestListeners.beforeScriptParse(scriptPath)
        script.run()
    }

    List<StandaloneTest> getTests() {
        return tests
    }

    int getNumberOfPassed() {
        return tests.count { it.isPassed() }
    }

    int getNumberOfFailed() {
        return tests.count { it.isFailed() }
    }

    int getNumberOfErrored() {
        return tests.count { it.hasError() }
    }

    void scenario(String description, Closure code) {
        def test = new StandaloneTest(workingDir, currentTestPath, description, code)
        tests.add(test)
    }

    void sscenario(String description, Closure code) {
        def test = new StandaloneTest(workingDir, currentTestPath, description, code)
        exclusiveTests.add(test)
    }

    void runTests() {
        StandaloneTestListeners.beforeFirstTest()

        def testsToRun = exclusiveTests.isEmpty() ? tests : exclusiveTests
        def testsToSkip = exclusiveTests.isEmpty() ? [] : tests

        testsToSkip.each { test ->
            StandaloneTestListeners.beforeTestRun(test)
            StandaloneTestListeners.afterTestRun(test)
        }

        testsToRun.each { test ->
            StandaloneTestListeners.beforeTestRun(test)
            test.run()
            StandaloneTestListeners.afterTestRun(test)
        }

        StandaloneTestListeners.afterAllTests()
    }
}
