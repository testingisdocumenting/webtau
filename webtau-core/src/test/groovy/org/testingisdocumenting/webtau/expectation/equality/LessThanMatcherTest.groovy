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

package org.testingisdocumenting.webtau.expectation.equality

import org.junit.Test
import org.testingisdocumenting.webtau.data.ValuePath

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runAndValidateOutput
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runExpectExceptionAndValidateOutput

class LessThanMatcherTest {
    private final int expected = 8
    private final ValuePath actualPath = new ValuePath('value')
    private final LessThanMatcher matcher = new LessThanMatcher(expected)

    @Test
    void "positive match"() {
        runAndValidateOutput(". [value] less than 88 (Xms)") {
            actual(10).shouldBe(lessThan(88))
        }
    }

    @Test
    void "positive mismatch"() {
        runExpectExceptionAndValidateOutput(AssertionError, "X failed expecting [value] to be less than 100:\n" +
                "      actual: 100 <java.lang.Integer>\n" +
                "    expected: less than 100 <java.lang.Integer> (Xms)") {
            actual(100).shouldBe(lessThan(100))
        }
    }

    @Test
    void "negative match"() {
        runAndValidateOutput(". [value] greater than or equal to 100 (Xms)") {
            actual(100).shouldNotBe(lessThan(100))
        }
    }

    @Test
    void "negative mismatch"() {
        runExpectExceptionAndValidateOutput(AssertionError, "X failed expecting [value] to be greater than or equal to 10:\n" +
                "      actual: 1 <java.lang.Integer>\n" +
                "    expected: greater than or equal to 10 <java.lang.Integer> (Xms)") {
            actual(1).shouldNotBe(lessThan(10))
        }
    }

    @Test
    void "matching message"() {
        assert matcher.matchingTokenizedMessage(actualPath, 100).toString() == "to be less than $expected"
    }

    @Test
    void "negative matching message"() {
        assert matcher.negativeMatchingTokenizedMessage(actualPath, 100).toString() == "to be greater than or equal to $expected"
    }

    @Test
    void "equal comparison with matcher renders matching logic in case of comparison with null"() {
        CompareToComparator comparator = CompareToComparator.comparator()
        comparator.compareIsEqual(actualPath, null, matcher)
        assert comparator.generateEqualMismatchReport().toString().contains('expected: <less than 8>')
    }
}
