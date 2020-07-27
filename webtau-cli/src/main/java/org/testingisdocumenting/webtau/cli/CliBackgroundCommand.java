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

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.*;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class CliBackgroundCommand {
    private final String command;

    private final CliProcessConfig processConfig;
    private CliBackgroundProcess backgroundProcess;
    private long startTime;

    CliBackgroundCommand(String command, CliProcessConfig processConfig) {
        this.command = command;
        this.processConfig = processConfig;
    }

    public void run() {
        if (backgroundProcess != null) {
            return;
        }

        TestStep.createAndExecuteStep(
                tokenizedMessage(action("running cli command in background"), stringValue(command)),
                () -> tokenizedMessage(action("ran cli command in background"), stringValue(command)),
                this::startBackgroundProcess);

        waitForProcessToFinishInBackground();
    }

    public void stop() {
        if (backgroundProcess == null) {
            return;
        }

        TestStep.createAndExecuteStep(
                tokenizedMessage(action("stopping cli command in background"), stringValue(command)),
                () -> tokenizedMessage(action("stopped cli command in background"), stringValue(command)),
                () -> {
                    backgroundProcess.destroy();
                    CliBackgroundCommandManager.remove(this);

                    backgroundProcess = null;
                });
    }

    CliBackgroundProcess getBackgroundProcess() {
        return backgroundProcess;
    }

    public void reRun() {
        stop();
        run();
    }

    public CliOutput getOutput() {
        return backgroundProcess.getOutput();
    }

    public CliOutput getError() {
        return backgroundProcess.getError();
    }

    public void send(String line) {
        TestStep.createAndExecuteStep(
                tokenizedMessage(action("sending"), stringValue(line), TO, classifier("running"), stringValue(command)),
                () -> tokenizedMessage(action("sent"), stringValue(line), TO, classifier("running"), stringValue(command)),
                () -> backgroundProcess.send(line));
    }

    public void clearOutput() {
        TestStep.createAndExecuteStep(
                () -> tokenizedMessage(action("cleared output"), OF, classifier("running"), stringValue(command)),
                () -> backgroundProcess.clearOutput());
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

    private void waitForProcessToFinishInBackground() {
        new Thread(() -> {
            try {
                backgroundProcess.getProcess().waitFor();

                TestStep step = TestStep.createStep(null,
                        startTime,
                        tokenizedMessage(),
                        () -> tokenizedMessage(action("background cli command"), COLON, stringValue(command),
                                action("finished with exit code"), numberValue(backgroundProcess.exitCode())),
                        () -> CliBackgroundCommandManager.remove(this));

                step.execute(StepReportOptions.SKIP_START);
            } catch (InterruptedException e) {
                // ignore
            }
        }).start();
    }
}
