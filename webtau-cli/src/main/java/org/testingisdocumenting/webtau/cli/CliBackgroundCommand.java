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
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TestStep;
import org.testingisdocumenting.webtau.time.Time;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class CliBackgroundCommand {
    private static final Map<Integer, CliBackgroundProcess> runningProcesses = new ConcurrentHashMap<>();
    static {
        registerShutdown();
    }

    private final String command;

    private final ProcessEnv env;
    private CliBackgroundProcess backgroundProcess;
    private long startTime;

    public CliBackgroundCommand(String command, ProcessEnv env) {
        this.command = command;
        this.env = env;
    }

    public void start() {
        if (backgroundProcess != null) {
            return;
        }

        TestStep.createAndExecuteStep(
                tokenizedMessage(action("running cli command in background"), stringValue(command)),
                () -> tokenizedMessage(action("ran cli command in background"), stringValue(command)),
                this::startBackgroundProcess);

        waitForProcessToFinishInBackground();
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

    private void startBackgroundProcess() {
        try {
            startTime = Time.currentTimeMillis();
            backgroundProcess = ProcessUtils.runInBackground(command, env.getEnv());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        runningProcesses.put(backgroundProcess.getPid(), backgroundProcess);
    }

    private void waitForProcessToFinishInBackground() {
        new Thread(() -> {
            try {
                backgroundProcess.getProcess().waitFor();

                TestStep<?, CliBackgroundProcess> step = TestStep.createStep(null,
                        startTime,
                        tokenizedMessage(),
                        () -> tokenizedMessage(action("background cli command"), COLON, stringValue(command),
                                action("finished with exit code"), numberValue(backgroundProcess.exitCode())),
                        () -> runningProcesses.remove(backgroundProcess.getPid()));

                step.execute(StepReportOptions.SKIP_START);
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
