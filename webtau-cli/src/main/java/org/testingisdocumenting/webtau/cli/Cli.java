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
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.ExpectationHandler;
import com.twosigma.webtau.expectation.ExpectationHandlers;
import com.twosigma.webtau.expectation.ValueMatcher;
import com.twosigma.webtau.reporter.StepReportOptions;
import com.twosigma.webtau.reporter.TestStep;
import com.twosigma.webtau.utils.CollectionUtils;

import java.util.Map;
import java.util.function.Consumer;

import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.action;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.stringValue;
import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class Cli {
    public static final Cli cli = new Cli();

    private final ThreadLocal<CliValidationResult> lastValidationResult = new ThreadLocal<>();

    public final CliDocumentation doc = new CliDocumentation();

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
        cliStep(command, env, (validationResult) -> handler.handle(
                validationResult.getOut(),
                validationResult.getErr()));
    }

    public void run(String command, CliValidationExitCodeOutputHandler handler) {
        run(command, ProcessEnv.EMPTY, handler);
    }

    public void run(String command, ProcessEnv env, CliValidationExitCodeOutputHandler handler) {
        cliStep(command, env,
                (validationResult) -> handler.handle(
                        exitCode(validationResult),
                        validationResult.getOut(),
                        validationResult.getErr()));

    }

    public CliValidationResult getLastValidationResult() {
        return lastValidationResult.get();
    }

    private void cliStep(String command, ProcessEnv env, Consumer<CliValidationResult> validationCode) {
        CliValidationResult validationResult = new CliValidationResult(command);

        TestStep<Object, Void> step = TestStep.createStep(null,
                tokenizedMessage(action("running cli command "), stringValue(command)),
                () -> tokenizedMessage(action("ran cli command"), stringValue(command)),
                () -> runAndValidate(validationResult, command, env, validationCode));

        try {
            step.execute(StepReportOptions.REPORT_ALL);
        } finally {
            step.addPayload(validationResult);
            lastValidationResult.set(validationResult);
        }
    }

    private void runAndValidate(CliValidationResult validationResult,
                                String command,
                                ProcessEnv env,
                                Consumer<CliValidationResult> validationCode) {
        try {
            long startTime = System.currentTimeMillis();
            ProcessRunResult runResult = ProcessUtils.run(command, env.getEnv());
            long endTime = System.currentTimeMillis();

            if (runResult.getErrorReadingException() != null) {
                throw runResult.getErrorReadingException();
            }

            if (runResult.getOutputReadingException() != null) {
                throw runResult.getOutputReadingException();
            }

            validationResult.setExitCode(runResult.getExitCode());
            validationResult.setOut(cliOutput(runResult));
            validationResult.setErr(cliError(runResult));
            validationResult.setStartTime(startTime);
            validationResult.setElapsedTime(endTime - startTime);

            ExpectationHandler recordAndThrowHandler = new ExpectationHandler() {
                @Override
                public Flow onValueMismatch(ValueMatcher valueMatcher, ActualPath actualPath, Object actualValue, String message) {
                    validationResult.addMismatch(message);
                    return ExpectationHandler.Flow.PassToNext;
                }
            };

            ExpectationHandlers.withAdditionalHandler(recordAndThrowHandler, () -> {
                validationCode.accept(validationResult);
                return null;
            });
        } catch (AssertionError e) {
            throw e;
        } catch (Throwable e) {
            validationResult.setErrorMessage(e.getMessage());
            throw new CliException("error during running '" + command + "'", e);
        }
    }

    private CliExitCode exitCode(CliValidationResult validationResult) {
        return new CliExitCode(validationResult.getExitCode());
    }

    private static CliOutput cliOutput(ProcessRunResult runResult) {
        return new CliOutput("process output", runResult.getOutput());
    }

    private static CliOutput cliError(ProcessRunResult runResult) {
        return new CliOutput("process error output", runResult.getError());
    }
}
