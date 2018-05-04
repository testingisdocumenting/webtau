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

import org.junit.Test

import static com.twosigma.webtau.Ddjt.*

class RegexpEqualHandlerTest {
    @Test
    void "handles instances of String value as actual and Pattern as expected"() {
        def handler = new RegexpEqualHandler()

        assert handler.handle("test", ~/regexp/)

        assert ! handler.handle("test", "another text")
        assert ! handler.handle(100, ~/regexp/)
    }

    @Test
    void "uses regexp to compare values"() {
        actual("hello world").should(equal(~/wor.d/))
    }

    @Test(expected = AssertionError)
    void "shows regexp when comparison fails"() {
        actual("hello world").should(equal(~/wor1d/))
    }
}
