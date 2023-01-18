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

package org.testingisdocumenting.webtau.expectation

import org.junit.Assert
import org.junit.Test
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.data.ValuePath
import org.testingisdocumenting.webtau.data.render.TestConsoleOutput
import org.testingisdocumenting.webtau.reporter.StepReporters

import static org.testingisdocumenting.webtau.Matchers.actual
import static org.testingisdocumenting.webtau.Matchers.equal

class ActualValueStepOutputTest {
    @Test
    void "should print iterable with marked failed values in case of mismatch"() {
        runAndValidateOutput("X failed expecting [value] to equal [1, world, 2]: \n" +
                "    mismatches:\n" +
                "    \n" +
                "    [value][1]:   actual: \"hello\" <java.lang.String>\n" +
                "                expected: \"world\" <java.lang.String>\n" +
                "                           ^ (Xms)\n" +
                "  [\n" +
                "    1,\n" +
                "    **\"hello\"**,\n" +
                "    2\n" +
                "  ]") {
            actual([1, "hello", 2]).should(equal([1, "world", 2]))
        }
    }

    @Test
    void "should print map with marked failed values in case of mismatch"() {
        runAndValidateOutput("X failed expecting [value] to equal {key=value1, another=23}: \n" +
                "    mismatches:\n" +
                "    \n" +
                "    [value].another:   actual: 22 <java.lang.Integer>\n" +
                "                     expected: 23 <java.lang.Integer> (Xms)\n" +
                "  {\n" +
                "    \"key\": \"value1\",\n" +
                "    \"another\": **22**\n" +
                "  }") {
            actual([key: "value1", another: 22]).should(equal([key: "value1", another: 23]))
        }
    }

    static void runAndValidateOutput(String expectedOutput, Closure code) {
        def testOutput = new TestConsoleOutput()

        ConsoleOutputs.add(ConsoleOutputs.defaultOutput)
        StepReporters.add(StepReporters.defaultStepReporter)
        try {
            ConsoleOutputs.withAdditionalOutput(testOutput) {
                try {
                    code()
                } catch (AssertionError ignored) {
                }
            }
        } finally {
            StepReporters.remove(StepReporters.defaultStepReporter)
            ConsoleOutputs.remove(ConsoleOutputs.defaultOutput)
        }

        def output = replaceTime(testOutput.noColorOutput)
        Assert.assertEquals(expectedOutput, output)
    }

    private static String replaceTime(String original) {
        return original.replaceAll("\\d+ms", "Xms")
    }
}
