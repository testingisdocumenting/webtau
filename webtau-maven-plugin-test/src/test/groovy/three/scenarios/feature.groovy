package three.scenarios

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.scenario

scenario('simple scenario A') {
    cfg.valueA.should == 42
}