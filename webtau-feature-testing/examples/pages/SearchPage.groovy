package pages

import static org.testingisdocumenting.webtau.WebTauDsl.*

class SearchPage {
    def header = $("#header")
    def welcomeMessage = $("#welcome")
    def searchMessage = $("#message")
    def box = $("#search-box")

    def resultsArea = $("#results")
    def results = $("#results .result")
    def numberOfResults = results.count

    def submit(query) {
        browser.open("/search")

        box.setValue(query)
        box.sendKeys(browser.keys.enter)
    }
}