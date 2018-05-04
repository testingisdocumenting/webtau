package com.twosigma.webtau.expectation.equality.handlers

import com.twosigma.webtau.data.DummyLiveValue
import org.junit.Test

import static com.twosigma.webtau.Ddjt.*

class LiveValueEqualHandlerTest {
    @Test
    void "handles instances of live value as actual and any other value as expected"() {
        def handler = new LiveValueEqualHandler()

        def liveValue = new DummyLiveValue([])

        assert handler.handle(liveValue, "hello")
        assert handler.handle(liveValue, 100)

        assert ! handler.handle(100, 100)
    }

    @Test
    void "calculates new value for comparison"() {
        def liveValue = new DummyLiveValue([1, 10, 100])
        actual(liveValue).should(equal(1))
        actual(liveValue).should(equal(10))
        actual(liveValue).should(equal(100))
    }
}
