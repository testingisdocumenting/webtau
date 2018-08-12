package scenarios.ui

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario('simple open') {
    browser.open("/search")
    $('#welcome').should == 'welcome to super search'
}