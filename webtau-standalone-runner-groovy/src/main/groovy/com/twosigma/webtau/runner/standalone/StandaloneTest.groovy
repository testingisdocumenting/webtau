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

import com.twosigma.webtau.report.ReportTestEntry
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

    private final ReportTestEntry reportTestEntry

    private final Path workingDir
    private final Closure code

    StandaloneTest(Path workingDir, Path filePath, String description, Closure code) {
        this.reportTestEntry = new ReportTestEntry(workingDir)
        this.reportTestEntry.setId(idGenerator.generate(filePath))
        this.reportTestEntry.setScenario(description)
        this.reportTestEntry.setFilePath(workingDir.relativize(filePath))
        this.workingDir = workingDir
        this.code = code
    }

    ReportTestEntry getReportTestEntry() {
        return reportTestEntry
    }

    Path getFilePath() {
        return reportTestEntry.filePath
    }

    String getScenario() {
        return reportTestEntry.scenario
    }

    boolean hasError() {
        return reportTestEntry.hasError()
    }

    boolean isSkipped() {
        return reportTestEntry.isSkipped()
    }

    boolean isFailed() {
        return reportTestEntry.isFailed()
    }

    List<TestStep<?, ?>> getSteps() {
        return reportTestEntry.steps
    }

    Throwable getException() {
        return reportTestEntry.exception
    }

    String getAssertionMessage() {
        return reportTestEntry.assertionMessage
    }

    void addResultPayload(TestResultPayload payload) {
        reportTestEntry.addTestResultPayload(payload)
    }

    void run() {
        StepReporters.withAdditionalReporter(this) {
            try {
                code.run()
            } catch (Throwable e) {
                reportTestEntry.setException(e)
            } finally {
                reportTestEntry.setRan(true)
            }
        }
    }

    @Override
    void onStepStart(TestStep step) {
        if (step.getNumberOfParents() == 0) {
            reportTestEntry.addStep(step)
        }
    }

    @Override
    void onStepSuccess(TestStep step) {
    }

    @Override
    void onStepFailure(TestStep step) {
    }
}
