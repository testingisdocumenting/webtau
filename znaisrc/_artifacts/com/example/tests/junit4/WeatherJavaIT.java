package com.example.tests.junit4;

import com.twosigma.webtau.junit4.WebTauRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.twosigma.webtau.WebTauDsl.*;

@RunWith(WebTauRunner.class)
public class WeatherJavaIT {
    @Test
    public void checkWeather() {
        http.get("/weather", (header, body) -> {
            body.get("temperature").shouldBe(lessThan(100));
        });
    }
}
