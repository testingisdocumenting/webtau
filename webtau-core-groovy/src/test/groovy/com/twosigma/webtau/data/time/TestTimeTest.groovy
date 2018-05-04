package com.twosigma.webtau.data.time

import org.junit.Test

import static java.time.Month.JANUARY

class TestTimeTest {
    @Test
    void "should convert date and time part to millis using UTC as a default time zone"() {
        assert new TestTime(1970, JANUARY, 1, 0, 0, 0).toMillisSinceEpoch() == 0
    }
}
