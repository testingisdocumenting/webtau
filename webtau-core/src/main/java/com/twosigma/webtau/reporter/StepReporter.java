package com.twosigma.webtau.reporter;

public interface StepReporter<C> {
    void onStepStart(TestStep<C> step);
    void onStepSuccess(TestStep<C> step);
    void onStepFailure(TestStep<C> step);
}
