package scenarios.rest

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario("check weather") {
    http.get("/weather") {
        temperature.shouldBe < 100
    }
}