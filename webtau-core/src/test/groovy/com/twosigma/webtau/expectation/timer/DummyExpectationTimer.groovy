package com.twosigma.webtau.expectation.timer

class DummyExpectationTimer implements ExpectationTimer {
    int currentTick = 100
    int maxNumberOfTicks

    DummyExpectationTimer(int maxNumberOfTicks) {
        this.maxNumberOfTicks = maxNumberOfTicks
    }

    @Override
    void start() {
        currentTick = 0
    }

    @Override
    void tick(long millis) {
        currentTick++
    }

    @Override
    boolean hasTimedOut(long millis) {
        return currentTick >= maxNumberOfTicks
    }
}
