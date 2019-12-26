package pages


import static com.twosigma.webtau.WebTauDsl.*

class Report {
    def fullScreenIcon = $(".fullscreen-icon")
    def collapsedHeader = $(".collapsed-http-header")
    def groupNames = $(".group-of-tests .navigation-entry-group-label")

    def openGroovyStandaloneReport(String reportName) {
        browser.open(ReportLocation.groovyFeatureTestingFullUrl(reportName))
    }

    def openJunit5ExampleReport() {
        browser.open(ReportLocation.javaJunit5FullUrl('webtau.report.html'))
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
}
