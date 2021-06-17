package com.example.tests.junit5

import org.junit.jupiter.api.Test
import org.testingisdocumenting.webtau.junit5.WebTau

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

@WebTau
class WeatherGroovyTest {
    @Test
    void checkWeather() {
        http.get("/weather") {
            temperature.shouldBe < 100
        }
    }
}
