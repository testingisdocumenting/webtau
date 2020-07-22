/*
 * Copyright 2020 webtau maintainers
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
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg;

public class ProcessUtils {
    private static final String ENV_PATH_SEPARATOR = System.getProperty("path.separator");

    private ProcessUtils() {
    }

    public static ProcessRunResult run(String command, CliProcessConfig config) throws IOException {
        CliBackgroundProcess backgroundRunResult = runInBackground(command, config);

        try {
            backgroundRunResult.getProcess().waitFor();

            backgroundRunResult.getConsumeErrorThread().join();
            backgroundRunResult.getConsumeOutThread().join();

            return backgroundRunResult.createRunResult();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void kill(int pid) throws IOException {
        run("kill " + pid + " && pkill -TERM -P " + pid, CliProcessConfig.EMPTY);
    }

    public static CliBackgroundProcess runInBackground(String command, CliProcessConfig config) throws IOException {
        List<String> splitCommandWithPrefix = prefixCommandWithPathAndSplit(command);

        ProcessBuilder processBuilder = new ProcessBuilder(splitCommandWithPrefix);
        config.applyTo(processBuilder);

        Process process = processBuilder.start();

        StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream());
        StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream());

        Thread consumeErrorThread = new Thread(errorGobbler);
        Thread consumeOutThread = new Thread(outputGobbler);

        consumeErrorThread.start();
        consumeOutThread.start();

        return new CliBackgroundProcess(process, command,
                outputGobbler, errorGobbler,
                consumeOutThread, consumeErrorThread);
    }

    private static List<String> prefixCommandWithPathAndSplit(String command) {
        String commandPrefix = "";

        String additionalPath = String.join(ENV_PATH_SEPARATOR, envPathWithWorkingDirPrefix());
        if (!additionalPath.isEmpty()) {
            String existingPath = System.getenv("PATH");
            String newPath = additionalPath;
            if (existingPath != null && !existingPath.isEmpty()) {
                newPath += ENV_PATH_SEPARATOR + existingPath;
            }

            commandPrefix = "PATH=" + newPath;
        }

        // TODO implement windows equivalent with cmd.exe
        return Arrays.asList("/bin/sh", "-c", commandPrefix + " " + command);
    }

    private static List<String> envPathWithWorkingDirPrefix() {
        return getCfg().getEnvPath().stream().map(ProcessUtils::prefixWithWorkingDir).collect(Collectors.toList());
    }

    private static String prefixWithWorkingDir(String pathName) {
        if (Paths.get(pathName).isAbsolute()) {
            return pathName;
        }

        return getCfg().getWorkingDir().resolve(pathName).toAbsolutePath().toString();
    }
}
