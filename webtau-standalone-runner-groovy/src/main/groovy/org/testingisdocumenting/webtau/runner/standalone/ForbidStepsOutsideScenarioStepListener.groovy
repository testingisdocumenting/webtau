package org.testingisdocumenting.webtau.runner.standalone

import org.testingisdocumenting.webtau.reporter.StepReporter
import org.testingisdocumenting.webtau.reporter.TestStep

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
