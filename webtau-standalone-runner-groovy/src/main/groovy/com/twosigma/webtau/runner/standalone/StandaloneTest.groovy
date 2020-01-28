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

import com.twosigma.webtau.reporter.TestListeners
import com.twosigma.webtau.reporter.WebTauTest
import com.twosigma.webtau.reporter.StepReporter
import com.twosigma.webtau.reporter.StepReporters
import com.twosigma.webtau.reporter.TestResultPayload
import com.twosigma.webtau.reporter.TestStep

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

    List<TestStep<?, ?>> getSteps() {
        return test.steps
    }

    Throwable getException() {
        return test.exception
    }

    String getAssertionMessage() {
        return test.assertionMessage
    }

    void run() {
        test.clear()
        if (test.isDisabled()) {
            return
        }

        StepReporters.withAdditionalReporter(this) {
            try {
                test.startClock()
                if (!test.isDisabled()) {
                    TestListeners.beforeFirstTestStatement(test)
                    code.run()
                    TestListeners.afterLastTestStatement(test)
                }
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
    void onStepStart(TestStep step) {
        if (step.getNumberOfParents() == 0) {
            test.addStep(step)
        }
    }

    @Override
    void onStepSuccess(TestStep step) {
    }

    @Override
    void onStepFailure(TestStep step) {
    }
}
