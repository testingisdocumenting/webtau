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
import com.twosigma.webtau.data.live.LiveValue
import com.twosigma.webtau.expectation.timer.DummyExpectationTimer
import org.junit.Before
import org.junit.Test

import static com.twosigma.webtau.Ddjt.actual
import static com.twosigma.webtau.Ddjt.equal

class ActualValueTest {
    LiveValue liveValue
    LiveValue ones

    @Before
    void init() {
        liveValue = new DummyLiveValue([1, 10, 100, 1000, 2000])
        ones = new DummyLiveValue([1, 1, 1, 1, 1])
    }

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

    @Test
    void "custom handler for waitTo"() {
        def expectationTimer = new DummyExpectationTimer(2)
        testCustomHandler('expected: 2000') {
            actual(liveValue).waitTo(equal(2000), expectationTimer, 1000, 10)
        }
    }

    @Test
    void "custom handler for waitToNot"() {
        def expectationTimer = new DummyExpectationTimer(2)
        testCustomHandler('equals 1') {
            actual(ones).waitToNot(equal(1), expectationTimer, 1000, 10)
        }
    }

    @Test
    void "custom handler for should"() {
        testCustomHandler('expected: 100') {
            actual(ones).should(equal(100))
        }
    }

    @Test
    void "custom handler for shouldNot"() {
        testCustomHandler('equals 1') {
            actual(ones).shouldNot(equal(1))
        }
    }

    static void testCustomHandler(expectedMessagePart, code) {
        def messages = []

        def handler = { path, value, message ->
            messages.add([path: path, value: value, message: message])
            return ExpectationHandler.Flow.Terminate
        }

        ExpectationHandlers.withAdditionalHandler(handler, code)

        assert messages.size() == 1

        def message = messages[0]
        assert message.message.contains(expectedMessagePart)
        assert message.value instanceof LiveValue
        assert message.path.toString() == '[value]'
    }
}
