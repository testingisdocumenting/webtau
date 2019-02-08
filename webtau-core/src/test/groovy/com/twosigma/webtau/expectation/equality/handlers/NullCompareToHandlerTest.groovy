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

import org.junit.Test

import static com.twosigma.webtau.Ddjt.actual
import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.equal
import static com.twosigma.webtau.Ddjt.throwException

class NullCompareToHandlerTest {
    @Test
    void "handles nulls as actual and expected"() {
        def handler = new NullCompareToHandler()

        assert handler.handleNulls()
        assert handler.handleEquality(null, "hello")
        assert handler.handleEquality("hello", null)
        assert handler.handleEquality(null, null)

        assert !handler.handleEquality(10, "world")
    }

    @Test
    void "handler is linked"() {
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
}
