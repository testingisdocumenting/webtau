package ui

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario("filter by regexp") {
    open("/finders-and-filters")
    $("#menu ul li a").get(~/ord.../).should == 'orders'
}
