package two.scenarios

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario('simple scenario B') {
    cfg.customValue.should == null
    cfg.anotherCustomValue.should == 42
    cfg.baseUrl.should == 'http://localhost:1010'
}