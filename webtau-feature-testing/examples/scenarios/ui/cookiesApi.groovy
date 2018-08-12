package scenarios.ui

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario("delete all cookies") {
    browser.open("/with-cookies")

    browser.cookies.add("test-cookie", "hello world")

    browser.reopen("/with-cookies")
    $("#cookies").should == "test-cookie=hello world"

    browser.cookies.deleteAll()
    browser.reopen("/with-cookies")
    $("#cookies").should == ""
}