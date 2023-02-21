/*
 * Copyright 2022 webtau maintainers
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

class AnyOfMatcherTest {
    private final ValuePath actualPath = new ValuePath("value")

    @Test
    void "positive match"() {
        runAndValidateOutput('. [value] matches any of [3, 10, <greater than 8>, 10] (Xms)') {
            actual(10).shouldBe(anyOf(3, 10, greaterThan(8), 10))
        }
    }

    @Test
    void "positive mismatch"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to match any of [3, <less than 8>, 1]:\n' +
                '      actual: 12 <java.lang.Integer>\n' +
                '    expected: 3 <java.lang.Integer>\n' +
                '      actual: 12 <java.lang.Integer>\n' +
                '    expected: less than 8 <java.lang.Integer>\n' +
                '      actual: 12 <java.lang.Integer>\n' +
                '    expected: 1 <java.lang.Integer> (Xms)') {
            actual(12).shouldBe(anyOf(3, lessThan(8), 1))
        }
    }

    @Test
    void "negative match"() {
        runAndValidateOutput('. [value] doesn\'t match any of [3, 11, <less than 8>] (Xms)') {
            actual(10).shouldNotBe(anyOf(3, 11, lessThan(8)))
        }
    }

    @Test
    void "negative mismatch"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to not match any of [3, 11, <less than 12>]:\n' +
                '      actual: 10 <java.lang.Integer>\n' +
                '    expected: greater than or equal to 12 <java.lang.Integer> (Xms)') {
            actual(10).shouldNotBe(anyOf(3, 11, lessThan(12)))
        }
    }
}