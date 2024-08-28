package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("send text to focused element") {
    browser.open("/search")
    $("#search-box").click()
    // focused-element-send-keys
    browser.focusedElement().setValue("search this")
    // focused-element-send-keys
    browser.focusedElement().sendKeys(browser.keys.enter)

    $("#message").should contain("searching for")
}

scenario("send text when no focused element") {
    browser.reopen("/search")
    browser.focusedElement().sendKeys("search this\n")
}