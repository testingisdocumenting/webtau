/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.termui;

import org.testingisdocumenting.webtau.TestListener;
import org.testingisdocumenting.webtau.reporter.WebTauTest;

import java.util.List;

public class TermUiTestListener implements TestListener {
    private final TermUi termUi = TermUi.INSTANCE;

    @Override
    public void beforeFirstTest() {
        if (!TermUiConfig.isTermUiEnabled()) {
            System.setProperty("java.awt.headless", "true");
        }
    }

    @Override
    public void beforeTestRun(WebTauTest test) {
    }

    @Override
    public void afterTestRun(WebTauTest test) {
        termUi.updateTest(test);
    }

    @Override
    public void afterTestsRegistration(List<WebTauTest> tests) {
        if (!TermUiConfig.isTermUiEnabled()) {
            return;
        }

        tests.forEach(termUi::registerTest);
    }

    @Override
    public void afterAllTests() {
    }
}
