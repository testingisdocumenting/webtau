package scenarios.ui

import static com.twosigma.webtau.WebTauDsl.$
import static com.twosigma.webtau.WebTauDsl.browser
import static com.twosigma.webtau.WebTauGroovyDsl.scenario

def searchBox = $('#search-box')
def numberOfResults = searchBox.count

scenario('search by specific query') {
    browser.open('/search')

    searchBox.setValue('search this')
    searchBox.sendKeys("\n")

    numberOfResults.shouldBe > 1
}