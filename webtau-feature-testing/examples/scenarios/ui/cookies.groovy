package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("open browser") {
    browser.open("/cookies")
}

scenario("add cookies") {
    browser.cookies.add("cookie-a", "hello")
    browser.cookies.add("cookie-b", "world")
}

scenario("validate added cookies") {
    browser.reopen("/cookies")
    $("#cookies").should == "cookie-a=hello; cookie-b=world"
}

scenario("delete named cookie") {
    browser.cookies.delete("cookie-b")
}

scenario("validate deleted cookies") {
    browser.reopen("/cookies")
    $("#cookies").should == "cookie-a=hello"
}

scenario("add more cookies") {
    browser.cookies.add("cookie-a", "hello")
    browser.cookies.add("cookie-b", "world")
}

scenario("get all cookies") {
    browser.cookies.get("cookie-a").should == "hello"
    browser.cookies.get("cookie-b").should == "world"

    browser.cookies.getAll().should == ["cookie-a": "hello", "cookie-b": "world"]
}

scenario("delete all cookies") {
    browser.cookies.deleteAll()
}

scenario("validate after delete all cookies") {
    browser.reopen("/cookies")
    $("#cookies").should == ""
}
