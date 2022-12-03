package com.example.tests.junit5;

import org.junit.jupiter.api.Test;
import org.testingisdocumenting.webtau.junit5.WebTau;

import static org.testingisdocumenting.webtau.WebTauDsl.*;

@WebTau
public class NewYorkWeatherJavaTest {
    @Test
    public void checkFahrenheitTemperature() {
        http.get("/city/NewYork", (header, body) -> {
            body.get("weather.temperatureF").shouldBe(greaterThan(80));
        });
    }

    @Test
    public void checkCelsiusTemperature() {
        http.get("/city/NewYork", (header, body) -> {
            body.get("weather.temperatureC").shouldBe(greaterThan(26));
        });
    }
}
