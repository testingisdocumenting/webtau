package com.twosigma.webtau.reporter;

import com.twosigma.webtau.utils.ServiceLoaderUtils;

import java.util.Set;

public class StepReporters {
    private static Set<StepReporter> reporters = ServiceLoaderUtils.load(StepReporter.class);

    public static void add(StepReporter reporter) {
        reporters.add(reporter);
    }

    public static void remove(StepReporter reporter) {
        reporters.remove(reporter);
    }

    public static void onStart(TestStep step) {
        reporters.forEach(r -> r.onStepStart(step));
    }

    public static void onSuccess(TestStep step) {
        reporters.forEach(r -> r.onStepSuccess(step));
    }

    public static void onFailure(TestStep step) {
        reporters.forEach(r -> r.onStepFailure(step));
    }
}
