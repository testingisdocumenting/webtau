package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def welcomeMessage = $('#welcome')

scenario('simple open') {
    browser.open("/search")
    welcomeMessage.should == 'welcome to super search'
}