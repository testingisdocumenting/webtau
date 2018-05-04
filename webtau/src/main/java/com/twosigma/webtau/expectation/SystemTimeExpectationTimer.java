package com.twosigma.webtau.expectation;

import com.twosigma.webtau.expectation.timer.ExpectationTimer;

public class SystemTimeExpectationTimer implements ExpectationTimer {
    private long startTime;

    @Override
    public void start() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void tick(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasTimedOut(long millis) {
        return (System.currentTimeMillis() - startTime) > millis;
    }
}
