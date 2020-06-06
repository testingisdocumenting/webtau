/*
 * Copyright 2020 webtau maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.webtau.reporter;

public class ScopeLimitingStepReporter implements StepReporter {
    private final StepReporter stepReporter;
    private final int maxLevel;

    public ScopeLimitingStepReporter(StepReporter stepReporter, int maxLevel) {
        this.stepReporter = stepReporter;
        this.maxLevel = maxLevel;
    }

    @Override
    public void onStepStart(TestStep step) {
        checkAndDelegate(step, () -> stepReporter.onStepStart(step));
    }

    @Override
    public void onStepSuccess(TestStep step) {
        checkAndDelegate(step, () -> stepReporter.onStepSuccess(step));
    }

    @Override
    public void onStepFailure(TestStep step) {
        checkAndDelegate(step, () -> stepReporter.onStepFailure(step));
    }

    private void checkAndDelegate(TestStep step, Runnable code) {
        int currentLevel = step.getNumberOfParents() + 1;
        if (currentLevel <= maxLevel) {
            code.run();
        }
    }
}
