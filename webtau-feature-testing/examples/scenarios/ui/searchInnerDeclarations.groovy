package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("search by specific query") {
    browser.open("/search")

    $("#search-box").setValue("search this")
    $("#search-box").sendKeys(browser.keys.enter)

    $("#results .result").count.shouldBe > 1
}