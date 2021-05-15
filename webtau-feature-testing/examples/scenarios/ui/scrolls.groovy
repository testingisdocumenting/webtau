package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def container = $(".container")
def sectionTwo = $("#two")

scenario("scroll top property") {
    browser.reopen("/scrolls")

    sectionTwo.click()
    container.scrollTop.waitTo >= 50
}

scenario("scroll into view") {
    browser.reopen("/scrolls")

    // scrollIntoView-example
    sectionTwo.scrollIntoView()
    // scrollIntoView-example
    container.scrollTop.waitTo >= 50
}

scenario("scroll to bottom and top") {
    browser.reopen("/scrolls")
    container.scrollTop.should == 0

    container.scrollTo(30, 0)

    // scrollToBottom-example
    container.scrollToBottom()
    // scrollToBottom-example
    container.scrollTop.waitTo == container.scrollHeight.get() - container.clientHeight.get()
    container.scrollLeft.should == 30

    // scrollToTop-example
    container.scrollToTop()
    // scrollToTop-example

    container.scrollTop.waitTo == 0
    container.scrollLeft.should == 30
}

scenario("scroll to left and right") {
    browser.reopen("/scrolls")
    container.scrollLeft.should == 0

    container.scrollTo(0, 50)

    // scrollToRight-example
    container.scrollToRight()
    // scrollToRight-example
    container.scrollLeft.waitTo == container.scrollWidth.get() - container.clientWidth.get()
    container.scrollTop.should == 50

    // scrollToLeft-example
    container.scrollToLeft()
    // scrollToLeft-example
    container.scrollLeft.waitTo == 0
    container.scrollTop.should == 50
}

scenario("scroll to x and y") {
    browser.reopen("/scrolls")

    // scrollTo-example
    container.scrollTo(100, 50)
    // scrollTo-example

    container.scrollLeft.waitTo == 100
    container.scrollTop.should == 50
}