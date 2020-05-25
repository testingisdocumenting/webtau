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

public class ProcessBackgroundRunResult {
    private final Process process;
    private final StreamGobbler outputGobbler;
    private final StreamGobbler errorGobbler;

    private final Thread consumeErrorThread;
    private final Thread consumeOutThread;

    public ProcessBackgroundRunResult(Process process,
                                      StreamGobbler outputGobbler,
                                      StreamGobbler errorGobbler,
                                      Thread consumeErrorThread,
                                      Thread consumeOutThread) {
        this.process = process;
        this.outputGobbler = outputGobbler;
        this.errorGobbler = errorGobbler;
        this.consumeErrorThread = consumeErrorThread;
        this.consumeOutThread = consumeOutThread;
    }

    public Process getProcess() {
        return process;
    }

    public void destroy() {
        outputGobbler.close();
        errorGobbler.close();
        process.destroy();
    }

    public StreamGobbler getOutputGobbler() {
        return outputGobbler;
    }

    public StreamGobbler getErrorGobbler() {
        return errorGobbler;
    }

    public Thread getConsumeErrorThread() {
        return consumeErrorThread;
    }

    public Thread getConsumeOutThread() {
        return consumeOutThread;
    }

    public ProcessRunResult createRunResult() {
        return new ProcessRunResult(process.exitValue(),
                outputGobbler.getLines(),
                errorGobbler.getLines(),
                outputGobbler.getException(),
                errorGobbler.getException());
    }
}
