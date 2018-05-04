package ui

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario("accessing custom config value") {
    open("/finders-and-filters")
    $("#menu ul li a").get(2).should == cfg.userName
}
