package com.twosigma.webtau.expectation.timer;

public interface ExpectationTimer {
    void start();
    void tick(long millis);
    boolean hasTimedOut(long millis);
}
