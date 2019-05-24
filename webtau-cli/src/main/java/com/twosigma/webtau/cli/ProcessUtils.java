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

import java.io.IOException;

public class ProcessUtils {
    private ProcessUtils() {
    }

    public static ProcessRunResult run(String[] command) {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            Process process = processBuilder.start();

            StreamGobbler outputGobbler = new StreamGobbler("output", process.getInputStream());
            StreamGobbler errorGobbler = new StreamGobbler("error", process.getErrorStream());

            Thread consumeErrorThread = new Thread(errorGobbler);
            Thread consumeOutThread = new Thread(outputGobbler);

            consumeErrorThread.start();
            consumeOutThread.start();

            process.waitFor();

            consumeErrorThread.join();
            consumeOutThread.join();

            return new ProcessRunResult(process.exitValue(), outputGobbler.getLines(), errorGobbler.getLines());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
