package com.twosigma.webtau.data.time.listener

import com.twosigma.webtau.data.time.TestTime
import com.twosigma.webtau.utils.ServiceUtils
class TimeRangeContextListeners {
    // TODO multithread support
    private static List<TimeRangeContextListener> listeners = ServiceUtils.discover(TimeRangeContextListener)

    static void clear() {
        listeners.clear()
    }

    static void add(TimeRangeContextListener listener) {
        listeners.add(listener)
    }

    static void beforeCall(TestTime begin, TestTime end) {
        listeners.each { it.beforeCall(begin, end) }
    }

    static void afterCall(TestTime begin, TestTime end) {
        listeners.each { it.afterCall(begin, end) }
    }
}
