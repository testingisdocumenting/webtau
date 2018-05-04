package pages

import static com.twosigma.webtau.WebTauGroovyDsl.*

class SearchPage {
    def welcomeMessage = $('#welcome')
    def box = $('#search-box')
    def numberOfResults = $('#results .result').count

    void open() {
        reopen("/search")
    }

    def submit = action("submitting search value '<query>'") {
        box.setValue(it.query)
        box.sendKeys("\n")
    }
}