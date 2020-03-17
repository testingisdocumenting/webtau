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

package org.testingisdocumenting.webtau.reporter;

import org.testingisdocumenting.webtau.reporter.stacktrace.StackTraceCodeEntry;
import org.testingisdocumenting.webtau.reporter.stacktrace.StackTraceUtils;
import org.testingisdocumenting.webtau.time.Time;
import org.testingisdocumenting.webtau.utils.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static org.testingisdocumenting.webtau.reporter.TestStatus.*;

public class WebTauTest {
    private String id;
    private String scenario;

    private Path filePath;
    private String className;
    private String shortContainerId;

    private Throwable exception;

    private final List<TestResultPayload> payloads;
    private final List<TestStep<?, ?>> steps;

    private boolean isDisabled;
    private String disableReason;

    private boolean isRan;
    private Path workingDir;

    private long startTime;
    private long elapsedTime;

    private final WebTauTestMetadata metadata;

    public WebTauTest(Path workingDir) {
        this.workingDir = workingDir;
        payloads = new ArrayList<>();
        steps = new ArrayList<>();
        metadata = new WebTauTestMetadata();
    }

    public void clear() {
        isRan = false;
        startTime = 0;
        elapsedTime = 0;

        exception = null;

        steps.clear();
        payloads.clear();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public Path getFilePath() {
        return filePath;
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public String getClassName() {
        return className;
    }

    public void setShortContainerId(String shortContainerId) {
        this.shortContainerId = shortContainerId;
    }

    public String getShortContainerId() {
        return shortContainerId;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void disable(String reason) {
        disableReason = reason;
        isDisabled = true;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public String getDisableReason() {
        return disableReason;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public void setExceptionIfNotSet(Throwable exception) {
        if (this.exception == null) {
            setException(exception);
        }
    }

    public void setRan(boolean ran) {
        isRan = ran;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public void startClock() {
        startTime = Time.currentTimeMillis();
    }

    public void stopClock() {
        elapsedTime = Time.currentTimeMillis() - startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public String getAssertionMessage() {
        if (!(exception instanceof AssertionError)) {
            return null;
        }

        return exception.getMessage();
    }

    public boolean isErrored() {
        return exception != null && !isFailed();
    }

    public boolean isSkipped() {
        return ! isRan;
    }

    public boolean isFailed() {
        return exception instanceof AssertionError;
    }

    public TestStatus getTestStatus() {
        if (isFailed()) {
            return Failed;
        }

        if (isErrored()) {
            return Errored;
        }

        if (isSkipped()) {
            return Skipped;
        }

        return Passed;
    }

    public List<TestResultPayload> getPayloads() {
        return payloads;
    }

    public List<TestStep<?, ?>> getSteps() {
        return steps;
    }


    public WebTauTestMetadata getMetadata() {
        return metadata;
    }

    public void addStep(TestStep<?, ?> step) {
        steps.add(step);
    }

    public void addTestResultPayload(TestResultPayload testResultPayload) {
        payloads.add(testResultPayload);
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("id", id);
        result.put("scenario", scenario);
        result.put("status", getTestStatus().toString());
        result.put("startTime", startTime);
        result.put("elapsedTime", elapsedTime);

        if (filePath !=null) {
            result.put("fileName", filePath.toString());
        }

        if (className != null) {
            result.put("className", className);
        }

        if (shortContainerId != null) {
            result.put("shortContainerId", shortContainerId);
        }

        result.put("disabled", isDisabled);

        if (isDisabled) {
            result.put("disableReason", disableReason);
        }

        if (exception != null) {
            result.put("assertion", getAssertionMessage());
            result.put("exceptionMessage", StackTraceUtils.fullCauseMessage(exception));
            result.put("failedCodeSnippets", extractFailedCodeSnippet(exception));
            result.put("fullStackTrace", StackTraceUtils.renderStackTrace(exception));
            result.put("shortStackTrace", StackTraceUtils.renderStackTraceWithoutLibCalls(exception));
        }

        payloads.forEach(p -> result.putAll(p.toMap()));

        result.put("metadata", metadata.toMap());

        return result;
    }

    private List<Map<String, ?>> extractFailedCodeSnippet(Throwable throwable) {
        List<StackTraceCodeEntry> entries = StackTraceUtils.extractLocalCodeEntries(throwable);
        return entries.stream()
                .filter(e -> Files.exists(workingDir.resolve(e.getFilePath())))
                .map(e -> {
                    Map<String, Object> entry = new LinkedHashMap<>();
                    entry.put("filePath", e.getFilePath());
                    entry.put("lineNumbers", e.getLineNumbers());
                    entry.put("snippet", FileUtils.fileTextContent(workingDir.resolve(e.getFilePath())));

                    return entry;
                }).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "WebTauTest{" +
                "id='" + id + '\'' +
                ", scenario='" + scenario + '\'' +
                ", shortContainerId='" + shortContainerId + '\'' +
                ", exception=" + exception +
                '}';
    }
}
