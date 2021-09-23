package scenarios.rest.openapi

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("unspecified operation warning") {
    http.get("/customers") {
    }
}
