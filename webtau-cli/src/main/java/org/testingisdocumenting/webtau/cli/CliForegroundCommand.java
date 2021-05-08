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

import org.testingisdocumenting.webtau.cli.expectation.CliValidationExitCodeOutputHandler;
import org.testingisdocumenting.webtau.cli.expectation.CliValidationOutputOnlyHandler;
import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.ExpectationHandler;
import org.testingisdocumenting.webtau.expectation.ExpectationHandlers;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.util.function.Consumer;

import static org.testingisdocumenting.webtau.Matchers.equal;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.action;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.stringValue;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class CliForegroundCommand {
    CliForegroundCommand() {
    }

    public CliRunResult run(String command, CliProcessConfig config, CliValidationOutputOnlyHandler handler) {
        return cliStep(command, config, (validationResult) -> handler.handle(
                validationResult.getOut(),
                validationResult.getErr()));
    }

    public CliRunResult run(String command, CliProcessConfig config, CliValidationExitCodeOutputHandler handler) {
        return cliStep(command, config,
                (validationResult) -> handler.handle(
                        validationResult.getExitCode(),
                        validationResult.getOut(),
                        validationResult.getErr()));
    }

    private CliRunResult cliStep(String command, CliProcessConfig config, Consumer<CliValidationResult> validationCode) {
        CliValidationResult validationResult = new CliValidationResult(command);

        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage(action("running cli command "), stringValue(command)),
                () -> tokenizedMessage(action("ran cli command"), stringValue(command)),
                () -> runAndValidate(validationResult, command, config, validationCode));

        try {
            step.execute(StepReportOptions.REPORT_ALL);
            return new CliRunResult(command,
                    validationResult.getExitCode().get(),
                    validationResult.getOut().get(),
                    validationResult.getErr().get());
        } finally {
            step.setOutput(validationResult);
            Cli.cli.setLastDocumentationArtifact(validationResult.createDocumentationArtifact());
        }
    }

    private void runAndValidate(CliValidationResult validationResult,
                                String command,
                                CliProcessConfig config,
                                Consumer<CliValidationResult> validationCode) {
        try {
            long startTime = System.currentTimeMillis();
            ProcessRunResult runResult = ProcessUtils.run(command, config);
            long endTime = System.currentTimeMillis();

            if (runResult.getErrorReadingException() != null) {
                throw runResult.getErrorReadingException();
            }

            if (runResult.getOutputReadingException() != null) {
                throw runResult.getOutputReadingException();
            }

            validationResult.setExitCode(exitCode(runResult.getExitCode()));
            validationResult.setOut(runResult.getOutput());
            validationResult.setErr(runResult.getError());
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
                validateExitCode(validationResult);
                return null;
            });
        } catch (AssertionError e) {
            throw e;
        } catch (Throwable e) {
            validationResult.setErrorMessage(e.getMessage());
            throw new CliException(e.getMessage(), e);
        }
    }

    private static void validateExitCode(CliValidationResult validationResult) {
        if (validationResult.getExitCode().isChecked()) {
            return;
        }

        validationResult.getExitCode().should(equal(0));
    }

    private CliExitCode exitCode(int exitCode) {
        return new CliExitCode(exitCode);
    }
}
