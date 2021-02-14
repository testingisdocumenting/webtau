package scenarios.rest.openapi

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("open api validation") {
    http.post("/employee", [firstName: 'First']) {
        statusCode.shouldBe > 0
    }
}
