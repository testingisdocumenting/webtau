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

class NotEqualMatcherTest {
    private final int expected = 8
    private final ValuePath actualPath = new ValuePath("value")
    private final NotEqualMatcher matcher = new NotEqualMatcher(expected)

    @Test
    void "positive match"() {
        runAndValidateOutput(". [value] doesn't equal 101 (Xms)") {
            actual(100).should(notEqual(101))
        }
    }

    @Test
    void "positive mismatch"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to not equal 100:\n' +
                '      actual: 100 <java.lang.Integer>\n' +
                '    expected: not 100 <java.lang.Integer> (Xms)') {
            actual(100).should(notEqual(100))
        }
    }

    @Test
    void "negative match"() {
        runAndValidateOutput(". [value] equals 100 (Xms)") {
            actual(100).shouldNot(notEqual(100))
        }
    }

    @Test
    void "negative mismatch"() {
        runExpectExceptionAndValidateOutput(AssertionError, "X failed expecting [value] to equal 101:\n" +
                "      actual: 100 <java.lang.Integer>\n" +
                "    expected: 101 <java.lang.Integer> (Xms)") {
            actual(100).shouldNot(notEqual(101))
        }
    }

    @Test
    void "delegates to matcher in case of negative mismatch if passed as expected"() {
        runExpectExceptionAndValidateOutput(AssertionError.class, 'X failed expecting [value] to not match any of [30, 100]:\n' +
                '      actual: 100 <java.lang.Integer>\n' +
                '    expected: not 100 <java.lang.Integer> (Xms)') {
            actual(100).should(notEqual(anyOf(30, 100)))
        }
    }

    @Test
    void "delegates to matcher in case of negative match if passed as expected"() {
        runAndValidateOutput(". [value] doesn't match any of [30, 110] (Xms)") {
            actual(100).should(notEqual(anyOf(30, 110)))
        }
    }

    @Test
    void "delegates to matcher in case of positive mismatch if passed as expected"() {
        runExpectExceptionAndValidateOutput(AssertionError.class, 'X failed expecting [value] to match any of [30, 110]:\n' +
                '      actual: 100 <java.lang.Integer>\n' +
                '    expected: 30 <java.lang.Integer>\n' +
                '      actual: 100 <java.lang.Integer>\n' +
                '    expected: 110 <java.lang.Integer> (Xms)') {
            actual(100).shouldNot(notEqual(anyOf(30, 110)))
        }
    }

    @Test
    void "delegates to matcher in case of positive match if passed as expected"() {
        runAndValidateOutput(". [value] matches any of [30, 100] (Xms)") {
            actual(100).shouldNot(notEqual(anyOf(30, 100)))
        }
    }

    @Test
    void "matching message"() {
        assert matcher.matchingTokenizedMessage(actualPath, 100).toString() == "to not equal $expected"
    }

    @Test
    void "negative matching message"() {
        assert matcher.negativeMatchingTokenizedMessage(actualPath, 100).toString() == "to equal $expected"
    }
}
