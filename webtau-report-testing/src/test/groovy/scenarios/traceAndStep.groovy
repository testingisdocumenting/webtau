/*
 * Copyright 2021 webtau maintainers
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

scenario('check steps after trace') {
    report.openGroovyStandaloneReport('concept/trace-webtau-report.html')
    report.selectTest("trace key values")
    report.selectSteps()

    report.keyValuesKeys.should == ["k1", "k2", "k3", "k4"]
}

scenario('step with key values') {
    report.openGroovyStandaloneReport('concept/stepGroup-webtau-report.html')
    report.selectTest("wrap as step with input")
    report.selectSteps()

    report.keyValuesKeys.should == ["url", "port"]

    browser.doc.capture('report-step-key-value')
}

