package scenarios.ui

import static com.twosigma.webtau.WebTauDsl.$
import static com.twosigma.webtau.WebTauDsl.browser
import static com.twosigma.webtau.WebTauGroovyDsl.scenario

scenario('search by specific query') {
    browser.open('/search')

    $('#search-box').setValue('search this')
    $('#search-box').sendKeys("\n")

    $('#results .result').count.shouldBe > 1
}