package scenarios.rest.coverage

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("check fahrenheit temperature") {
    http.get("/city/NewYork") {
        weather.temperatureF.shouldBe > 80
    }
}

scenario("check celsius temperature") {
    http.get("/city/NewYork") {
        weather.temperatureC.shouldBe > 26
    }
}
