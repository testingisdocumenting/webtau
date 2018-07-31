package scenarios.ui

import static com.twosigma.webtau.WebTauDsl.$
import static com.twosigma.webtau.WebTauDsl.open
import static com.twosigma.webtau.WebTauGroovyDsl.scenario

scenario("filter by text") {
    open("/finders-and-filters")
    $("#menu ul li a").get("orders").should == 'orders'
}
