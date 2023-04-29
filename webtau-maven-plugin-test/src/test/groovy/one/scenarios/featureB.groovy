package one.scenarios

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('simple scenario B') {
    cfg.customValue.should == 42
    cfg.url.should == 'http://localhost:8080'
}