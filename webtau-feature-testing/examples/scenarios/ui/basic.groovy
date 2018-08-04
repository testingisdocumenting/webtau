package scenarios.ui

import static com.twosigma.webtau.WebTauDsl.$
import static com.twosigma.webtau.WebTauDsl.browser
import static com.twosigma.webtau.WebTauGroovyDsl.scenario

scenario('simple open') {
    browser.open("/search")
    $('#welcome').should == 'welcome to super search'
}