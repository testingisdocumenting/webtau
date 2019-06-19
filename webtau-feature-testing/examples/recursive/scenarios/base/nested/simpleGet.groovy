package scenarios.base.nested

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario("simple get") {
    http.get("/weather") {
        temperature.shouldBe < 100
    }
}
