package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('simple open') {
    browser.open("/search")
    $('#welcome').should == 'welcome to super search'
}