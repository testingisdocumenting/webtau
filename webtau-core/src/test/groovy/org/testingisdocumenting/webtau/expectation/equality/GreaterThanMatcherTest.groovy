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

class GreaterThanMatcherTest {
    private final int expected = 8
    private final ValuePath actualPath = new ValuePath('value')
    private final GreaterThanMatcher matcher = new GreaterThanMatcher(expected)

    @Test
    void "positive match"() {
        runAndValidateOutput(". [value] greater than 88 (Xms)") {
            actual(100).shouldBe(greaterThan(88))
        }
    }

    @Test
    void "positive mismatch"() {
        runExpectExceptionAndValidateOutput(AssertionError, "X failed expecting [value] to be greater than 100: \n" +
                "    mismatches:\n" +
                "    \n" +
                "    [value]:   actual: 100 <java.lang.Integer>\n" +
                "             expected: greater than 100 <java.lang.Integer> (Xms)") {
            actual(100).shouldBe(greaterThan(100))
        }
    }

    @Test
    void "negative match"() {
        runAndValidateOutput(". [value] less than or equal to 100 (Xms)") {
            actual(100).shouldNotBe(greaterThan(100))
        }
    }

    @Test
    void "negative mismatch"() {
        runExpectExceptionAndValidateOutput(AssertionError, "X failed expecting [value] to be less than or equal to 10: \n" +
                "    mismatches:\n" +
                "    \n" +
                "    [value]:   actual: 100 <java.lang.Integer>\n" +
                "             expected: less than or equal to 10 <java.lang.Integer> (Xms)") {
            actual(100).shouldNotBe(greaterThan(10))
        }
    }

    @Test
    void "matching message"() {
        assert matcher.matchingTokenizedMessage().toString() == "to be greater than $expected"
    }

    @Test
    void "negative matching message"() {
        assert matcher.negativeMatchingTokenizedMessage().toString() == "to be less than or equal to $expected"
    }

    @Test
    void "equal comparison with matcher renders matching logic in case of comparison with null"() {
        CompareToComparator comparator = CompareToComparator.comparator()
        comparator.compareIsEqual(actualPath, null, matcher)
        assert comparator.generateEqualMismatchReport().contains('expected: <greater than 8>')
    }
}
