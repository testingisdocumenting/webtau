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
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*

class GreaterThanOrEqualMatcherTest {
    private final int expected = 8
    private final ValuePath actualPath = new ValuePath('value')
    private final GreaterThanOrEqualMatcher matcher = new GreaterThanOrEqualMatcher(expected)

    @Test
    void "positive match greater than"() {
        assertPositiveMatch(expected + 1)
    }

    @Test
    void "positive match equal"() {
        assertPositiveMatch(expected)
    }

    @Test
    void "positive mismatch"() {
        runExpectExceptionAndValidateOutput(AssertionError, "X failed expecting [value] to be greater than or equal to 11:\n" +
                "      actual: 10 <java.lang.Integer>\n" +
                "    expected: greater than or equal to 11 <java.lang.Integer> (Xms)") {
            actual(10).shouldBe(greaterThanOrEqual(11))
        }
    }

    @Test
    void "negative match"() {
        runAndValidateOutput(". [value] less than 12 (Xms)") {
            actual(10).shouldNotBe(greaterThanOrEqual(12))
        }
    }

    @Test
    void "negative mismatch equal"() {
        runExpectExceptionAndValidateOutput(AssertionError, "X failed expecting [value] to be less than 8:\n" +
                "      actual: 8 <java.lang.Integer>\n" +
                "    expected: less than 8 <java.lang.Integer> (Xms)") {
            actual(expected).shouldNotBe(greaterThanOrEqual(expected))
        }
    }

    @Test
    void "negative mismatch greater"() {
        runExpectExceptionAndValidateOutput(AssertionError, "X failed expecting [value] to be less than 8:\n" +
                "      actual: 9 <java.lang.Integer>\n" +
                "    expected: less than 8 <java.lang.Integer> (Xms)") {
            actual(expected + 1).shouldNotBe(greaterThanOrEqual(expected))
        }
    }

    @Test
    void "matching message"() {
        assert matcher.matchingTokenizedMessage(actualPath, 100).toString() == "to be greater than or equal to $expected"
    }

    @Test
    void "negative matching message"() {
        assert matcher.negativeMatchingTokenizedMessage(actualPath, 100).toString() == "to be less than $expected"
    }

    @Test
    void "equal comparison with matcher renders matching logic in case of comparison with null"() {
        CompareToComparator comparator = CompareToComparator.comparator()
        comparator.compareIsEqual(actualPath, null, matcher)
        assert comparator.generateEqualMismatchReport().toString().contains('expected: <greater than or equal 8>')
    }

    private void assertPositiveMatch(int actualValue) {
        runAndValidateOutput(". [value] greater than or equal 8 (Xms)") {
           actual(actualValue).shouldBe(greaterThanOrEqual(expected))
        }
    }
}
