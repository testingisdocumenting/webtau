package com.example.tests.junit4

import org.junit.Test
import org.junit.runner.RunWith
import org.testingisdocumenting.webtau.junit4.WebTauRunner

import static org.testingisdocumenting.webtau.WebTauDsl.graphql

@RunWith(WebTauRunner.class)
class GraphQLWeatherGroovyIT {
    @Test
    void checkWeather() {
        def query = "{ weather { temperature } }";
        graphql.execute(query) {
            weather.temperature.shouldBe < 100
        }
    }
}
