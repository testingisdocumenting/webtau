/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twosigma.webtau.reporter;

import com.twosigma.webtau.WebTauDsl;
import com.twosigma.webtau.page.PageElement;
import com.twosigma.webtau.report.ScreenshotStepPayload;

public class ScreenshotStepReporter implements StepReporter<PageElement, Void> {
    @Override
    public void onStepStart(TestStep<PageElement, Void> step) {
    }

    @Override
    public void onStepSuccess(TestStep<PageElement, Void> step) {
    }

    @Override
    public void onStepFailure(TestStep<PageElement, Void> step) {
        if (! WebTauDsl.wasBrowserUsed()) {
            return;
        }

        if (step.hasPayload(ScreenshotStepPayload.class)) {
            return;
        }

        step.addPayload(new ScreenshotStepPayload(WebTauDsl.takeScreenshotAsBase64()));
    }
}
