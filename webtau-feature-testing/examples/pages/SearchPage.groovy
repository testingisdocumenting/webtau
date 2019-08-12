package pages

import static com.twosigma.webtau.WebTauDsl.*

class SearchPage {
    def welcomeMessage = $('#welcome')
    def searchMessage = $('#message')
    def box = $('#search-box')
    def results = $('#results .result')
    def numberOfResults = results.count

    def submit(query) {
        browser.open("/search")

        box.setValue(query)
        box.sendKeys("\n")
    }
}