package pages

import static com.twosigma.webtau.WebTauDsl.*

class Report {
    def open(String reportName) {
        browser.open(ReportLocation.fullUrl(reportName))
    }

    def selectTest(String testName) {
        def navEntry = $('.navigation-entry .label').get(testName)
        navEntry.waitTo beVisible()
        navEntry.click()
    }
}
