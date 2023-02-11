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

package org.testingisdocumenting.webtau.expectation.equality.handlers;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.testingisdocumenting.webtau.Matchers.*;
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*;

public class ArrayAndArrayCompareToHandlerTest {
    @Test
    public void shouldHandleOnlyArrays() {
        ArrayAndArrayCompareToHandler handler = new ArrayAndArrayCompareToHandler();

        int[] a = {1, 2, 3};
        Boolean[] b = {true, false};

        actual(handler.handleEquality(a, a)).should(equal(true));
        actual(handler.handleEquality(a, b)).should(equal(true));
    }

    @Test
    public void shouldCompareArraysOfPrimitive() {
        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};

        actual(a).should(equal(b));
    }

    @Test
    public void shouldRenderExpectedArray() {
        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};

        runAndValidateOutput(". [value] equals [\n" +
                "                   1,\n" +
                "                   2,\n" +
                "                   3\n" +
                "                 ] (Xms)", () -> actual(a).should(equal(b)));
    }

    @Test
    public void shouldDetectDifferenceWhenCompareArraysOfPrimitive() {
        int[] a = {1, 2, 3};
        int[] b = {1, 7, 3};

        runAndValidateOutput(Pattern.compile("expected: 7"), () -> actual(a).should(equal(b)));
    }

    @Test
    public void shouldCompareElementsOfDifferentType() {
        int[] a = {1, 2, 3};
        long[] b = {1L, 2L, 3L};

        actual(a).should(equal(b));
    }
}