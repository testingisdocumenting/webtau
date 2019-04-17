package com.twosigma.webtau.runner.standalone

import com.twosigma.webtau.reporter.StepReporter
import com.twosigma.webtau.reporter.TestStep

class ForbidStepsOutsideScenarioStepListener implements StepReporter {
    @Override
    void onStepStart(TestStep step) {
        throw new UnsupportedOperationException("executing <" + step.getInProgressMessage() + "> outside of scenario is " +
                "not supported")
    }

    @Override
    void onStepSuccess(TestStep step) {

    }

    @Override
    void onStepFailure(TestStep step) {

    }
}
