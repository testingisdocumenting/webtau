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

package com.twosigma.webtau.expectation.contain

import org.junit.Test

import static com.twosigma.webtau.Ddjt.actual
import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.contain
import static com.twosigma.webtau.Ddjt.throwException

class ContainMatcherTest {
    @Test
    void "should throw exception when value doesn't contain expected value"() {
        code {
            actual("hello world").should(contain("world!"))
        } should throwException("\n[value] expect to contain world!\n" +
            "[value]: hello world")
    }

    @Test
    void "should throw exception when value contain expected value, but should not"() {
        code {
            actual("hello world").shouldNot(contain("world"))
        } should throwException("[value] expect to not contain world\n" +
            "[value]: hello world")
    }

    @Test
    void "should pass when value contains expected value"() {
        actual("hello world").should(contain("world"))
    }

    @Test
    void "should pass when value doesn't contains expected value and should not"() {
        actual("hello world").shouldNot(contain("world!"))
    }
}
