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

package org.testingisdocumenting.webtau.expectation

import org.testingisdocumenting.webtau.data.DummyLiveValue
import org.testingisdocumenting.webtau.data.ValuePath
import org.testingisdocumenting.webtau.data.live.LiveValue
import org.testingisdocumenting.webtau.expectation.timer.DummyExpectationTimer
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.reporter.TokenizedMessage

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runExpectExceptionAndValidateOutput

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

    @Test
    void "waitToNot fails when timer times out"() {
        def expectationTimer = new DummyExpectationTimer(2)
        runExpectExceptionAndValidateOutput(AssertionError.class, '> waiting for [value] to not equal 1\n' +
                'X failed waiting for [value] to not equal 1:\n' +
                '      actual: 1 <java.lang.Integer>\n' +
                '    expected: 1 <java.lang.Integer> (Xms)') {
            actual(ones).waitToNot(equal(1), expectationTimer, 1000, 10)
        }
    }

    @Test
    void "custom mismatch handler for waitTo"() {
        def expectationTimer = new DummyExpectationTimer(2)
        testCustomMismatchHandler('expected: 2000') {
            actual(liveValue).waitTo(equal(2000), expectationTimer, 1000, 10)
        }
    }

    @Test
    void "custom mismatch handler for waitToNot"() {
        def expectationTimer = new DummyExpectationTimer(2)
        testCustomMismatchHandler('actual: 1') {
            actual(ones).waitToNot(equal(1), expectationTimer, 1000, 10)
        }
    }

    @Test
    void "custom mismatch handler for should"() {
        testCustomMismatchHandler('expected: 100') {
            actual(ones).should(equal(100))
        }
    }

    @Test
    void "custom mismatch handler for shouldNot"() {
        testCustomMismatchHandler('actual: 1') {
            actual(ones).shouldNot(equal(1))
        }
    }

    @Test
    void "custom match handler for waitTo"() {
        def expectationTimer = new DummyExpectationTimer(100)
        testCustomMatchHandler(2000) {
            actual(liveValue).waitTo(equal(2000), expectationTimer, 1, 100)
        }
    }

    @Test
    void "custom match handler for waitToNot"() {
        def expectationTimer = new DummyExpectationTimer(100)
        testCustomMatchHandler(10) {
            actual(liveValue).waitToNot(equal(1), expectationTimer, 1, 100)
        }
    }

    @Test
    void "custom match handler for should"() {
        testCustomMatchHandler(1) {
            actual(ones).should(equal(1))
        }
    }

    @Test
    void "custom match handler for shouldNot"() {
        testCustomMatchHandler(1) {
            actual(ones).shouldNot(equal(2))
        }
    }

    static void testCustomMismatchHandler(expectedMessagePart, code) {
        def messages = []

        def handler = new ExpectationHandler() {
            @Override
            ExpectationHandler.Flow onValueMismatch(ValueMatcher valueMatcher, ValuePath path, Object actualValue, TokenizedMessage message) {
                messages.add([path: path, value: actualValue, message: message.toString()])
                return ExpectationHandler.Flow.Terminate
            }
        }

        ExpectationHandlers.withAdditionalHandler(handler, code)

        assert messages.size() == 1

        def message = messages[0]
        assert message.message.contains(expectedMessagePart)
        assert message.value instanceof LiveValue
        assert message.path.toString() == '[value]'
    }

    static void testCustomMatchHandler(expectedActual,code) {
        def messages = []

        def handler = new ExpectationHandler() {
            @Override
            void onValueMatch(ValueMatcher valueMatcher, ValuePath path, Object value) {
                messages.add([path: path, value: value])
            }
        }

        ExpectationHandlers.withAdditionalHandler(handler, code)

        assert messages.size() == 1

        def message = messages[0]
        assert message.value instanceof LiveValue
        assert message.value.last == expectedActual
        assert message.path.toString() == '[value]'
    }
}
