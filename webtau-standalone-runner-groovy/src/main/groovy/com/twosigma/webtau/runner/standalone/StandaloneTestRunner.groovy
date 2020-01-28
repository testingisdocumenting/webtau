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
import com.twosigma.webtau.reporter.WebTauTestMetadata
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

    private final ThreadLocal<WebTauTestMetadata> currentTestMetadata = new ThreadLocal<>()

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
        currentTestMetadata.set(new WebTauTestMetadata())
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

        TestListeners.withDisabledListeners {
            scriptParse.run()
        }

        if (scriptParse.hasError()) {
            scriptParse.test.metadata.add(currentTestMetadata.get())
            registeredTests.add(scriptParse)
        }
    }

    def attachTestMetadata(Map<String, Object> meta) {
        def testMetadata = currentTestMetadata.get()
        if (testMetadata == null) {
            throw new IllegalStateException("current test meta must be already set")
        }

        return testMetadata.add(meta)
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

    void dscenario(String scenarioDescription, String reason, Closure scenarioCode) {
        def test = createTest(scenarioDescription, scenarioCode)
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
            currentTestMetadata.set(standaloneTest.test.metadata)
            standaloneTest.run()
        }

        if (standaloneTest.test.exception instanceof TestsRunTerminateException) {
            isTerminated.set(true)
        }
    }

    private void handleDisabledByCondition(String scenarioDescription, Closure scenarioCode, Closure registrationCode) {
        def runCondition = runCondition.get()
        if (runCondition.isConditionMet) {
            def test = createTest(scenarioDescription, scenarioCode)
            test.test.metadata.add(currentTestMetadata.get())

            registrationCode(test)
        } else {
            dscenario(scenarioDescription, runCondition.skipReason, scenarioCode)
        }
    }

    private StandaloneTest createTest(String scenarioDescription, Closure scenarioCode) {
        new StandaloneTest(workingDir, currentTestPath, currentShortContainerId, scenarioDescription, scenarioCode)
    }

    private static class TestRunCondition {
        String skipReason
        boolean isConditionMet
    }
}
