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

package org.testingisdocumenting.webtau.expectation.equality;


import org.junit.Test;
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput;

import java.util.Arrays;

import static org.testingisdocumenting.webtau.Matchers.*;

public class SameInstanceMatcherJavaTest {
    @Test
    public void matches() {
        TestConsoleOutput.runAndValidateOutput("""
                . [value] is the same instance as [1, 2, 3] (Xms)""", () -> {
            Object value = Arrays.asList(1, 2, 3);
            Object anotherValue = value;
            // should-be
            actual(value).shouldBe(sameInstance(anotherValue));
            // should-be
        });
    }

    @Test
    public void negativeMatches() {
        TestConsoleOutput.runAndValidateOutput("""
                . [value] is different instance from [1, 2, 3] (Xms)""", () -> {
            Object value = Arrays.asList(1, 2, 3);
            Object anotherValue = Arrays.asList(1, 2, 3);
            // should-not-be
            actual(value).shouldNotBe(sameInstance(anotherValue));
            // should-not-be
        });
    }

    @Test
    public void mismatch() {
        TestConsoleOutput.runExpectExceptionAndValidateOutput(AssertionError.class, """
                X failed expecting [value] to be the same instance as [1, 2, 3]:
                    different instance than [1, 2, 3] (Xms)
                 \s
                  [1, 2, 3]""", () -> {
            Object value = Arrays.asList(1, 2, 3);
            Object anotherValue = Arrays.asList(1, 2, 3);
            actual(value).shouldBe(sameInstance(anotherValue));
        });
    }

    @Test
    public void negativeMismatch() {
        TestConsoleOutput.runExpectExceptionAndValidateOutput(AssertionError.class, """
                X failed expecting [value] to be different instance from [1, 2, 3]:
                    is the same instance as [1, 2, 3] (Xms)""", () -> {
            Object value = Arrays.asList(1, 2, 3);
            Object anotherValue = value;
            actual(value).shouldNotBe(sameInstance(anotherValue));
        });
    }
}