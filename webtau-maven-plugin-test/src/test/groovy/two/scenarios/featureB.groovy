package two.scenarios

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('simple scenario B') {
    cfg.customValue.should == null
    cfg.anotherCustomValue.should == 42
    cfg.url.should == 'http://localhost:1010'
}