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

import com.twosigma.webtau.reporter.StepReporter
import com.twosigma.webtau.reporter.StepReporters
import com.twosigma.webtau.reporter.TestStep
import com.twosigma.webtau.runner.standalone.report.GroovyStackTraceUtils
import com.twosigma.webtau.utils.TraceUtils

import java.nio.file.Files
import java.nio.file.Path

import static com.twosigma.webtau.runner.standalone.StandaloneTestStatus.Errored
import static com.twosigma.webtau.runner.standalone.StandaloneTestStatus.Failed
import static com.twosigma.webtau.runner.standalone.StandaloneTestStatus.Passed
import static com.twosigma.webtau.runner.standalone.StandaloneTestStatus.Skipped

/**
 * Most of the runner API can be used outside standard JUnit/TestNG setup.
 * One way is to define a simple script. TODO refer example here
 */
class StandaloneTest implements StepReporter {
    private static StandaloneTestIdGenerator idGenerator = new StandaloneTestIdGenerator()

    private final String id
    private final Path workingDir
    private final Path filePath
    private final String description
    private final Closure code

    private Throwable exception
    private String assertionMessage

    private final List<StandaloneTestResultPayload> payloads
    private final List<TestStep> steps

    private boolean isRan

    StandaloneTest(Path workingDir, Path filePath, String description, Closure code) {
        this.workingDir = workingDir
        this.id = idGenerator.generate(filePath)
        this.filePath = filePath
        this.description = description
        this.isRan = false
        this.code = code
        this.steps = []
        this.payloads = []
    }

    List<TestStep> getSteps() {
        return steps
    }

    Path getFilePath() {
        return filePath
    }

    boolean isSkipped() {
        return ! isRan
    }

    boolean isPassed() {
        return !hasError() && !isFailed()
    }

    boolean hasError() {
        return exception != null && assertionMessage == null
    }

    boolean isFailed() {
        return assertionMessage != null
    }

    String getDescription() {
        return description
    }

    Throwable getException() {
        return exception
    }

    StandaloneTestStatus getStatus() {
        if (hasError()) {
            return Errored
        }

        if (isFailed()) {
            return Failed
        }

        if (isSkipped()) {
            return Skipped
        }

        return Passed
    }

    void addResultPayload(StandaloneTestResultPayload payload) {
        payloads.add(payload)
    }

    Map<String, ?> toMap() {
        def testAsMap = [id: id,
                         scenario: description,
                         fileName: filePath.fileName.toString(),
                         status: getStatus().toString(),
                         assertion: assertionMessage,
                         contextDescription: steps.find { it.isFailed() }?.firstAvailableContext?.toString(),
                         exceptionMessage: exception ? renderExceptionNameAndMessage(exception) : null,
                         failedCodeSnippets: exception ? extractFailedCodeSnippet(exception) : null,
                         shortStackTrace: exception ? GroovyStackTraceUtils.renderStackTraceWithoutLibCalls(exception) : null,
                         fullStackTrace: exception ? TraceUtils.stackTrace(exception) : null]

        payloads.each { testAsMap << it.toMap() }

        return testAsMap
    }

    void run() {
        try {
            StepReporters.add(this)
            code.run()
        } catch (AssertionError e) {
            exception = e
            assertionMessage = e.message
        } catch (Throwable e) {
            exception = e
        } finally {
            isRan = true
            StepReporters.remove(this)
        }
    }

    @Override
    void onStepStart(TestStep step) {
        if (step.getNumberOfParents() == 0) {
            steps.add(step)
        }
    }

    @Override
    void onStepSuccess(TestStep step) {
    }

    @Override
    void onStepFailure(TestStep step) {
    }

    private def extractFailedCodeSnippet(Throwable throwable) {
        def entries = GroovyStackTraceUtils.extractLocalCodeEntries(throwable)
        return entries.findAll {
            Files.exists(workingDir.resolve(it.filePath))
        }.collect { [
                filePath: it.filePath,
                lineNumbers: it.lineNumbers,
                snippet: workingDir.resolve(it.filePath).toFile().text ] }
    }

    private static String renderExceptionNameAndMessage(Throwable t) {
        return GroovyStackTraceUtils.fullCauseMessage(t)
    }
}
