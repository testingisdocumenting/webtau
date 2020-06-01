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

scenario('open report') {
    report.openGroovyStandaloneReport('concept/beforeAllAfterAllSuccess-webtau-report.html')
}

scenario('validate setup and teardown presence') {
    report.groupNames.should == ['Setup', 'beforeAllAfterAllSuccess.groovy', 'Teardown']
    report.testNames.should == ['before first test', 'dummy scenario', 'after all tests']
}
