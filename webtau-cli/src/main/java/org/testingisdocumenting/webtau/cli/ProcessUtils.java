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
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg;

class ProcessUtils {
    private ProcessUtils() {
    }

    static ProcessRunResult run(String command, CliProcessConfig config) throws IOException {
        CliBackgroundProcess backgroundRunResult = runInBackground(command, config);

        try {
            long timeoutMs = config.isTimeoutSpecified() ? config.getTimeoutMs() : CliConfig.getCliTimeoutMs();
            boolean onTime = backgroundRunResult.getProcess().waitFor(timeoutMs, TimeUnit.MILLISECONDS);

            if (!onTime) {
                backgroundRunResult.closeGlobbers();
            }

            backgroundRunResult.getConsumeErrorThread().join();
            backgroundRunResult.getConsumeOutThread().join();

            return backgroundRunResult.createRunResult(!onTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static void kill(int pid) {
        try {
            run("pkill -TERM -P " + pid, CliProcessConfig.EMPTY);
            run("kill " + pid, CliProcessConfig.SILENT);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static CliBackgroundProcess runInBackground(String command, CliProcessConfig config) throws IOException {
        String[] splitCommand = CommandParser.splitCommand(command);
        if (splitCommand.length == 0) {
            throw new IllegalArgumentException("command is not specified");
        }

        splitCommand[0] = findCommandIfRequiredUsingPath(splitCommand[0]);

        ProcessBuilder processBuilder = new ProcessBuilder(splitCommand);
        config.applyTo(processBuilder);

        Process process = processBuilder.start();

        StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), config.isSilent());
        StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), config.isSilent());

        Thread consumeErrorThread = new Thread(errorGobbler);
        Thread consumeOutThread = new Thread(outputGobbler);

        consumeErrorThread.start();
        consumeOutThread.start();

        return new CliBackgroundProcess(process, command,
                outputGobbler, errorGobbler,
                consumeOutThread, consumeErrorThread);
    }

    private static String findCommandIfRequiredUsingPath(String command) {
        List<Path> paths = cliPathWithWorkingDirPrefix();
        if (paths.isEmpty()) {
            return command;
        }

        return paths.stream()
                .map(p -> p.resolve(command))
                .filter(Files::exists)
                .map(Path::toString)
                .findFirst()
                .orElse(command);
    }

    private static List<Path> cliPathWithWorkingDirPrefix() {
        return CliConfig.getPath().stream()
                .map(ProcessUtils::prefixWithWorkingDir)
                .collect(Collectors.toList());
    }

    private static Path prefixWithWorkingDir(String pathName) {
        Path path = Paths.get(pathName);
        if (path.isAbsolute()) {
            return path;
        }

        return getCfg().getWorkingDir().resolve(pathName).toAbsolutePath();
    }
}
