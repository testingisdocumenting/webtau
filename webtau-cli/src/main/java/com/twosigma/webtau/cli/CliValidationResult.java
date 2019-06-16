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

package com.twosigma.webtau.cli;

import com.twosigma.webtau.cli.expectation.CliOutput;
import com.twosigma.webtau.reporter.TestStepPayload;

import java.util.*;

public class CliValidationResult implements TestStepPayload {
    private final String command;
    private final List<String> mismatches;

    private Integer exitCode;
    private CliOutput out;
    private CliOutput err;
    private long startTime;
    private long elapsedTime;

    private String errorMessage;

    public CliValidationResult(String command) {
        this.command = command;
        this.mismatches = new ArrayList<>();
    }

    public String getCommand() {
        return command;
    }

    public Integer getExitCode() {
        return exitCode;
    }

    public void setExitCode(Integer exitCode) {
        this.exitCode = exitCode;
    }

    public CliOutput getOut() {
        return out;
    }

    public void setOut(CliOutput out) {
        this.out = out;
    }

    public CliOutput getErr() {
        return err;
    }

    public void setErr(CliOutput err) {
        this.err = err;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void addMismatch(String message) {
        mismatches.add(message);
    }

    public List<String> getMismatches() {
        return mismatches;
    }

    @Override
    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("command", command);
        result.put("out", out != null ? out.get() : "");
        result.put("err", err != null ? err.get() : "");
        result.put("exitCode", exitCode);
        result.put("outMatches", out != null ? out.extractMatchedLines() : Collections.emptyList());
        result.put("errMatches", err != null ? err.extractMatchedLines() : Collections.emptyList());
        result.put("startTime", startTime);
        result.put("elapsedTime", elapsedTime);
        result.put("mismatches", mismatches);
        result.put("errorMessage", errorMessage);

        return result;
    }
}
