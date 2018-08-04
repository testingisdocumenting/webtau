package scenarios.ui

import static com.twosigma.webtau.WebTauDsl.$
import static com.twosigma.webtau.WebTauDsl.browser
import static com.twosigma.webtau.WebTauGroovyDsl.scenario

def welcomeMessage = $('#welcome')

scenario('simple open') {
    browser.open("/search")
    welcomeMessage.should == 'welcome to super search'
}