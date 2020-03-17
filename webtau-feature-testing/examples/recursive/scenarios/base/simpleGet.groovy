package scenarios.base

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("simple get") {
    http.get("/weather") {
        temperature.shouldBe < 100
    }
}
