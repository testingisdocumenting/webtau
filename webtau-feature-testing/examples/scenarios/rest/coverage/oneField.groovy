package scenarios.rest.coverage

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("single field touch") {
    http.get("/city/London") {
        weather.temperature.shouldBe > 10
    }
}
