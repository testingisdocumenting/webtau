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

import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.reporter.WebTauStepPayload;
import org.testingisdocumenting.webtau.time.Time;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class CliBackgroundCommand implements WebTauStepPayload {
    private final String command;

    private final CliProcessConfig processConfig;
    private CliBackgroundProcess backgroundProcess;
    private long startTime;

    private final ThreadLocal<Integer> localOutputNextLineIdxMarker = ThreadLocal.withInitial(() -> 0);
    private final ThreadLocal<Integer> localErrorNextLineIdxMarker = ThreadLocal.withInitial(() -> 0);

    private Thread waitToStopThread;

    CliBackgroundCommand(String command, CliProcessConfig processConfig) {
        this.command = command;
        this.processConfig = processConfig;
    }

    public void run() {
        if (backgroundProcess != null && backgroundProcess.isActive()) {
            return;
        }

        WebTauStep.createAndExecuteStep(
                tokenizedMessage(action("running cli command in background"), stringValue(command)),
                () -> tokenizedMessage(action("ran cli command in background"), stringValue(command)),
                this::startBackgroundProcess);

        waitToStopThread = waitForProcessToFinishInBackground();
    }

    public void stop() {
        WebTauStep.createAndExecuteStep(
                null,
                tokenizedMessage(action("stopping cli command in background"),
                        classifier("pid"), id(String.valueOf(backgroundProcess.getPid())), COLON, stringValue(command)),
                (wasRunning) -> (Boolean) wasRunning ?
                        tokenizedMessage(action("stopped cli command in background"), stringValue(command)) :
                        tokenizedMessage(action("command has already finished"), stringValue(command)),
                () -> {
                    boolean wasRunning = backgroundProcess.isActive();
                    if (wasRunning) {
                        synchronized (this) {
                            backgroundProcess.destroy();
                        }

                        try {
                            waitToStopThread.join();
                        } catch (InterruptedException ignored) {
                        }

                        CliBackgroundCommandManager.remove(this);
                    }

                    return wasRunning;
                },
                StepReportOptions.REPORT_ALL);
    }

    CliBackgroundProcess getBackgroundProcess() {
        return backgroundProcess;
    }

    public void reRun() {
        stop();
        run();
    }

    public boolean isActive() {
        return backgroundProcess.isActive();
    }

    public CliOutput getOutput() {
        return backgroundProcess.getOutput();
    }

    public CliOutput getError() {
        return backgroundProcess.getError();
    }

    public void send(String line) {
        WebTauStep.createAndExecuteStep(
                tokenizedMessage(action("sending"), stringValue(line), TO, classifier("running"), stringValue(command)),
                () -> tokenizedMessage(action("sent"), stringValue(line), TO, classifier("running"), stringValue(command)),
                () -> backgroundProcess.send(line));
    }

    public void clearOutput() {
        WebTauStep.createAndExecuteStep(
                () -> tokenizedMessage(action("cleared output"), OF, classifier("running"), stringValue(command)),
                () -> backgroundProcess.clearOutput());
    }

    // each thread maintains an output for report
    // so each test can capture the output of background processed during that test run
    void clearThreadLocal() {
        localOutputNextLineIdxMarker.set(backgroundProcess.getOutput().getNumberOfLines());
        localErrorNextLineIdxMarker.set(backgroundProcess.getError().getNumberOfLines());
    }

    List<String> getThreadLocalOutput() {
        return backgroundProcess.getOutputStartingAtIdx(localOutputNextLineIdxMarker.get());
    }

    List<String> getThreadLocalError() {
        return backgroundProcess.getErrorStartingAtIdx(localErrorNextLineIdxMarker.get());
    }

    private void startBackgroundProcess() {
        try {
            startTime = Time.currentTimeMillis();
            backgroundProcess = ProcessUtils.runInBackground(command, processConfig);
            CliBackgroundCommandManager.register(this);

            Cli.cli.setLastDocumentationArtifact(
                    new CliDocumentationArtifact(command, getOutput(), getError(), null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Thread waitForProcessToFinishInBackground() {
        Thread thread = new Thread(() -> {
            try {
                backgroundProcess.getProcess().waitFor();

                WebTauStep step = WebTauStep.createStep(null,
                        startTime,
                        tokenizedMessage(),
                        (exitCode) -> tokenizedMessage(action("background cli command"), COLON, stringValue(command),
                                action("finished with exit code"), numberValue(exitCode)),
                        (context) -> {
                            synchronized (this) {
                                backgroundProcess.setAsInactive();
                                CliBackgroundCommandManager.remove(this);
                            }

                            return backgroundProcess.exitCode();
                        });

                step.execute(StepReportOptions.SKIP_START);
            } catch (InterruptedException e) {
                // ignore
            }
        });

        thread.start();

        return thread;
    }

    @Override
    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("command", command);
        result.put("out", String.join("\n", getThreadLocalOutput()));
        result.put("err", String.join("\n", getThreadLocalError()));
        result.put("startTime", startTime);

        return result;
    }
}
