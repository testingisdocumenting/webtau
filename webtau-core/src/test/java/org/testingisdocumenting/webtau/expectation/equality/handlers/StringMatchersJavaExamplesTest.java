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

package org.testingisdocumenting.webtau.expectation.equality.handlers;

import org.junit.Test;

import static org.testingisdocumenting.webtau.Matchers.*;
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*;

public class StringMatchersJavaExamplesTest {
    @Test
    public void singleLine() {
        runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "single-line-string-compare-output",
                "X failed expecting [value] to equal \"hello world\":\n" +
                        "      actual: \"hallo world\" <java.lang.String>\n" +
                        "    expected: \"hello world\" <java.lang.String>\n" +
                        "                ^ (Xms)", () -> {
                    // single-line-compare
                    String output = "hallo world";
                    actual(output).should(equal("hello world"));
                    // single-line-compare
                });
    }

    @Test
    public void multiLine() {
        runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "multi-line-string-compare-output",
                contain("**line two**"),  () -> {
                    // multi-line-compare
                    String output = buildOutput();
                    actual(output).should(equal("line one\nline 2"));
                    // multi-line-compare
                });
    }

    @Test
    public void extraEmptyLines() {
        runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "extra-empty-line-string-compare-output",
                contain("different number of empty lines at the end"),  () -> {
                    // extra-empty-line-compare
                    String output = buildOutput();
                    actual(output).should(equal("line one\nline two\nline three\n"));
                    // extra-empty-line-compare
                });
    }

    private static String buildOutput() {
        return "line one\nline two\nline three";
    }
}
