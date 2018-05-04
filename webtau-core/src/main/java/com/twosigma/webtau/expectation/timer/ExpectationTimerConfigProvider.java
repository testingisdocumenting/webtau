package com.twosigma.webtau.expectation.timer;

import com.twosigma.webtau.utils.ServiceUtils;

import java.util.List;

public class ExpectationTimerConfigProvider {
    private static List<ExpectationTimerConfig> configs = ServiceUtils.discover(ExpectationTimerConfig.class);

    public static ExpectationTimer createExpectationTimer() {
        validate();
        return configs.get(0).createExpectationTimer();
    }

    public static long defaultTickMillis() {
        validate();
        return configs.get(0).defaultTickMillis();
    }

    public static long defaultTimeoutMillis() {
        validate();
        return configs.get(0).defaultTimeoutMillis();
    }

    private static void validate() {
        if (configs.isEmpty()) {
            throw new RuntimeException("no " + ExpectationTimerConfig.class + " registered");
        }

        if (configs.size() > 1) {
            throw new RuntimeException("more than one " + ExpectationTimerConfig.class + " is registered");
        }
    }
}
