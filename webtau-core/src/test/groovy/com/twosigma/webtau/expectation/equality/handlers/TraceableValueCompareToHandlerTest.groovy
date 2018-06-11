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

package com.twosigma.webtau.expectation.equality.handlers

import com.twosigma.webtau.data.traceable.CheckLevel
import com.twosigma.webtau.data.traceable.TraceableValue
import org.junit.Before
import org.junit.Test

import static com.twosigma.webtau.Ddjt.actual
import static com.twosigma.webtau.Ddjt.beGreaterThan
import static com.twosigma.webtau.Ddjt.beGreaterThanOrEqual
import static com.twosigma.webtau.Ddjt.beLessThan
import static com.twosigma.webtau.Ddjt.beLessThanOrEqual
import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.equal
import static com.twosigma.webtau.Ddjt.throwException

class TraceableValueCompareToHandlerTest {
    def nonMatchingValue = 100
    TraceableValue traceableValue

    @Before
    void init() {
        traceableValue = new TraceableValue(10)
    }

    @Test
    void "handles traceable value as actual"() {
        def handler = new TraceableValueCompareToHandler()
        assert handler.handleEquality(traceableValue, 10)
        assert !handler.handleEquality(10, 10)
    }

    @Test
    void "mark value as explicitly passed when matches during should equal"() {
        actual(traceableValue).should(equal(traceableValue.value))
        assert traceableValue.checkLevel == CheckLevel.ExplicitPassed
    }

    @Test
    void "mark value as explicitly failed when mismatches during should equal"() {
        code {
            actual(traceableValue).should(equal(nonMatchingValue))
        } should throwException(AssertionError)

        assert traceableValue.checkLevel == CheckLevel.ExplicitFailed
    }

    @Test
    void "mark value as explicitly passed when matches during should not equal"() {
        actual(traceableValue).shouldNot(equal(nonMatchingValue))
        assert traceableValue.checkLevel == CheckLevel.FuzzyPassed
    }

    @Test
    void "mark value as explicitly failed when mismatches during should not equal"() {
        code {
            actual(traceableValue).shouldNot(equal(traceableValue.value))
        } should throwException(AssertionError)

        assert traceableValue.checkLevel == CheckLevel.ExplicitFailed
    }

    @Test
    void "mark value as fuzzy passed when matches during greater than"() {
        actual(traceableValue).should(beGreaterThan(traceableValue.value - 1))
        assert traceableValue.checkLevel == CheckLevel.FuzzyPassed
    }

    @Test
    void "mark value as fuzzy passed when matches during greater than or equal"() {
        actual(traceableValue).should(beGreaterThanOrEqual(traceableValue.value))
        assert traceableValue.checkLevel == CheckLevel.FuzzyPassed
    }

    @Test
    void "mark value as fuzzy passed when matches during less than"() {
        actual(traceableValue).should(beLessThan(traceableValue.value + 1))
        assert traceableValue.checkLevel == CheckLevel.FuzzyPassed
    }

    @Test
    void "mark value as fuzzy passed when matches during less than or equal"() {
        actual(traceableValue).should(beLessThanOrEqual(traceableValue.value))
        assert traceableValue.checkLevel == CheckLevel.FuzzyPassed
    }

    @Test
    void "mark value as fuzzy passed when matches during not less than or equal"() {
        actual(traceableValue).shouldNot(beLessThanOrEqual(traceableValue.value - 1))
        assert traceableValue.checkLevel == CheckLevel.FuzzyPassed
    }

    @Test
    void "mark value as explicitly failed when fails during less than"() {
        code {
            actual(traceableValue).should(beLessThan(traceableValue.value - 1))
        } should throwException(AssertionError)

        assert traceableValue.checkLevel == CheckLevel.ExplicitFailed
    }

    @Test
    void "mark value as explicitly failed when fails during less than or equal"() {
        code {
            actual(traceableValue).should(beLessThanOrEqual(traceableValue.value - 1))
        } should throwException(AssertionError)

        assert traceableValue.checkLevel == CheckLevel.ExplicitFailed
    }

    @Test
    void "mark value as explicitly failed when fails during greater than"() {
        code {
            actual(traceableValue).should(beGreaterThan(traceableValue.value + 1))
        } should throwException(AssertionError)

        assert traceableValue.checkLevel == CheckLevel.ExplicitFailed
    }

    @Test
    void "mark value as explicitly failed when fails during greater than or equal"() {
        code {
            actual(traceableValue).should(beGreaterThanOrEqual(traceableValue.value + 1))
        } should throwException(AssertionError)

        assert traceableValue.checkLevel == CheckLevel.ExplicitFailed
    }

    @Test
    void "mark value as explicitly failed when fails during not greater than"() {
        code {
            actual(traceableValue).shouldNot(beGreaterThan(traceableValue.value - 1))
        } should throwException(AssertionError)

        assert traceableValue.checkLevel == CheckLevel.ExplicitFailed
    }
}
