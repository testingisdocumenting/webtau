package scenarios.rest.openapi

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("open api validation") {
    http.post("/customers", [firstName: "First", lastName: "Last"]) {
       firstName.should == "First"
    }
}
