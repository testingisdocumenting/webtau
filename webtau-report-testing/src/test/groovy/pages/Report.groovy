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

package pages

import static org.testingisdocumenting.webtau.WebTauDsl.*

class Report {
    def fullScreenIcon = $(".fullscreen-icon")
    def collapsedHeader = $(".collapsed-http-header")
    def groupNames = $(".group-of-tests .navigation-entry-group-label")
    def testNames = $(".navigation-entry .label")
    def testSummaryMetaKey = $(".test-summary-metadata th").get("METADATA KEY")

    def responseData = $(".response .data")

    def steps = $(".step")
    def personaId = $(".step .persona-id")

    def tabNames = $(".tab-selection .tab-name")

    def cellValues = $("td").all()

    private def httpCalls = $(".test-http-call")
    private def cliCalls = $(".test-cli-call")

    def testSummaryHttpCallWarnings = $(".webtau-http-calls-warning")

    def openGroovyStandaloneReport(String reportName) {
        openReportFile(ReportLocation.groovyFeatureTestingFullUrl(reportName))
    }

    def openJunit5ExampleReport() {
        openReportFile(ReportLocation.javaJunit5FullUrl('webtau.report.html'))
    }

    def selectTest(String testName) {
        def navEntry = $('.navigation-entry .label').get(testName)
        navEntry.waitTo beVisible()
        navEntry.click()
    }

    def selectHttpCalls() {
        selectTab('HTTP calls')
    }

    def selectCliCalls() {
        selectTab('CLI calls')
    }

    def selectSteps() {
        selectTab('Steps')
    }

    def selectConfiguration() {
        selectTab('Configuration')
    }

    def selectEnvVars() {
        selectTab('Environment Variables')
    }

    def selectTab(String tabName) {
        tabNames.get(tabName).click()
    }

    def expandHttpCall(callNumber) {
        httpCalls.waitTo beVisible()
        httpCalls.get(callNumber).find(".collapse-toggle").click()
    }

    def expandCliCall(callNumber) {
        cliCalls.waitTo beVisible()
        cliCalls.get(callNumber).find(".collapse-toggle").click()
    }

    def standardCliOutput() {
        return $(".cli-output.standard")
    }

    private static def openReportFile(String fileName) {
        browser.open(fileName)
        $(".status-filter-area").waitTo beVisible()
    }
}
