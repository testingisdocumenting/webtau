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

package org.testingisdocumenting.webtau.runner.standalone

import org.testingisdocumenting.webtau.TestListener
import org.testingisdocumenting.webtau.reporter.WebTauTest

class TracingTestListener implements TestListener {
    List calls = []

    @Override
    void beforeFirstTest() {
        calls << 'beforeFirstTest'
    }

    @Override
    void beforeTestRun(WebTauTest test) {
        calls << 'beforeTestRun'
    }

    @Override
    void afterTestRun(WebTauTest test) {
        calls << 'afterTestRun'
    }

    @Override
    void afterAllTests() {
        calls << 'afterAllTests'
    }

    @Override
    void beforeFirstTestStatement(WebTauTest test) {
        calls << 'beforeFirstTestStatement'
    }

    @Override
    void afterLastTestStatement(WebTauTest test) {
        calls << 'afterLastTestStatement'
    }
}
