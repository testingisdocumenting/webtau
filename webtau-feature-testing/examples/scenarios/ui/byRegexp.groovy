package scenarios.ui

import static com.twosigma.webtau.WebTauDsl.$
import static com.twosigma.webtau.WebTauDsl.open
import static com.twosigma.webtau.WebTauGroovyDsl.scenario

scenario("filter by regexp") {
    open("/finders-and-filters")
    $("#menu ul li a").get(~/ord.../).should == 'orders'
}
