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
import org.testingisdocumenting.webtau.reporter.TokenizedMessage

import static org.testingisdocumenting.webtau.Matchers.actual
import static org.testingisdocumenting.webtau.Matchers.equal
import static org.testingisdocumenting.webtau.WebTauCore.map
import static org.testingisdocumenting.webtau.WebTauCore.tokenizedMessage
import static org.testingisdocumenting.webtau.expectation.equality.ActualExpectedTestReportExpectations.simpleActualExpectedWithIntegers
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runAndValidateOutput

class EqualMatcherTest {
    private final int expected = 8
    private final ValuePath actualPath = new ValuePath("value")
    private final EqualMatcher matcher = new EqualMatcher(expected)

    @Test
    void "positive match"() {
        def actual = expected

        assert matcher.matches(actualPath, actual)
        assert matcher.matchedTokenizedMessage(actualPath, actual).toString() == "equals $expected"
    }

    @Test
    void "positive mismatch"() {
        def actual = expected + 1
        assert !matcher.matches(actualPath, actual)

        assert matcher.mismatchedTokenizedMessage(actualPath, actual).toString() == "mismatches:\n\n" +
            simpleActualExpectedWithIntegers(actual, expected)
    }

    @Test
    void "negative match"() {
        def actual = expected + 1
        assert matcher.negativeMatches(actualPath, actual)
        assert matcher.negativeMatchedTokenizedMessage(actualPath, actual).toString() == "doesn't equal $expected"
    }

    @Test
    void "negative mismatch"() {
        def actual = expected
        assert !matcher.negativeMatches(actualPath, actual)
        assert matcher.negativeMismatchedTokenizedMessage(actualPath, actual).toString() == "mismatches:\n\n" +
            simpleActualExpectedWithIntegers(actual, "not", expected)
    }

    @Test
    void "matching message"() {
        assert matcher.matchingTokenizedMessage().toString() == "to equal $expected"
    }

    @Test
    void "negative matching message"() {
        assert matcher.negativeMatchingTokenizedMessage().toString() == "to not equal $expected"
    }

    @Test
    void "rendering positive"() {
        runAndValidateOutput(". [value] equals 100 (Xms)") {
            actual(100).should(equal(100))
        }

        runAndValidateOutput('. [value] equals {\n' +
                '                   "key": "value"\n' +
                '                 } (Xms)') {
            actual(map("key", "value")).should(equal(map("key", "value")))
        }

        runAndValidateOutput(". [value] doesn't equal 101 (Xms)") {
            actual(100).shouldNot(equal(101))
        }
    }

    @Test
    void "rendering negative"() {
        runAndValidateOutput('X failed expecting [value] to equal 10: \n' +
                '    mismatches:\n' +
                '    \n' +
                '    [value]:   actual: 100 <java.lang.Integer>\n' +
                '             expected: 10 <java.lang.Integer> (Xms)') {
            actual(100).should(equal(10))
        }
    }

    @Test
    void "delegates to value matcher as expected to streamline messages"() {
        int actual = 8
        EqualMatcher matcher = new EqualMatcher(new DummyMatcher())

        assert matcher.matchingTokenizedMessage().toString() == "matchingMessage"
        assert matcher.matchedTokenizedMessage(actualPath, actual).toString() == "matchedMessage:value:8"
        assert matcher.mismatchedTokenizedMessage(actualPath, actual).toString() == "mismatchedMessage:value:8"

        assert matcher.negativeMatchingTokenizedMessage().toString() == "negativeMatchingMessage"
        assert matcher.negativeMatchedTokenizedMessage(actualPath, actual).toString() == "negativeMatchedMessage:value:8"
        assert matcher.negativeMismatchedTokenizedMessage(actualPath, actual).toString() == "negativeMismatchedMessage:value:8"

        assert matcher.matches(actualPath, actual)
        assert matcher.negativeMatches(actualPath, actual)
    }

    private static class DummyMatcher implements ValueMatcher {
        @Override
        TokenizedMessage matchingTokenizedMessage() {
            return tokenizedMessage().matcher("matchingMessage")
        }

        @Override
        TokenizedMessage matchedTokenizedMessage(ValuePath actualPath, Object actual) {
            return tokenizedMessage().matcher("matchedMessage:" + actualPath + ":" + actual)
        }

        @Override
        TokenizedMessage mismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
            return tokenizedMessage().matcher("mismatchedMessage:" + actualPath + ":" + actual)
        }

        @Override
        boolean matches(ValuePath actualPath, Object actual) {
            return true
        }

        @Override
        TokenizedMessage negativeMatchingTokenizedMessage() {
            return tokenizedMessage().matcher("negativeMatchingMessage")
        }

        @Override
        TokenizedMessage negativeMatchedTokenizedMessage(ValuePath actualPath, Object actual) {
            return tokenizedMessage().matcher("negativeMatchedMessage:" + actualPath + ":" + actual)
        }

        @Override
        TokenizedMessage negativeMismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
            return tokenizedMessage().matcher("negativeMismatchedMessage:" + actualPath + ":" + actual)
        }

        @Override
        boolean negativeMatches(ValuePath actualPath, Object actual) {
            return true
        }
    }
}
