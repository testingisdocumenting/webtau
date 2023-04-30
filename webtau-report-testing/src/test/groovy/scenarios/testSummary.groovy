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

package scenarios

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario("failed test has failed step on summary") {
    report.openGroovyStandaloneReport("ui/failedMatchers-chrome-failed-webtau-report.html")
    report.selectTest("expect visible but hidden")

    report.step.should == ~/failed expecting by css #feedback to be visible/
}

scenario("failed step with children is auto expanded") {
    report.openGroovyStandaloneReport("rest/simpleGetAndFail-failed-webtau-report.html")
    report.selectTest("check weather")

    report.step.should containAll("failed executing HTTP GET", "expected: less than")

    browser.doc.capture('report-test-summary-failed-step')
}

scenario("failed summary displays actual value with markers") {
    report.openGroovyStandaloneReport("concept/failedMatcher-failed-webtau-report.html")
    report.selectTest("failed matcher actual extra info")
    report.styledText.get(4).should == "    [1, **2**, 3]"
}