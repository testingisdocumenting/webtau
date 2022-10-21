package com.example.tests.junit5;

import org.junit.jupiter.api.Test;
import org.testingisdocumenting.webtau.junit5.WebTau;

import static org.testingisdocumenting.webtau.WebTauDsl.*;

@WebTau
public class WeatherJavaTest {
    @Test
    public void checkWeather() {
        defer(() -> {
            trace("will always be executed at the end", "key", "value");
        });

        http.get("/weather", (header, body) -> {
            body.get("temperature").shouldBe(lessThan(100));
        });
    }
}
