package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def searchBox = $("#search-box")
def numberOfResults = $(".result").count

scenario('send keys to browser') {
    browser.open("/search")
    searchBox.click()

    browser.sendKeys("search this")
    browser.sendKeys(browser.keys.enter)

    numberOfResults.shouldBe > 1
}