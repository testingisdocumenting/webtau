/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.expectation.ranges

import com.twosigma.webtau.expectation.ActualPath
import com.twosigma.webtau.expectation.ValueMatcher
import org.junit.Test

class GreaterThanMatcherTest {
    private ActualPath actualPath = new ActualPath("value")
    private ValueMatcher matcher = new GreaterThanMatcher(8)

    @Test
    void "positive match"() {
        def actual = 10

        assert matcher.matches(actualPath, actual)
        assert matcher.matchedMessage(actualPath, actual) == 'greater than 8 (actual equals 10)'
    }

    @Test
    void "positive mismatch"() {
        assert !matcher.matches(actualPath, 8)
        assert matcher.mismatchedMessage(actualPath, 8) == 'equals 8'

        assert !matcher.matches(actualPath, 6)
        assert matcher.mismatchedMessage(actualPath, 6) == 'less than 8 (actual equals 6)'
    }

    @Test
    void "negative match"() {
        assert matcher.negativeMatches(actualPath, 6)
        assert matcher.negativeMatchedMessage(actualPath, 6) == 'less than 8 (actual equals 6)'

        assert matcher.negativeMatches(actualPath, 8)
        assert matcher.negativeMatchedMessage(actualPath, 8) == 'equals 8'
    }

    @Test
    void "negative mismatch"() {
        assert !matcher.negativeMatches(actualPath, 10)
        assert matcher.negativeMismatchedMessage(actualPath, 10) == "greater than 8 (actual equals 10)"
    }

    @Test
    void "matching message"() {
        assert matcher.matchingMessage() == "to be greater than 8"
    }

    @Test
    void "negative matching message"() {
        assert matcher.negativeMatchingMessage() == "to be less than or equal to 8"
    }
}
