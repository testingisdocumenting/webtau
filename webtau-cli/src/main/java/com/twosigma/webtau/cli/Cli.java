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

import com.twosigma.webtau.cli.expectation.CliExitCode;
import com.twosigma.webtau.cli.expectation.CliOutput;
import com.twosigma.webtau.cli.expectation.CliValidationExitCodeOutputHandler;
import com.twosigma.webtau.cli.expectation.CliValidationOutputOnlylHandler;
import com.twosigma.webtau.reporter.TestStep;
import com.twosigma.webtau.utils.CollectionUtils;

import java.util.Map;
import java.util.function.Consumer;

import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.action;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.stringValue;
import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class Cli {
    public static final Cli cli = new Cli();

    private Cli() {
    }

    public ProcessEnv env(Map<String, String> env) {
        return new ProcessEnv(env);
    }

    public ProcessEnv env(String... keyValue) {
        return new ProcessEnv(CollectionUtils.aMapOf((Object[]) keyValue));
    }

    public void run(String command, CliValidationOutputOnlylHandler handler) {
        run(command, ProcessEnv.EMPTY, handler);
    }

    public void run(String command, ProcessEnv env, CliValidationOutputOnlylHandler handler) {
        cliStep(command, env, (runResult) -> handler.handle(cliOutput(runResult), cliError(runResult)));
    }

    public void run(String command, CliValidationExitCodeOutputHandler handler) {
        run(command, ProcessEnv.EMPTY, handler);
    }

    public void run(String command, ProcessEnv env, CliValidationExitCodeOutputHandler handler) {
        cliStep(command, env,
                (runResult) -> handler.handle(exitCode(runResult), cliOutput(runResult), cliError(runResult)));
    }

    private void cliStep(String command, ProcessEnv env, Consumer<ProcessRunResult> validationCode) {
        TestStep.createAndExecuteStep(null,
                tokenizedMessage(action("running cli command "), stringValue(command)),
                () -> tokenizedMessage(action("ran cli command"), stringValue(command)),
                () -> {
                    ProcessRunResult runResult = ProcessUtils.run(command, env.getEnv());
                    if (runResult.getErrorReadingException() != null) {
                        throw new RuntimeException(runResult.getErrorReadingException());
                    }

                    if (runResult.getOutputReadingException() != null) {
                        throw new RuntimeException(runResult.getOutputReadingException());
                    }

                    validationCode.accept(runResult);
                });
    }

    private CliExitCode exitCode(ProcessRunResult runResult) {
        return new CliExitCode(runResult.getExitCode());
    }

    private static CliOutput cliOutput(ProcessRunResult runResult) {
        return new CliOutput("process output", runResult.getOutput());
    }

    private static CliOutput cliError(ProcessRunResult runResult) {
        return new CliOutput("process error output", runResult.getError());
    }
}
