package scenarios.rest.redirect

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("redirect to weather") {
    http.get("/redirect") {
        temperature.shouldBe < 100
    }
}
