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
