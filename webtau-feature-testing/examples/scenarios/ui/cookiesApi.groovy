package scenarios.ui

import static com.twosigma.webtau.WebTauDsl.$
import static com.twosigma.webtau.WebTauDsl.cookies
import static com.twosigma.webtau.WebTauDsl.open
import static com.twosigma.webtau.WebTauDsl.reopen
import static com.twosigma.webtau.WebTauGroovyDsl.scenario

scenario("delete all cookies") {
    open("/with-cookies")

    cookies.add("test-cookie", "hello world")

    reopen("/with-cookies")
    $("#cookies").should == "test-cookie=hello world"

    cookies.deleteAll()
    reopen("/with-cookies")
    $("#cookies").should == ""
}