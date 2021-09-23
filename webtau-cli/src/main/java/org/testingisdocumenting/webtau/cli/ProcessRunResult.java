/*
 * Copyright 2020 webtau maintainers
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

public class ProcessRunResult {
    private final int exitCode;
    private final CliOutput output;
    private final CliOutput error;
    private final boolean isTimeOut;

    public ProcessRunResult(int exitCode, CliOutput output, CliOutput error, boolean isTimeOut) {
        this.exitCode = exitCode;
        this.output = output;
        this.error = error;
        this.isTimeOut = isTimeOut;
    }

    public IOException getOutputReadingException() {
        return output.getException();
    }

    public IOException getErrorReadingException() {
        return error.getException();
    }

    public int getExitCode() {
        return exitCode;
    }

    public CliOutput getOutput() {
        return output;
    }

    public CliOutput getError() {
        return error;
    }

    public boolean isTimeOut() {
        return isTimeOut;
    }
}
