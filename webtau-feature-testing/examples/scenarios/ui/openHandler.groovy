package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("page open handlers") {
    browser.open('/logged-in-user')
    $('#welcome').should == 'Welcome LoggedIn User'
}

scenario("page open handlers during re-open") {
    browser.localStorage.clear()

    browser.reopen('/logged-in-user')
    $('#welcome').should == 'Welcome LoggedIn User'
}