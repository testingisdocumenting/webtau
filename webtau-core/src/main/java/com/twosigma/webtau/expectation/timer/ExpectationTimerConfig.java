package com.twosigma.webtau.expectation.timer;

public interface ExpectationTimerConfig {
    ExpectationTimer createExpectationTimer();
    long defaultTimeoutMillis();
    long defaultTickMillis();
}
