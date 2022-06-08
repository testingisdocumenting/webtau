package scenarios.rest.headers

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("simple get") {
    http.get("/weather") {
        temperature.should == 88
    }
}