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

package org.testingisdocumenting.webtau.javarunner.report;

import org.testingisdocumenting.webtau.reporter.WebTauTest;
import org.testingisdocumenting.webtau.reporter.StepReporter;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg;

public class JavaBasedTest implements StepReporter {
    private final WebTauTest test;

    public JavaBasedTest(String id, String name) {
        test = new WebTauTest(getCfg().getWorkingDir());
        test.setId(id);
        test.setScenario(name);
    }

    public WebTauTest getTest() {
        return test;
    }

    @Override
    public void onStepStart(WebTauStep step) {
    }

    @Override
    public void onStepSuccess(WebTauStep step) {
        if (step.getNumberOfParents() == 0) {
            test.addStep(step);
        }
    }

    @Override
    public void onStepFailure(WebTauStep step) {
        if (step.getNumberOfParents() == 0) {
            test.addStep(step);
        }
    }
}
