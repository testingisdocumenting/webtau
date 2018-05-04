package com.twosigma.webtau.data.time

class TestTimeDSL {
    static TestTime today(Map hoursMinutesMap) {
        def hm = TestTimeDSLUtils.hoursMinutesFromMap(hoursMinutesMap)
        return TestTime.today(hm.hours, hm.minutes)
    }
}
