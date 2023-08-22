/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.equality.handlers

import org.junit.Test
import org.testingisdocumenting.webtau.data.ValuePath
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput

import static org.junit.Assert.*
import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.expectation.equality.CompareToComparator.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runExpectExceptionAndValidateOutput

class IterableCompareToHandlerTest {
    private static final ValuePath actualPath = createActualPath("value")

    @Test
    void "handles iterables"() {
        def handler = new IterableCompareToHandler()
        assert handler.handleEquality([], [])
        assert !handler.handleEquality("test", [])
    }

    @Test
    void "order should matter"() {
        actual([1, 2]).shouldNot(equal([2, 1]))
    }

    @Test
    void "should report matched elements"() {
        CompareToComparator comparator = comparator(AssertionMode.EQUAL)
        comparator.compareUsingEqualOnly(actualPath, [1, 2, 5], [1, 2, 5])

        assertEquals(
                "value[0]:  actual: 1 <java.lang.Integer>\n" +
                "         expected: 1 <java.lang.Integer>\n" +
                "value[1]:  actual: 2 <java.lang.Integer>\n" +
                "         expected: 2 <java.lang.Integer>\n" +
                "value[2]:  actual: 5 <java.lang.Integer>\n" +
                "         expected: 5 <java.lang.Integer>", comparator.generateEqualMatchReport().toString())
    }

    @Test
    void "should report mismatched elements"() {
        runExpectExceptionAndValidateOutput(AssertionError, "X failed expecting list to equal [3, 2, 4]:\n" +
                "    list[0]:  actual: 1 <java.lang.Integer>\n" +
                "            expected: 3 <java.lang.Integer>\n" +
                "    list[2]:  actual: 5 <java.lang.Integer>\n" +
                "            expected: 4 <java.lang.Integer> (Xms)\n" +
                "  \n" +
                "  [**1**, 2, **5**]") {
            actual([1, 2, 5], "list").should(equal([3, 2, 4]))
        }
    }

    @Test
    void "should report missing elements"() {
        runExpectExceptionAndValidateOutput(AssertionError, "X failed expecting list to equal [1, 2, 4, 6, 8, 10, 12, 43, 1, 5]:\n" +
                "    missing, but expected values:\n" +
                "    \n" +
                "    [4, 6, 8, 10, 12, 43, 1, 5] (Xms)\n" +
                "  \n" +
                "  [1, 2]") {
            actual([1, 2], "list").should(equal([1, 2, 4, 6, 8, 10, 12, 43, 1, 5]))
        }
    }

    @Test
    void "should report extra elements"() {
        runExpectExceptionAndValidateOutput(AssertionError, "X failed expecting list to equal [1, 2]:\n" +
                "    unexpected values:\n" +
                "    \n" +
                "    [3, 4, 12, 43, 23] (Xms)\n" +
                "  \n" +
                "  [1, 2, 3, 4, 12, 43, 23]") {
            actual([1, 2, 3, 4, 12, 43, 23], "list").should(equal([1, 2]))
        }
    }

    @Test
    void "should report matched elements in not equal mode"() {
        actual([1, 2]).shouldNot(equal([1, 2, 3]))

        CompareToComparator comparator = comparator(AssertionMode.NOT_EQUAL)
        comparator.compareUsingEqualOnly(actualPath, [1, 2], [1, 2, 3])

        assertEquals(
                "value[0]:  actual: 1 <java.lang.Integer>\n" +
                "         expected: not 1 <java.lang.Integer>\n" +
                "value[1]:  actual: 2 <java.lang.Integer>\n" +
                "         expected: not 2 <java.lang.Integer>", comparator.generateNotEqualMismatchReport().toString())
    }
}
