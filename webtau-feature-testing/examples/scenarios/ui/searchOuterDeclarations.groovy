package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def searchBox = $("#search-box")
def numberOfResults = $("#results .result").count

scenario("search by specific query") {
    browser.open("/search")

    searchBox.setValue("search this")
    searchBox.sendKeys("\n")

    numberOfResults.shouldBe > 1
}