package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('open and validate title and url') {
    browser.open("/search")
    browser.url.path.should == "/search"

    browser.title.waitTo == "Super Search"
    browser.title.should == "Super Search"
}