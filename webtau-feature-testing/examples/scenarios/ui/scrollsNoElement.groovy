package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def wrongElement = $("#wrong-id")

scenario("scroll into view using non existing element") {
    browser.reopen("/scrolls")
    wrongElement.scrollIntoView()
}
