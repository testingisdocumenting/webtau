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
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Function
import java.util.stream.Stream

class StandaloneTestRunner {
    private List<StandaloneTest> tests
    private List<StandaloneTest> exclusiveTests

    private Path workingDir
    private Path currentTestPath
    private GroovyScriptEngine groovy

    private AtomicBoolean isTerminated

    private ThreadLocal<TestRunCondition> runCondition = ThreadLocal.<TestRunCondition>withInitial { ->
        new TestRunCondition(isConditionMet: true) }

    StandaloneTestRunner(GroovyScriptEngine groovy, Path workingDir) {
        this.workingDir = workingDir.toAbsolutePath()
        this.tests = []
        this.exclusiveTests = []
        this.groovy = groovy
        this.isTerminated = new AtomicBoolean(false)
    }

    void process(Path scriptPath, delegate) {
        currentTestPath = scriptPath.isAbsolute() ? scriptPath : workingDir.resolve(scriptPath)

        def script = groovy.createScript((workingDir.relativize(currentTestPath)).toString(), new Binding())

        script.setDelegate(delegate)
        script.setProperty("scenario", this.&scenario)
        script.setProperty("dscenario", this.&dscenario)
        script.setProperty("sscenario", this.&sscenario)
        script.setProperty("onlyWhen", this.&onlyWhen)

        StandaloneTestListeners.beforeScriptParse(scriptPath)
        script.run()
    }

    void clearRegisteredTests() {
        tests = []
        exclusiveTests = []
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
        handleDisabledByCondition(description, code) { test ->
            tests.add(test)
        }
    }

    void sscenario(String description, Closure code) {
        handleDisabledByCondition(description, code) { test ->
            exclusiveTests.add(test)
        }
    }

    void dscenario(String description, Closure code) {
        dscenario(description, "disabled with dscenario", code)
    }

    void dscenario(String description, String reason, Closure code) {
        def test = new StandaloneTest(workingDir, currentTestPath, description, code)
        test.disable(reason)
        tests.add(test)
    }

    void onlyWhen(String skipReason, Closure condition, Closure registrationCode) {
        def registerTests = registrationCode.clone() as Closure

        def isConditionMet = condition()

        def previousRunCondition = runCondition.get()
        if (!isConditionMet) {
            runCondition.set(new TestRunCondition(skipReason: skipReason, isConditionMet: false))
        }

        try {
            registerTests()
        } finally {
            runCondition.set(previousRunCondition)
        }
    }

    void runTests() {
        runTestsFromStream { testsByFile -> testsByFile.entrySet().stream() }
    }

    void runTestsInParallel(int maxNumberOfThreads) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(maxNumberOfThreads)
        forkJoinPool.submit {
            runTestsFromStream { testsByFile ->
                testsByFile.entrySet().stream().parallel()
            }
        }.get()
    }

    private void runTestsFromStream(Function<Map, Stream> streamCreator) {
        StandaloneTestListeners.beforeFirstTest()

        def testsToRun = exclusiveTests.isEmpty() ? tests : exclusiveTests
        def testsToSkip = exclusiveTests.isEmpty() ? [] : tests

        testsToSkip.each { test ->
            StandaloneTestListeners.beforeTestRun(test)
            StandaloneTestListeners.afterTestRun(test)
        }

        def testsByFile = testsToRun.groupBy { it.filePath }

        def stream = streamCreator.apply(testsByFile)
        stream.forEach { entry ->
            Collection<StandaloneTest> tests = entry.value
            tests.forEach { test ->
                StandaloneTestListeners.beforeTestRun(test)
                runTestIfNotTerminated(test)
                StandaloneTestListeners.afterTestRun(test)
            }
        }

        StandaloneTestListeners.afterAllTests()
    }

    private void runTestIfNotTerminated(StandaloneTest test) {
        if (! isTerminated.get()) {
            test.run()
        }

        if (test.reportTestEntry.exception instanceof TestsRunTerminateException) {
            isTerminated.set(true)
        }
    }

    private void handleDisabledByCondition(String scenarioDescription, Closure scenarioCode, Closure registrationCode) {
        def runCondition = runCondition.get()
        if (runCondition.isConditionMet) {
            def test = new StandaloneTest(workingDir, currentTestPath, scenarioDescription, scenarioCode)
            registrationCode(test)
        } else {
            dscenario(scenarioDescription, runCondition.skipReason, scenarioCode)
        }
    }

    private static class TestRunCondition {
        String skipReason
        boolean isConditionMet
    }

    private class SkipTestsDelegate {
        private String skipReason

        SkipTestsDelegate(String skipReason) {
            this.skipReason = skipReason
        }

        void scenario(String description, Closure code) {
            StandaloneTestRunner.this.dscenario(description, skipReason, code)
        }

        void sscenario(String description, Closure code) {
            StandaloneTestRunner.this.dscenario(description, skipReason, code)
        }
    }
}
