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

import com.twosigma.webtau.cli.parser.CommandParser;

import java.io.IOException;
import java.util.Map;

public class ProcessUtils {
    private ProcessUtils() {
    }

    public static ProcessRunResult run(String command, Map<String, String> env) {
        ProcessBuilder processBuilder = new ProcessBuilder(splitCommand(command));
        processBuilder.environment().putAll(env);

        try {
            Process process = processBuilder.start();

            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream());
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream());

            Thread consumeErrorThread = new Thread(errorGobbler);
            Thread consumeOutThread = new Thread(outputGobbler);

            consumeErrorThread.start();
            consumeOutThread.start();

            process.waitFor();

            consumeErrorThread.join();
            consumeOutThread.join();

            return new ProcessRunResult(process.exitValue(),
                    outputGobbler.getLines(),
                    errorGobbler.getLines(),
                    outputGobbler.getException(),
                    errorGobbler.getException());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static String[] splitCommand(String command) {
        return new CommandParser(command).parse();
    }
}
