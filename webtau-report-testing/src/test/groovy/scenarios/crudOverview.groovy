/*
 * Copyright 2020 webtau maintainers
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

scenario('http calls') {
    report.openGroovyStandaloneReport('rest/springboot/customerCrud-webtau-report.html')
    report.selectTest('CRUD operations for customer')

    report.selectHttpCalls()
    report.expandHttpCall(2)

    browser.doc.capture('report-crud-http-calls')

    report.selectSteps()
    report.steps.count.shouldBe > 2
    browser.doc.withAnnotations(
            browser.doc.badge(report.httpCallsTab).invertedColors(),
            browser.doc.badge(report.stepsTab).invertedColors())
            .capture('report-crud-steps')
}