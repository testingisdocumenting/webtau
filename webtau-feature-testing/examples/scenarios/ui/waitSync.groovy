package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario("wait for element to appear") {
    calculation.start()

    calculation.feedback.waitToBe visible
    calculation.results.should == [100, 230]
}

scenario("wait for match") {
    calculation.start()
    calculation.results.waitTo == [100, 230]
}

scenario("wait for element to be enabled") {
    calculation.open()

    calculation.input.waitToBe enabled
    calculation.input.setValue(100)
}

scenario("wait for element to be hidden") {
    calculation.open()
    calculation.input.waitToBe enabled

    calculation.input.setValue('abc')
    calculation.calculate()

    calculation.error.waitToBe visible
    calculation.dismissError()
    calculation.error.waitToBe hidden
}

scenario("wait for element to be hidden reversed") {
    calculation.open()
    calculation.input.waitToNotBe disabled

    calculation.input.setValue('abc')
    calculation.calculate()

    calculation.error.waitToNotBe hidden
    calculation.dismissError()
    calculation.error.waitToNotBe visible
}