package scenarios.rest.redirect

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("redirect disabled") {
    http.get("/redirect") {
        statusCode.should == 302
        temperature.should == null
    }
}
