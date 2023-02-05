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
import org.testingisdocumenting.webtau.expectation.ValueMatcher
import org.testingisdocumenting.webtau.data.ValuePath
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput

import static org.testingisdocumenting.webtau.Matchers.actual
import static org.testingisdocumenting.webtau.Matchers.equal
import static org.testingisdocumenting.webtau.expectation.equality.ActualExpectedTestReportExpectations.simpleActualExpectedWithIntegers

class EqualMatcherTest {
    private final int expected = 8
    private final ValuePath actualPath = new ValuePath("value")
    private final EqualMatcher matcher = new EqualMatcher(expected)

    @Test
    void "positive match"() {
        def actual = expected

        assert matcher.matches(actualPath, actual)
        assert matcher.matchedMessage(actualPath, actual) == "equals $expected"
    }

    @Test
    void "positive mismatch"() {
        def actual = expected + 1
        assert !matcher.matches(actualPath, actual)

        assert matcher.mismatchedMessage(actualPath, actual) == "mismatches:\n\n" +
            simpleActualExpectedWithIntegers(actual, expected)
    }

    @Test
    void "negative match"() {
        def actual = expected + 1
        assert matcher.negativeMatches(actualPath, actual)
        assert matcher.negativeMatchedMessage(actualPath, actual) == "doesn't equal $expected"
    }

    @Test
    void "negative mismatch"() {
        def actual = expected
        assert !matcher.negativeMatches(actualPath, actual)
        assert matcher.negativeMismatchedMessage(actualPath, actual) == "mismatches:\n\n" +
            simpleActualExpectedWithIntegers(actual, "not", expected)
    }

    @Test
    void "matching message"() {
        assert matcher.matchingMessage() == "to equal $expected"
    }

    @Test
    void "negative matching message"() {
        assert matcher.negativeMatchingMessage() == "to not equal $expected"
    }

    @Test
    void "rendering"() {
        TestConsoleOutput.runAndValidateOutput(". [value] equals 100 (Xms)") {
            actual(100).should(equal(100))
        }

        TestConsoleOutput.runAndValidateOutput(". [value] doesn't equal 101 (Xms)") {
            actual(100).shouldNot(equal(101))
        }
    }

    @Test
    void "delegates to value matcher as expected to streamline messages"() {
        int actual = 8
        EqualMatcher matcher = new EqualMatcher(new DummyMatcher())

        assert matcher.matchingMessage() == "matchingMessage"
        assert matcher.matchedMessage(actualPath, actual) == "matchedMessage:value:8"
        assert matcher.mismatchedMessage(actualPath, actual) == "mismatchedMessage:value:8"

        assert matcher.negativeMatchingMessage() == "negativeMatchingMessage"
        assert matcher.negativeMatchedMessage(actualPath, actual) == "negativeMatchedMessage:value:8"
        assert matcher.negativeMismatchedMessage(actualPath, actual) == "negativeMismatchedMessage:value:8"

        assert matcher.matches(actualPath, actual)
        assert matcher.negativeMatches(actualPath, actual)
    }

    private static class DummyMatcher implements ValueMatcher {
        @Override
        String matchingMessage() {
            return "matchingMessage"
        }

        @Override
        String matchedMessage(ValuePath actualPath, Object actual) {
            return "matchedMessage:" + actualPath + ":" + actual
        }

        @Override
        String mismatchedMessage(ValuePath actualPath, Object actual) {
            return "mismatchedMessage:" + actualPath + ":" + actual
        }

        @Override
        boolean matches(ValuePath actualPath, Object actual) {
            return true
        }

        @Override
        String negativeMatchingMessage() {
            return "negativeMatchingMessage"
        }

        @Override
        String negativeMatchedMessage(ValuePath actualPath, Object actual) {
            return "negativeMatchedMessage:" + actualPath + ":" + actual
        }

        @Override
        String negativeMismatchedMessage(ValuePath actualPath, Object actual) {
            return "negativeMismatchedMessage:" + actualPath + ":" + actual
        }

        @Override
        boolean negativeMatches(ValuePath actualPath, Object actual) {
            return true
        }
    }
}
