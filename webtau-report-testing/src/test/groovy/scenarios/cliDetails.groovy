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

scenario('cli calls') {
    if (isWindows()) {
        return
    }

    report.openGroovyStandaloneReport('cli/simpleRun-webtau-report.html')
    report.selectTest('simple cli run')

    report.selectCliCalls()
    report.expandCliCall(1)

    report.stdCliOutput.should == ~/welcome to my script/

    browser.doc.capture('cli-calls')
}

scenario('timeout cli should have output captured') {
    if (isWindows()) {
        return
    }

    report.openGroovyStandaloneReport('cli/cliTimeout-failed-webtau-report.html')
    report.selectTest('sleep timeout')
    report.selectCliCalls()
    report.expandCliCall(1)
    report.stdCliOutput.should contain("welcome sleeping script")
}

static boolean isWindows() {
    return System.getProperty("os.name").startsWith("Windows")
}