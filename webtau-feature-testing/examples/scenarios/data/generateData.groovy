package scenarios.data

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('generate guid') {
    def id = data.guid.generate()
    id.should == ~/^[a-z0-9]{8}.*/
}
