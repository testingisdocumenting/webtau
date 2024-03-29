/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.testutils;

import org.testingisdocumenting.webtau.console.ConsoleOutput;
import org.testingisdocumenting.webtau.console.ConsoleOutputs;
import org.testingisdocumenting.webtau.console.ansi.AutoResetAnsiString;
import org.testingisdocumenting.webtau.console.ansi.IgnoreAnsiString;
import org.testingisdocumenting.webtau.reporter.StepReporters;

import java.util.ArrayList;
import java.util.List;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class TestConsoleOutput implements ConsoleOutput {
    private final List<String> noColorLines = new ArrayList<>();
    private final List<String> colorLines = new ArrayList<>();

    public void clear() {
        noColorLines.clear();
        colorLines.clear();
    }

    public String getNoColorOutput() {
        return String.join("\n", noColorLines);
    }

    public String getColorOutput() {
        return String.join("\n", colorLines);
    }

    @Override
    public void out(Object... styleOrValues) {
        colorLines.add(new AutoResetAnsiString(styleOrValues).toString());
        noColorLines.add(new IgnoreAnsiString(styleOrValues).toString());
    }

    @Override
    public void err(Object... styleOrValues) {
    }

    public static String replaceTimePortAndObjRefs(String original) {
        return original.replaceAll("\\d+ms", "Xms")
                .replaceAll("localhost:\\d+", "localhost:port")
                .replaceAll("@[^\\s,]+", "@<ref>");
    }

    public static TestConsoleOutput runAndValidateOutput(Object expectedOutput, Runnable code) {
        return runExpectExceptionAndValidateOutput(null, expectedOutput, code);
    }

    public static void withConsoleReporters(Runnable code) {
        ConsoleOutputs.add(ConsoleOutputs.defaultOutput);
        StepReporters.add(StepReporters.defaultStepReporter);

        try {
            code.run();
        } finally {
            StepReporters.remove(StepReporters.defaultStepReporter);
            ConsoleOutputs.remove(ConsoleOutputs.defaultOutput);
        }
    }

    public static TestConsoleOutput runExpectExceptionAndValidateOutput(Class<?> expectedException, Object expectedOutput, Runnable code) {
        TestConsoleOutput testOutput = new TestConsoleOutput();

        withConsoleReporters(() -> {
            OutputAndCaughtException outputAndCaughtException = ConsoleOutputs.withAdditionalOutput(testOutput, () -> {
                Throwable caughtException = null;
                try {
                    code.run();
                } catch (Throwable e) {
                    caughtException = e;
                }

                String output = replaceTimePortAndObjRefs(testOutput.getNoColorOutput());
                return new OutputAndCaughtException(output, caughtException);
            });

            if (expectedException != null) {
                actual(outputAndCaughtException.caughtException != null ?
                                outputAndCaughtException.caughtException.getClass() : null,
                        "caught exception").should(equal(expectedException));
            } else if (outputAndCaughtException.caughtException != null) {
                throw new AssertionError("expected no exception, but caught: " + outputAndCaughtException.caughtException);
            }

            actual(outputAndCaughtException.output, "output").should(equal(expectedOutput));
        });

        return testOutput;
    }

    public static void runCaptureAndValidateOutput(String artifactName, String expectedOutput, Runnable code) {
        TestConsoleOutput testConsoleOutput = runAndValidateOutput(expectedOutput, code);
        captureArtifact(artifactName, testConsoleOutput);
    }

    public static void runExpectExceptionCaptureAndValidateOutput(Class<?> expectedException, String artifactName, Object expectedOutput, Runnable code) {
        TestConsoleOutput testConsoleOutput = runExpectExceptionAndValidateOutput(expectedException, expectedOutput, code);
        captureArtifact(artifactName, testConsoleOutput);
    }

    private static void captureArtifact(String artifactName, TestConsoleOutput testConsoleOutput) {
        ConsoleOutputs.add(ConsoleOutputs.defaultOutput);
        StepReporters.add(StepReporters.defaultStepReporter);
        try {
            doc.capture(artifactName, testConsoleOutput.getColorOutput());
        } finally {
            StepReporters.remove(StepReporters.defaultStepReporter);
            ConsoleOutputs.remove(ConsoleOutputs.defaultOutput);
        }
    }

    private static class OutputAndCaughtException {
        private final String output;
        private final Throwable caughtException;

        public OutputAndCaughtException(String output, Throwable caughtException) {
            this.output = output;
            this.caughtException = caughtException;
        }
    }
}
