package scenarios.ui

import static com.twosigma.webtau.WebTauGroovyDsl.*
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