package scenarios.rest.redirect

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario("redirect to weather") {
    http.get("/redirect") {
        temperature.shouldBe < 100
    }
}
