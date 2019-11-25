package two.scenarios

import static com.twosigma.webtau.WebTauGroovyDsl.scenario

scenario('simple scenario A') {
    2.should == 2
}