package com.twosigma.webtau.data.time.listener

import com.twosigma.webtau.data.time.TestTime

interface TimeRangeContextListener {
    void beforeCall(TestTime begin, TestTime end)
    void afterCall(TestTime begin, TestTime end)
}
