/*
 * Copyright 2021 webtau maintainers
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

import org.testingisdocumenting.webtau.expectation.ValueMatcher
import org.testingisdocumenting.webtau.data.ValuePath
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator
import org.junit.Test
import org.testingisdocumenting.webtau.reporter.TokenizedMessage

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.junit.Assert.assertEquals
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runAndValidateOutput
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runExpectExceptionAndValidateOutput

class ValueMatcherCompareToHandlerTest {
    private static final ValuePath actualPath = createActualPath("value")

    @Test
    void "handles expected as ValueMatcher"() {
        def handler = new ValueMatcherCompareToHandler()
        assert handler.handleEquality(100, greaterThan(10))
        assert !handler.handleEquality(100, 200)
    }

    @Test
    void "uses ValueMatcher match message when matches"() {
        CompareToComparator comparator = CompareToComparator.comparator()
        assert comparator.compareIsEqual(actualPath, 100, new DummyValueMatcher(true))

        assertEquals("matchedMessage", comparator.generateEqualMatchReport().toString())
    }

    @Test
    void "uses ValueMatcher mismatch message when mismatches"() {
        CompareToComparator comparator = CompareToComparator.comparator()
        assert !comparator.compareIsEqual(actualPath, 100, new DummyValueMatcher(false))

        assertEquals("mismatchedMessage", comparator.generateEqualMismatchReport().toString())
    }

    @Test
    void "uses ValueMatcher negative match message when negative matches"() {
        CompareToComparator comparator = CompareToComparator.comparator()
        assert comparator.compareIsNotEqual(actualPath, 100, new DummyValueMatcher(true))

        assertEquals("negativeMatchedMessage", comparator.generateEqualMismatchReport().toString())
    }

    @Test
    void "uses ValueMatcher negative mismatch message when negative mismatches"() {
        CompareToComparator comparator = CompareToComparator.comparator()
        assert !comparator.compareIsNotEqual(actualPath, 100, new DummyValueMatcher(false))

        assertEquals("negativeMismatchedMessage", comparator.generateEqualMatchReport().toString())
    }

    @Test
    void "should work in combination with contain matcher"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to not contain <greater than 7>:\n' +
                '    [value][2]:  actual: 8 <java.lang.Integer>\n' +
                '               expected: less than or equal to 7 <java.lang.Integer> (Xms)\n' +
                '  \n' +
                '  [1, 3, **8**]') {
            actual([1, 3, 8]).shouldNot(contain(greaterThan(7)))
        }
    }

    @Test
    void "combination with nested contain matcher"() {
        actual(["hello", "world", "of matchers"]).should(contain(containing("of")))
    }

    @Test
    void "combination with negative nested contain matcher"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to not contain <contain "of">:\n' +
                '    [value][2]: contains at idx 0 (Xms)\n' +
                '  \n' +
                '  ["hello", "world", **"of matchers"**]') {
            actual(["hello", "world", "of matchers"]).shouldNot(contain(containing("of")))
        }
    }

    @Test
    void "contain notEqual combination"() {
        runAndValidateOutput(". [value] contains <not equal 3> (Xms)") {
            actual([10, 20, 30]).should(contain(notEqual(3)))
        }
    }

    @Test
    void "contain greaterThan combination"() {
        runAndValidateOutput(". [value] contains <greater than 23> (Xms)") {
            actual([10, 20, 30]).should(contain(greaterThan(23)))
        }
    }

    static class DummyValueMatcher implements ValueMatcher {
        private boolean matches

        DummyValueMatcher(boolean matches) {
            this.matches = matches
        }

        @Override
        TokenizedMessage matchingTokenizedMessage(ValuePath actualPath, Object actual) {
            return tokenizedMessage().action("matchingMessage")
        }

        @Override
        TokenizedMessage matchedTokenizedMessage(ValuePath actualPath, Object actual) {
            return tokenizedMessage().action("matchedMessage")
        }

        @Override
        TokenizedMessage mismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
            return tokenizedMessage().action("mismatchedMessage")
        }

        @Override
        boolean matches(ValuePath actualPath, Object actual) {
            return matches
        }

        @Override
        TokenizedMessage negativeMatchingTokenizedMessage(ValuePath actualPath, Object actual) {
            return tokenizedMessage().action("negativeMatchingMessage")
        }

        @Override
        TokenizedMessage negativeMatchedTokenizedMessage(ValuePath actualPath, Object actual) {
            return tokenizedMessage().action("negativeMatchedMessage")
        }

        @Override
        TokenizedMessage negativeMismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
            return tokenizedMessage().action("negativeMismatchedMessage")
        }

        @Override
        boolean negativeMatches(ValuePath actualPath, Object actual) {
            return matches
        }
    }
}
