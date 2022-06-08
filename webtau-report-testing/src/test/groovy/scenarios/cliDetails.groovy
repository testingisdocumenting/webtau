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

    selectTestAndCli('cli/simpleRun-webtau-report.html', 'simple cli run')
    report.expandCliCall(1)

    report.stdCliOutput.should == ~/welcome to my script/

    browser.doc.capture('cli-calls')
}

scenario('timeout cli should have output captured') {
    if (isWindows()) {
        return
    }

    selectTestAndCli('cli/cliTimeout-failed-webtau-report.html', 'sleep timeout')
    report.expandCliCall(1)

    report.stdCliOutput.should contain("welcome sleeping script")
}

scenario('cli calls persona column') {
    if (isWindows()) {
        return
    }

    selectTestAndCli('cli/cliCommonEnvVars-webtau-report.html', 'persona foreground process var from config')
    report.cliPersonaIds.should == ['Alice', 'Bob']
}

static void selectTestAndCli(String reportName, String testName) {
    report.openGroovyStandaloneReport(reportName)
    report.selectTest(testName)
    report.selectCliCalls()
}

static boolean isWindows() {
    return System.getProperty("os.name").startsWith("Windows")
}