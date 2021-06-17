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

scenario('open report') {
    report.openGroovyStandaloneReport('rest/springboot/customerCrudSeparatedMissingMethod-webtau-report.html')
}

scenario('navigate to performance tab and validate size') {
    httpPerformance.selectOperationsPerformance()

    httpPerformance.operationsTableRows.count.waitTo == 3

    browser.doc.withAnnotations(browser.doc.badge(httpPerformance.operationsPerformanceTab))
            .capture('http-operations-performance-report')
}
