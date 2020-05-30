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

import org.testingisdocumenting.webtau.cli.expectation.CliOutput;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CliBackgroundCommand {
    private static final Map<Integer, CliBackgroundProcess> runningProcesses = new ConcurrentHashMap<>();
    static {
        registerShutdown();
    }

    private final String command;

    private final ProcessEnv env;
    private CliBackgroundProcess backgroundProcess;
    public CliBackgroundCommand(String command, ProcessEnv env) {
        this.command = command;
        this.env = env;
    }

    public void start() {
        if (backgroundProcess != null) {
            return;
        }

        try {
            backgroundProcess = ProcessUtils.runInBackground(command, env.getEnv());
            runningProcesses.put(backgroundProcess.getPid(), backgroundProcess);
            waitForProcessToFinishInBackground();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CliOutput getOutput() {
        return backgroundProcess.getOutput();
    }

    public CliOutput getError() {
        return backgroundProcess.getError();
    }

    public void stop() {
        backgroundProcess.destroy();
        backgroundProcess = null;
    }

    public void restart() {
        stop();
        start();
    }

    private void waitForProcessToFinishInBackground() {
        new Thread(() -> {
            try {
                backgroundProcess.getProcess().waitFor();
                runningProcesses.remove(backgroundProcess.getPid());
            } catch (InterruptedException e) {
                // ignore
            }
        }).start();
    }

    private static void registerShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            runningProcesses.forEach((pid, process) -> process.destroy());
        }));
    }
}
