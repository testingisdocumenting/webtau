package com.twosigma.webtau.reporter;

import com.twosigma.webtau.reporter.StepReporter;
import com.twosigma.webtau.reporter.TestStep;
import com.twosigma.webtau.WebTauDsl;
import com.twosigma.webtau.page.PageElement;

public class ScreenshotStepReporter implements StepReporter<PageElement> {
    @Override
    public void onStepStart(TestStep<PageElement> step) {
    }

    @Override
    public void onStepSuccess(TestStep<PageElement> step) {
    }

    @Override
    public void onStepFailure(TestStep<PageElement> step) {
        if (! WebTauDsl.wasBrowserUsed()) {
            return;
        }

        if (step.hasPayload(ScreenshotStepPayload.class)) {
            return;
        }

        step.addPayload(new ScreenshotStepPayload(WebTauDsl.takeScreenshotAsBase64()));
    }
}
