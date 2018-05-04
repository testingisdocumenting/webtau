package ui

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario("filter by text") {
    open("/finders-and-filters")
    $("#menu ul li a").get("orders").should == 'orders'
}
