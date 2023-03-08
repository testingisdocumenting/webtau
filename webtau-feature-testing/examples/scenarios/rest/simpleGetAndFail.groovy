package scenarios.rest

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("check weather") {
    http.get("/weather") {
        temperature.shouldBe < 10
    }
}