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

import static com.twosigma.webtau.WebTauCore.*

class AnyCompareToHandlerTest {
    def expected = 10

    @Test
    void "positive greater than match"() {
        actual(expected).shouldBe(greaterThan(expected - 1))
    }

    @Test
    void "positive greater than or equal to match"() {
        actual(expected).shouldBe(greaterThanOrEqual(expected - 1))
        actual(expected).shouldBe(greaterThanOrEqual(expected))
    }

    @Test
    void "positive less than match"() {
        actual(expected).shouldBe(lessThan(expected + 1))
    }

    @Test
    void "positive less than or equal to match"() {
        actual(expected).shouldBe(lessThanOrEqual(expected + 1))
        actual(expected).shouldBe(lessThanOrEqual(expected))
    }

    @Test(expected = AssertionError)
    void "positive greater than mismatch"() {
        actual(expected).shouldBe(greaterThan(expected))
    }

    @Test(expected = AssertionError)
    void "positive greater than or equal to mismatch"() {
        actual(expected).shouldBe(greaterThanOrEqual(expected + 1))
    }

    @Test(expected = AssertionError)
    void "positive less than mismatch"() {
        actual(expected).shouldBe(lessThan(expected))
    }

    @Test(expected = AssertionError)
    void "positive less than or equal to mismatch"() {
        actual(expected).shouldBe(lessThanOrEqual(expected - 1))
    }

    @Test
    void "negative greater than match"() {
        actual(expected).shouldNotBe(greaterThan(expected))
        actual(expected).shouldNotBe(greaterThan(expected + 1))
    }

    @Test
    void "negative greater than or equal to match"() {
        actual(expected).shouldNotBe(greaterThanOrEqual(expected + 1))
    }

    @Test
    void "negative less than match"() {
        actual(expected).shouldNotBe(lessThan(expected))
        actual(expected).shouldNotBe(lessThan(expected - 1))
    }

    @Test
    void "negative less than or equal to match"() {
        actual(expected).shouldNotBe(lessThanOrEqual(expected - 1))
    }

    @Test(expected = AssertionError)
    void "negative greater than mismatch"() {
        actual(expected).shouldNotBe(greaterThan(expected - 1))
    }

    @Test(expected = AssertionError)
    void "negative greater than or equal to mismatch: equal"() {
        actual(expected).shouldNotBe(greaterThanOrEqual(expected))
    }

    @Test(expected = AssertionError)
    void "negative greater than or equal to mismatch: greater"() {
        actual(expected).shouldNotBe(greaterThanOrEqual(expected - 1))
    }

    @Test(expected = AssertionError)
    void "negative less than mismatch"() {
        actual(expected).shouldNotBe(lessThan(expected + 1))
    }

    @Test(expected = AssertionError)
    void "negative less than or equal to mismatch: equal"() {
        actual(expected).shouldNotBe(lessThanOrEqual(expected))
    }

    @Test(expected = AssertionError)
    void "negative less than or equal to mismatch: greater"() {
        actual(expected).shouldNotBe(lessThanOrEqual(expected + 1))
    }
}
