package scenarios.rest.coverage

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("no route defined") {
    http.get("/weather") {
        temperature.shouldBe > 40
    }
}
