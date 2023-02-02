/*
 * Copyright 2023 webtau maintainers
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
import org.junit.Test
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runAndValidateOutput

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

        runAndValidateOutput(~/expected: null/) {
            actual(10).should(equal(null))
        }

        code {
            actual(null).should(equal(10))
        } should throwException(AssertionError)
    }

    @Test
    void "null is greater-than-or-equal and less-than-or-equal than null"() {
        actual(null).shouldBe(greaterThanOrEqual(null))
        actual(null).shouldBe(lessThanOrEqual(null))
    }

    @Test
    void "provides clear error message for greater-less check with actual equal to null"() {
        def expect = { ValueMatcher matcher, String expectedMessage ->
            runAndValidateOutput(~/actual: null\n.*${expectedMessage}/) {
                actual(null).should(matcher)
            }
        }

        expect(greaterThan(10), "expected: greater than 10")
        expect(greaterThanOrEqual(10), "expected: greater than or equal to 10")

        expect(lessThan(10), "expected: less than 10")
        expect(lessThanOrEqual(10), "expected: less than or equal to 10")
    }

    @Test
    void "provides clear error message for greater-less check with expected equal to null"() {
        def expect = { ValueMatcher matcher, String expectedMessage ->
            runAndValidateOutput(~/actual: 10.*\n.*${expectedMessage}/) {
                actual(10).should(matcher)
            }
        }

        expect(greaterThan(null), "expected: greater than null")
        expect(greaterThanOrEqual(null), "expected: greater than or equal to null")

        expect(lessThan(null), "expected: less than null")
        expect(lessThanOrEqual(null), "expected: less than or equal to null")
    }
}
