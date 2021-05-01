package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("scroll top") {
    browser.open("/scrolls")

    def container = $(".container")
    container.scrollTop.should == 0

    def sectionTwo = $("#two")
    sectionTwo.click()

    container.scrollTop.waitTo >= 50
}