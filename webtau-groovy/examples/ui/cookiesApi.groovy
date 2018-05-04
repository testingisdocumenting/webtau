package ui

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario("delete all cookies") {
    open("/with-cookies")

    cookies.add("test-cookie", "hello world")

    reopen("/with-cookies")
    $("#cookies").should == "test-cookie=hello world"

    cookies.deleteAll()
    reopen("/with-cookies")
    $("#cookies").should == ""
}