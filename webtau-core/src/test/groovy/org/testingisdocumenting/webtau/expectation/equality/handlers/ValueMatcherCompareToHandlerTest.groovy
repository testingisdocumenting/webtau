/*
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

import org.testingisdocumenting.webtau.expectation.ActualPath
import org.testingisdocumenting.webtau.expectation.ValueMatcher
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator
import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.junit.Assert.assertEquals

class ValueMatcherCompareToHandlerTest {
    private static final ActualPath actualPath = createActualPath("value")

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

        assertEquals('matches:\n' +
            '\n' +
            'value: matchedMessage', comparator.generateEqualMatchReport())
    }

    @Test
    void "uses ValueMatcher mismatch message when mismatches"() {
        CompareToComparator comparator = CompareToComparator.comparator()
        assert !comparator.compareIsEqual(actualPath, 100, new DummyValueMatcher(false))

        assertEquals('mismatches:\n' +
            '\n' +
            'value: mismatchedMessage', comparator.generateEqualMismatchReport())
    }

    @Test
    void "uses ValueMatcher negative match message when negative matches"() {
        CompareToComparator comparator = CompareToComparator.comparator()
        assert comparator.compareIsNotEqual(actualPath, 100, new DummyValueMatcher(true))

        assertEquals('mismatches:\n' +
            '\n' +
            'value: negativeMatchedMessage', comparator.generateEqualMismatchReport())
    }

    @Test
    void "uses ValueMatcher negative mismatch message when negative mismatches"() {
        CompareToComparator comparator = CompareToComparator.comparator()
        assert !comparator.compareIsNotEqual(actualPath, 100, new DummyValueMatcher(false))

        assertEquals('matches:\n' +
            '\n' +
            'value: negativeMismatchedMessage', comparator.generateEqualMatchReport())
    }

    @Test
    void "should work in combination with contain matcher"() {
        code {
            actual([1, 3, 8]).shouldNot(contain(greaterThan(7)))
        } should(throwException('\n[value] expects to not contain <greater than 7>\n' +
            '[value][2]: equals 8'))
    }

    @Test
    void "should work in combination with nested contain matcher"() {
        actual(['hello', 'world', 'of matchers']).should(contain(containing('of')))

        code {
            actual(['hello', 'world', 'of matchers']).shouldNot(contain(containing('of')))
        } should(throwException('\n[value] expects to not contain <contain "of">\n' +
            '[value][2]: equals "of matchers"'))
    }

    static class DummyValueMatcher implements ValueMatcher {
        private boolean matches

        DummyValueMatcher(boolean matches) {
            this.matches = matches
        }

        @Override
        String matchingMessage() {
            return null
        }

        @Override
        String matchedMessage(ActualPath actualPath, Object actual) {
            return 'matchedMessage'
        }

        @Override
        String mismatchedMessage(ActualPath actualPath, Object actual) {
            return 'mismatchedMessage'
        }

        @Override
        boolean matches(ActualPath actualPath, Object actual) {
            return matches
        }

        @Override
        String negativeMatchingMessage() {
            return null
        }

        @Override
        String negativeMatchedMessage(ActualPath actualPath, Object actual) {
            return 'negativeMatchedMessage'
        }

        @Override
        String negativeMismatchedMessage(ActualPath actualPath, Object actual) {
            return 'negativeMismatchedMessage'
        }

        @Override
        boolean negativeMatches(ActualPath actualPath, Object actual) {
            return matches
        }
    }
}
