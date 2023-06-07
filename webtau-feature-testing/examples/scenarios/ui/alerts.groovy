package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("dismiss alert") {
    browser.reopen("/alerts")
    $("#alert").click()

    // dismiss-alert
    browser.alert.dismiss()
    // dismiss-alert
}

scenario("accept alert") {
    browser.reopen("/alerts")
    $("#confirm").click()
    // accept-alert
    browser.alert.accept()
    // accept-alert
    $("#message").should == "confirmed"
}

scenario("wait on alert text") {
    browser.reopen("/alerts")
    $("#alert-delay").click()

    // wait-alert-text
    browser.alert.text.waitTo == "hello delayed\nalert"
    browser.alert.accept()
    // wait-alert-text

    $("#just-button").click()
}

scenario("no alert text") {
    browser.reopen("/alerts")
    browser.alert.text.should == null
}
