package scenarios.concept

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("first test") {
    terminateAll('hard stop of the rest of the tests')
}

scenario("second test") {
    browser.open("/app")
}

scenario("third test") {
    http.get("/weather") {
        temperature.shouldBe < 100
    }
}