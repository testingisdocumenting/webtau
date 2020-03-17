package pages


import static org.testingisdocumenting.webtau.WebTauDsl.*

class Report {
    def fullScreenIcon = $(".fullscreen-icon")
    def collapsedHeader = $(".collapsed-http-header")
    def groupNames = $(".group-of-tests .navigation-entry-group-label")
    def testSummaryMetaKey = $(".test-summary-metadata th").get("METADATA KEY")

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
        $(".tab-selection .tab-name").get('HTTP calls').click()
    }

    def expandHttpCall(number) {
        def httpCalls = $(".test-http-call")
        httpCalls.waitTo beVisible()
        httpCalls.get(number).find(".collapse-toggle").click()
    }

    private static def openReportFile(String fileName) {
        browser.open(fileName)
        $(".status-filter-area").waitTo beVisible()
    }
}
