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

package com.twosigma.webtau.expectation

import com.twosigma.webtau.data.DummyLiveValue
import com.twosigma.webtau.expectation.timer.DummyExpectationTimer
import org.junit.Test

import static com.twosigma.webtau.Ddjt.*

class ActualValueTest {
    def liveValue = new DummyLiveValue([1, 10, 100, 1000, 2000])
    def ones = new DummyLiveValue([1, 1, 1, 1, 1])

    @Test
    void "waitTo retries matcher till success"() {
        def expectationTimer = new DummyExpectationTimer(10)
        actual(liveValue).waitTo(equal(1000), expectationTimer, 1000, 10)
    }

    @Test(expected = AssertionError)
    void "waitTo fails when timer times out"() {
        def expectationTimer = new DummyExpectationTimer(2)
        actual(liveValue).waitTo(equal(2000), expectationTimer, 1000, 10)
    }

    @Test
    void "waitToNot retries matcher till success"() {
        def expectationTimer = new DummyExpectationTimer(10)
        actual(liveValue).waitToNot(equal(1), expectationTimer, 1000, 10)
    }

    @Test(expected = AssertionError)
    void "waitToNot fails when timer times out"() {
        def expectationTimer = new DummyExpectationTimer(2)
        actual(ones).waitToNot(equal(1), expectationTimer, 1000, 10)
    }
}
