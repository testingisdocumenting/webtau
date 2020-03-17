package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauDsl.$
import static org.testingisdocumenting.webtau.WebTauDsl.getCfg
import static org.testingisdocumenting.webtau.WebTauDsl.open
import static org.testingisdocumenting.webtau.WebTauGroovyDsl.scenario

scenario("accessing custom config value") {
    open("/finders-and-filters")
    $("#menu ul li a").get(2).should == cfg.userName
}
