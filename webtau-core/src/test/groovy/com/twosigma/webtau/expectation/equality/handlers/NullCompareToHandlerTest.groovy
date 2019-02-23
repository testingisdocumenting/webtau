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

package com.twosigma.webtau.expectation.equality.handlers

import com.twosigma.webtau.expectation.ValueMatcher
import org.junit.Test

import static com.twosigma.webtau.Ddjt.*

class NullCompareToHandlerTest {
    @Test
    void "handles nulls as actual and expected"() {
        def handler = new NullCompareToHandler()

        assert handler.handleNulls()

        assert handler.handleEquality(null, "hello")
        assert handler.handleEquality("hello", null)
        assert handler.handleEquality(null, null)
        assert !handler.handleEquality(10, "world")

        assert handler.handleGreaterLessEqual(null, "hello")
        assert handler.handleGreaterLessEqual("hello", null)
        assert handler.handleGreaterLessEqual(null, null)
        assert !handler.handleGreaterLessEqual(10, "world")
    }

    @Test
    void "provides clear error message for equality check with null"() {
        actual(null).should(equal(null))
        actual("hello").shouldNot(equal(null))
        actual(null).shouldNot(equal("hello"))

        code {
            actual(10).should(equal(null))
        } should throwException(AssertionError, ~/expected: null/)

        code {
            actual(null).should(equal(10))
        } should throwException(AssertionError, ~/expected: 10/)
    }

    @Test
    void "null is greater-than-or-equal and less-than-or-equal than null"() {
        actual(null).should(beGreaterThanOrEqual(null))
        actual(null).should(beLessThanOrEqual(null))
    }

    @Test
    void "provides clear error message for greater-less check with actual equal to null"() {
        def expect = { ValueMatcher matcher, String expectedMessage ->
            code {
                actual(null).should(matcher)
            } should throwException(~/actual: null\n.*${expectedMessage}/)

        }

        expect(beGreaterThan(10), "expected: greater than 10")
        expect(beGreaterThanOrEqual(10), "expected: greater than or equal to 10")

        expect(beLessThan(10), "expected: less than 10")
        expect(beLessThanOrEqual(10), "expected: less than or equal to 10")
    }

    @Test
    void "provides clear error message for greater-less check with expected equal to null"() {
        def expect = { ValueMatcher matcher, String expectedMessage ->
            code {
                actual(10).should(matcher)
            } should throwException(~/actual: 10.*\n.*${expectedMessage}/)

        }

        expect(beGreaterThan(null), "expected: greater than null")
        expect(beGreaterThanOrEqual(null), "expected: greater than or equal to null")

        expect(beLessThan(null), "expected: less than null")
        expect(beLessThanOrEqual(null), "expected: less than or equal to null")
    }
}
