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

package com.twosigma.webtau.runner.standalone

import com.twosigma.webtau.TestFile
import com.twosigma.webtau.reporter.StepReporters
import com.twosigma.webtau.reporter.TestListeners
import com.twosigma.webtau.reporter.WebTauReport
import com.twosigma.webtau.reporter.WebTauTestList
import com.twosigma.webtau.reporter.WebTauTestMeta
import com.twosigma.webtau.time.Time

import java.nio.file.Path
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Function
import java.util.stream.Stream

class StandaloneTestRunner {
    private RegisteredTests registeredTests
    private Path workingDir
    private Path currentTestPath

    private final ThreadLocal<WebTauTestMeta> currentTestMeta = new ThreadLocal<>()

    private String currentShortContainerId
    private GroovyScriptEngine groovy

    private AtomicBoolean isTerminated

    private long startTime
    private long endTime
    private WebTauTestList webTauTestList

    private ThreadLocal<TestRunCondition> runCondition = ThreadLocal.<TestRunCondition> withInitial { ->
        new TestRunCondition(isConditionMet: true)
    }

    StandaloneTestRunner(GroovyScriptEngine groovy, Path workingDir) {
        this.workingDir = workingDir.toAbsolutePath()
        this.registeredTests = new RegisteredTests()
        this.groovy = groovy
        this.isTerminated = new AtomicBoolean(false)
    }

    void process(TestFile testFile, delegate) {
        Path scriptPath = testFile.path
        currentTestPath = scriptPath.isAbsolute() ? scriptPath : workingDir.resolve(scriptPath)
        currentTestMeta.set(new WebTauTestMeta())
        currentShortContainerId = testFile.shortContainerId

        def relativeToWorkDirPath = workingDir.relativize(currentTestPath)

        def scriptParse = new StandaloneTest(workingDir, currentTestPath, currentShortContainerId, "parse/init", { ->
            def script = groovy.createScript(relativeToWorkDirPath.toString(), new Binding())

            script.setDelegate(delegate)
            script.setProperty("scenario", this.&scenario)
            script.setProperty("dscenario", this.&dscenario)
            script.setProperty("sscenario", this.&sscenario)
            script.setProperty("onlyWhen", this.&onlyWhen)

            StepReporters.withAdditionalReporter(new ForbidStepsOutsideScenarioStepListener()) {
                script.run()
            }
        })

        scriptParse.run()
        if (scriptParse.hasError()) {
            scriptParse.test.meta.add(currentTestMeta.get())
            registeredTests.add(scriptParse)
        }
    }

    def attachTestMetaValue(String key, Object value) {
        def testMeta = currentTestMeta.get()
        if (testMeta == null) {
            throw new IllegalStateException("current test meta must be already set")
        }

        return testMeta.add(key, value)
    }

    void clearRegisteredTests() {
        registeredTests.clear()
    }

    boolean hasExclusiveTests() {
        return registeredTests.hasExclusiveTests()
    }

    List<StandaloneTest> getTests() {
        return registeredTests.tests
    }

    void scenario(String description, Closure code) {
        handleDisabledByCondition(description, code) { test ->
            registeredTests.add(test)
        }
    }

    void sscenario(String description, Closure code) {
        handleDisabledByCondition(description, code) { test ->
            registeredTests.addExclusive(test)
        }
    }

    void dscenario(String description, Closure code) {
        dscenario(description, "disabled with dscenario", code)
    }

    void dscenario(String description, String reason, Closure code) {
        def test = new StandaloneTest(workingDir, currentTestPath, currentShortContainerId, description, code)
        test.disable(reason)
        registeredTests.add(test)
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

    int numThreadsToUse(int maxNumberOfThreads) {
        int numTestsToRun = registeredTests.testsByFile().size()
        return maxNumberOfThreads == -1 ?
            numTestsToRun :
            Math.min(maxNumberOfThreads, numTestsToRun)
    }

    void runTestsInParallel(int maxNumberOfThreads) {
        int numThreads = numThreadsToUse(maxNumberOfThreads)
        ForkJoinPool forkJoinPool = new ForkJoinPool(numThreads)
        forkJoinPool.submit {
            runTestsFromStream { testsByFile ->
                testsByFile.entrySet().stream().parallel()
            }
        }.get()
    }

    void resetAndWithListeners(Closure codeToRunTests) {
        isTerminated.set(false)
        try {
            startTime = Time.currentTimeMillis()
            webTauTestList = new WebTauTestList()

            TestListeners.beforeFirstTest()
            codeToRunTests()
        } finally {
            endTime = Time.currentTimeMillis()
            def report = new WebTauReport(webTauTestList, startTime, endTime)

            TestListeners.afterAllTests(report)
        }
    }

    private void runTestsFromStream(Function<Map, Stream> streamCreator) {
        resetAndWithListeners {
            def testsToSkip = registeredTests.testsToSkip()

            testsToSkip.each { standaloneTest ->
                handleTestAndNotifyListeners(standaloneTest) {}
            }

            def stream = streamCreator.apply(registeredTests.testsByFile())
            stream.forEach { entry ->
                Collection<StandaloneTest> tests = entry.value
                tests.forEach { test ->
                    runTestAndNotifyListeners(test)
                }
            }
        }
    }

    void runTestAndNotifyListeners(StandaloneTest standaloneTest) {
        handleTestAndNotifyListeners(standaloneTest) {
            runTestIfNotTerminated(it)
        }
    }

    void handleTestAndNotifyListeners(StandaloneTest standaloneTest, Closure testHandler) {
        TestListeners.beforeTestRun(standaloneTest.test)

        testHandler(standaloneTest)
        webTauTestList.add(standaloneTest.test)

        TestListeners.afterTestRun(standaloneTest.test)
    }

    private void runTestIfNotTerminated(StandaloneTest standaloneTest) {
        if (!isTerminated.get()) {
            currentTestMeta.set(standaloneTest.test.meta)
            standaloneTest.run()
        }

        if (standaloneTest.test.exception instanceof TestsRunTerminateException) {
            isTerminated.set(true)
        }
    }

    private void handleDisabledByCondition(String scenarioDescription, Closure scenarioCode, Closure registrationCode) {
        def runCondition = runCondition.get()
        if (runCondition.isConditionMet) {
            def test = new StandaloneTest(workingDir, currentTestPath, currentShortContainerId, scenarioDescription, scenarioCode)
            test.test.meta.add(currentTestMeta.get())

            registrationCode(test)
        } else {
            dscenario(scenarioDescription, runCondition.skipReason, scenarioCode)
        }
    }

    private static class TestRunCondition {
        String skipReason
        boolean isConditionMet
    }
}
