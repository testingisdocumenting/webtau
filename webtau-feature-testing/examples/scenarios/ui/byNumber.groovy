package scenarios.ui

import static com.twosigma.webtau.WebTauDsl.$
import static com.twosigma.webtau.WebTauDsl.open
import static com.twosigma.webtau.WebTauGroovyDsl.scenario

scenario("filter by number") {
    open("/finders-and-filters")
    $("#menu ul li a").get(2).should == 'orders'
}
