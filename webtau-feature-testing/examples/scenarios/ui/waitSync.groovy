package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static pages.Pages.*

scenario("wait for element to appear") {
    calculation.start()

    calculation.feedback.waitTo beVisible()
    calculation.results.should == [100, 230]
}

scenario("wait for match") {
    calculation.start()
    calculation.results.waitTo == [100, 230]
}

scenario("wait for element to be enabled") {
    calculation.open()

    calculation.input.waitTo beEnabled()
    calculation.input.setValue(100)
}

scenario("wait for element to be hidden") {
    calculation.open()
    calculation.input.waitTo beEnabled()

    calculation.input.setValue('abc')
    calculation.calculate()

    calculation.error.waitTo beVisible()
    calculation.dismissError()
    calculation.error.waitTo beHidden()
}

scenario("wait for element to be hidden reversed") {
    calculation.open()
    calculation.input.waitToNot beDisabled()

    calculation.input.setValue('abc')
    calculation.calculate()

    calculation.error.waitToNot beHidden()
    calculation.dismissError()
    calculation.error.waitToNot beVisible()
}