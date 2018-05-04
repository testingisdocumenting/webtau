package com.twosigma.webtau.expectation;

import com.twosigma.webtau.expectation.timer.ExpectationTimer;
import com.twosigma.webtau.expectation.timer.ExpectationTimerConfig;
import com.twosigma.webtau.cfg.WebTauConfig;

public class SystemTimerConfig implements ExpectationTimerConfig {
    private static WebTauConfig cfg = WebTauConfig.INSTANCE;

    @Override
    public ExpectationTimer createExpectationTimer() {
        return new SystemTimeExpectationTimer();
    }

    @Override
    public long defaultTimeoutMillis() {
        return cfg.waitTimeout();
    }

    @Override
    public long defaultTickMillis() {
        return 100;
    }
}
