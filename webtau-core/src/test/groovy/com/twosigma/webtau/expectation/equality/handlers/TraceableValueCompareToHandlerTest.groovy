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
import org.junit.Test

import static com.twosigma.webtau.Ddjt.actual
import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.equal
import static com.twosigma.webtau.Ddjt.throwException

class TraceableValueCompareToHandlerTest {
    @Test
    void "handles traceable value as actual"() {
        def handler = new TraceableValueCompareToHandler()
        assert handler.handleEquality(new TraceableValue('test'), 10)
        assert !handler.handleEquality(10, 10)
    }

    @Test
    void "mark value as explicitly passed when matches during should equal"() {
        def v = new TraceableValue('test')
        actual(v).should(equal('test'))

        assert v.checkLevel == CheckLevel.ExplicitPassed
    }

    @Test
    void "mark value as explicitly failed when mismatches during should equal"() {
        def v = new TraceableValue('test')

        code {
            actual(v).should(equal('10'))
        } should throwException(AssertionError)

        assert v.checkLevel == CheckLevel.ExplicitFailed
    }

    @Test
    void "mark value as explicitly passed when matches during should not equal"() {
        def v = new TraceableValue('test')
        actual(v).shouldNot(equal(10))

        assert v.checkLevel == CheckLevel.FuzzyPassed
    }

    @Test
    void "mark value as explicitly failed when mismatches during should not equal"() {
        def v = new TraceableValue('test')
        code {
            actual(v).shouldNot(equal('test'))
        } should throwException(AssertionError)

        assert v.checkLevel == CheckLevel.ExplicitFailed
    }
}
