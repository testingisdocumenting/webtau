package com.twosigma.webtau.data.time

class TestTimeDSLUtils {
    private TestTimeDSLUtils() {
    }

    static HoursMinutesPart hoursMinutesFromMap(Map hoursMinutes) {
        if (hoursMinutes.size() != 1)
            throw new IllegalArgumentException("hours:minutes argument is expected to be a map with one element, key is hours and value is minutes")

        def hours = hoursMinutes.keySet().iterator().next()
        if (! (hours instanceof Integer))
            throw new IllegalArgumentException("hours in hours:minutes expected to be an Integer")

        def minutes = hoursMinutes.values().iterator().next()
        if (! (minutes instanceof Integer))
            throw new IllegalArgumentException("minutes in hours:minutes expected to be an Integer")

        return new HoursMinutesPart(hours: hours, minutes: minutes)
    }
}
