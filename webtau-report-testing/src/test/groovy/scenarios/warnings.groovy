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

scenario('test summary http warning') {
    report.openGroovyStandaloneReport('rest/openapi/unspecifiedUrl-webtau-report.html')
    report.selectTest('unspecified operation warning')

    report.testSummaryHttpCallWarnings.should == ~/customers is not found in OpenAPI spec/
    browser.doc.capture('http-open-api-warning')
}