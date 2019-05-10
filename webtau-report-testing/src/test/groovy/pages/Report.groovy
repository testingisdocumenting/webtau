package pages


import static com.twosigma.webtau.WebTauDsl.*

class Report {
    def fullScreenIcon = $(".fullscreen-icon")
    def collapsedHeader = $(".collapsed-http-header")

    def open(String reportName) {
        browser.open(ReportLocation.fullUrl(reportName))
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
