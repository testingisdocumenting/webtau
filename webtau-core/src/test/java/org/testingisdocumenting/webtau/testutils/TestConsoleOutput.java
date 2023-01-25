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

import org.junit.Assert;
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

    public static TestConsoleOutput runAndValidateOutput(String expectedOutput, Runnable code) {
        TestConsoleOutput testOutput = new TestConsoleOutput();

        ConsoleOutputs.add(ConsoleOutputs.defaultOutput);
        StepReporters.add(StepReporters.defaultStepReporter);
        try {
            ConsoleOutputs.withAdditionalOutput(testOutput, () -> {
                try {
                    code.run();
                } catch (AssertionError ignored) {
                }
                return null;
            });
        } finally {
            StepReporters.remove(StepReporters.defaultStepReporter);
            ConsoleOutputs.remove(ConsoleOutputs.defaultOutput);
        }

        String output = replaceTime(testOutput.getNoColorOutput());
        Assert.assertEquals(expectedOutput, output);

        return testOutput;
    }

    public static void runCaptureAndValidateOutput(String artifactName, String expectedOutput, Runnable code) {
        TestConsoleOutput testConsoleOutput = runAndValidateOutput(expectedOutput, code);
        doc.capture(artifactName, testConsoleOutput.getColorOutput());
    }

    private static String replaceTime(String original) {
        return original.replaceAll("\\d+ms", "Xms");
    }
}
