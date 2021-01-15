/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.runner.standalone

import org.testingisdocumenting.webtau.reporter.TestListeners
import org.testingisdocumenting.webtau.reporter.WebTauTest
import org.testingisdocumenting.webtau.reporter.StepReporter
import org.testingisdocumenting.webtau.reporter.StepReporters
import org.testingisdocumenting.webtau.reporter.TestResultPayload
import org.testingisdocumenting.webtau.reporter.WebTauStep

import java.nio.file.Path

/**
 * Most of the runner API can be used outside standard JUnit/TestNG setup.
 * One way is to define a simple script. TODO refer example here
 */
class StandaloneTest implements StepReporter {
    private static StandaloneTestIdGenerator idGenerator = new StandaloneTestIdGenerator()

    private final WebTauTest test

    private final Path workingDir
    private final Closure code

    StandaloneTest(Path workingDir, Path filePath, String shortContainerId, String description, Closure code) {
        this.test = new WebTauTest(workingDir)
        this.test.setId(idGenerator.generate(filePath))
        this.test.setScenario(description)
        this.test.setFilePath(workingDir.relativize(filePath))
        this.test.setShortContainerId(shortContainerId)

        this.workingDir = workingDir
        this.code = code
    }

    WebTauTest getTest() {
        return test
    }

    Path getFilePath() {
        return test.filePath
    }

    String getScenario() {
        return test.scenario
    }

    String getShortContainerId() {
        return test.shortContainerId
    }

    String getDisableReason() {
        return test.disableReason
    }

    boolean hasError() {
        return test.isErrored()
    }

    boolean isSkipped() {
        return test.isSkipped()
    }

    boolean isFailed() {
        return test.isFailed()
    }

    boolean isSucceeded() {
        return test.isSucceeded()
    }

    List<WebTauStep> getSteps() {
        return test.steps
    }

    boolean hasSteps() {
        return !test.steps.isEmpty()
    }

    Throwable getException() {
        return test.exception
    }

    String getAssertionMessage() {
        return test.assertionMessage
    }

    void runIfNotRan() {
        if (test.ran) {
            return
        }

        run()
    }

    void run() {
        test.clear()
        if (test.isDisabled()) {
            return
        }

        StepReporters.withAdditionalReporter(this) {
            try {
                test.startClock()

                TestListeners.beforeFirstTestStatement(test)
                code.run()
                TestListeners.afterLastTestStatement(test)
            } catch (Throwable e) {
                test.setException(e)
            } finally {
                test.stopClock()
                test.setRan(true)
            }
        }
    }

    void disable(String reason) {
        test.disable(reason)
    }

    void addResultPayload(TestResultPayload payload) {
        test.addTestResultPayload(payload)
    }

    @Override
    void onStepStart(WebTauStep step) {
    }

    @Override
    void onStepSuccess(WebTauStep step) {
        addStepIfNoParent(step)
    }

    @Override
    void onStepFailure(WebTauStep step) {
        addStepIfNoParent(step)
    }

    private void addStepIfNoParent(WebTauStep step) {
        if (step.getNumberOfParents() == 0) {
            test.addStep(step)
        }
    }

    @Override
    String toString() {
        return "StandaloneTest{" +
                "test=" + test +
                ", workingDir=" + workingDir +
                '}'
    }
}
