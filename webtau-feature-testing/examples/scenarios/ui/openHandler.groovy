package scenarios.ui

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario("page open handlers") {
    browser.open('/logged-in-user')
    $('#welcome').should == 'Welcome LoggedIn User'
}