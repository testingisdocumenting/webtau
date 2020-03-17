package com.example.tests.junit4

import org.testingisdocumenting.webtau.junit4.WebTauRunner
import org.junit.Test
import org.junit.runner.RunWith

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

@RunWith(WebTauRunner.class)
class WeatherGroovyIT {
    @Test
    void checkWeather() {
        http.get("/weather") {
            temperature.shouldBe < 100
        }
    }
}
