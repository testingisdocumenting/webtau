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

import java.util.LinkedHashMap;
import java.util.Map;

public class CliValidationResult implements TestStepPayload {
    private final String command;
    private final Integer exitCode;
    private final CliOutput out;
    private final CliOutput err;

    public CliValidationResult(String command, Integer exitCode, CliOutput out, CliOutput err) {
        this.command = command;
        this.exitCode = exitCode;
        this.out = out;
        this.err = err;
    }

    public String getCommand() {
        return command;
    }

    public Integer getExitCode() {
        return exitCode;
    }

    public CliOutput getOut() {
        return out;
    }

    public CliOutput getErr() {
        return err;
    }

    @Override
    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        return result;
    }
}
