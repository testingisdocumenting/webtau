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

import static com.twosigma.webtau.Ddjt.actual
import static com.twosigma.webtau.Ddjt.beGreaterThan
import static com.twosigma.webtau.Ddjt.beGreaterThanOrEqual
import static com.twosigma.webtau.Ddjt.beLessThan
import static com.twosigma.webtau.Ddjt.beLessThanOrEqual

class AnyCompareToHandlerTest {
    def expected = 10

    @Test
    void "positive greater than match"() {
        actual(expected).should(beGreaterThan(expected - 1))
    }

    @Test
    void "positive greater than or equal to match"() {
        actual(expected).should(beGreaterThanOrEqual(expected - 1))
        actual(expected).should(beGreaterThanOrEqual(expected))
    }

    @Test
    void "positive less than match"() {
        actual(expected).should(beLessThan(expected + 1))
    }

    @Test
    void "positive less than or equal to match"() {
        actual(expected).should(beLessThanOrEqual(expected + 1))
        actual(expected).should(beLessThanOrEqual(expected))
    }

    @Test(expected = AssertionError)
    void "positive greater than mismatch"() {
        actual(expected).should(beGreaterThan(expected))
    }

    @Test(expected = AssertionError)
    void "positive greater than or equal to mismatch"() {
        actual(expected).should(beGreaterThanOrEqual(expected + 1))
    }

    @Test(expected = AssertionError)
    void "positive less than mismatch"() {
        actual(expected).should(beLessThan(expected))
    }

    @Test(expected = AssertionError)
    void "positive less than or equal to mismatch"() {
        actual(expected).should(beLessThanOrEqual(expected - 1))
    }

    @Test
    void "negative greater than match"() {
        actual(expected).shouldNot(beGreaterThan(expected))
        actual(expected).shouldNot(beGreaterThan(expected + 1))
    }

    @Test
    void "negative greater than or equal to match"() {
        actual(expected).shouldNot(beGreaterThanOrEqual(expected + 1))
    }

    @Test
    void "negative less than match"() {
        actual(expected).shouldNot(beLessThan(expected))
        actual(expected).shouldNot(beLessThan(expected - 1))
    }

    @Test
    void "negative less than or equal to match"() {
        actual(expected).shouldNot(beLessThanOrEqual(expected - 1))
    }

    @Test(expected = AssertionError)
    void "negative greater than mismatch"() {
        actual(expected).shouldNot(beGreaterThan(expected - 1))
    }

    @Test(expected = AssertionError)
    void "negative greater than or equal to mismatch: equal"() {
        actual(expected).shouldNot(beGreaterThanOrEqual(expected))
    }

    @Test(expected = AssertionError)
    void "negative greater than or equal to mismatch: greater"() {
        actual(expected).shouldNot(beGreaterThanOrEqual(expected - 1))
    }

    @Test(expected = AssertionError)
    void "negative less than mismatch"() {
        actual(expected).shouldNot(beLessThan(expected + 1))
    }

    @Test(expected = AssertionError)
    void "negative less than or equal to mismatch: equal"() {
        actual(expected).shouldNot(beLessThanOrEqual(expected))
    }

    @Test(expected = AssertionError)
    void "negative less than or equal to mismatch: greater"() {
        actual(expected).shouldNot(beLessThanOrEqual(expected + 1))
    }
}
