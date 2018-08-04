package pages

import static com.twosigma.webtau.WebTauDsl.$
import static com.twosigma.webtau.WebTauDsl.browser

class SearchPage {
    def welcomeMessage = $('#welcome')
    def box = $('#search-box')
    def numberOfResults = $('#results .result').count

    def submit(query) {
        browser.open("/search")

        box.setValue(query)
        box.sendKeys("\n")
    }
}