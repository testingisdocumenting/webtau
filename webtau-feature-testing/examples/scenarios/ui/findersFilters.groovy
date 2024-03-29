package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("open browser") {
    browser.open("/finders-and-filters")
}

scenario("by css id") {
    def welcomeMessage = $("#welcome")
    welcomeMessage.should == "hello"
}

scenario("by css first matched") {
    def menu = $("ul li a")
    menu.should == "book"
}

scenario("by css all matched") {
    def menu = $("ul li a")
    menu.should == ["book", "orders", "help"]
}

scenario("by css and filter by number") {
    def ordersMenu = $("ul li a").get(2)
    ordersMenu.should == "orders"
}

scenario("by css and filter by text") {
    def ordersMenu = $("ul li a").get("orders")
    ordersMenu.should == "orders"
}

scenario("by css and filter by regexp") {
    def ordersMenu = $("ul li a").get(~/ord/)
    ordersMenu.should == "orders"
}

scenario("by css and filter by number and nested css") {
    def ordersMenu = $("ul li").get(2).find("a")
    ordersMenu.should == "orders"
}

scenario("by buttons css and filter by text") {
    def cssButton = $("button").get("of CSS")
    cssButton.should == "of CSS"
}

scenario("immediate parent") {
    def welcomeMessage = $("#welcome")
    def wrapper = welcomeMessage.parent()

    wrapper.attribute("class").should == "wrapper-class"
}

scenario("parent by css") {
    def welcomeMessage = $("#welcome")
    def mainWrapper = welcomeMessage.parent(".top-section")

    mainWrapper.attribute("class").should == "top-section"
}

scenario("parent by css can't find") {
    def welcomeMessage = $("#welcome")
    def mainWrapper = welcomeMessage.parent(".top-section-wrong-name")

    mainWrapper.attribute("class").should == null
}

scenario("filter by geometry") {
    // nearby-example
    def button = $("button")
    def helloMessage = $("#hello-message")

    button.nearby(helloMessage).should == "World2"
    // nearby-example
    // nearby-chain-example
    button.get(~/World/).nearby(helloMessage).should == "World2"
    // nearby-chain-example
}
