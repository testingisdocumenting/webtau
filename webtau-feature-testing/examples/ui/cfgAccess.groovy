package ui

import static com.twosigma.webtau.WebTauDsl.$
import static com.twosigma.webtau.WebTauDsl.getCfg
import static com.twosigma.webtau.WebTauDsl.open
import static com.twosigma.webtau.WebTauGroovyDsl.scenario

scenario("accessing custom config value") {
    open("/finders-and-filters")
    $("#menu ul li a").get(2).should == cfg.userName
}
