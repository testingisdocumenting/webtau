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

package org.testingisdocumenting.webtau.cli;

import java.io.IOException;
import java.util.List;

public class ProcessRunResult {
    private int exitCode;
    private List<String> output;
    private List<String> error;

    private IOException outputReadingException;
    private IOException errorReadingException;

    public ProcessRunResult(int exitCode,
                            List<String> output,
                            List<String> error,
                            IOException outputReadingException,
                            IOException errorReadingException) {
        this.exitCode = exitCode;
        this.output = output;
        this.error = error;
        this.outputReadingException = outputReadingException;
        this.errorReadingException = errorReadingException;
    }

    public IOException getOutputReadingException() {
        return outputReadingException;
    }

    public IOException getErrorReadingException() {
        return errorReadingException;
    }

    public int getExitCode() {
        return exitCode;
    }

    public List<String> getOutput() {
        return output;
    }

    public List<String> getError() {
        return error;
    }
}
