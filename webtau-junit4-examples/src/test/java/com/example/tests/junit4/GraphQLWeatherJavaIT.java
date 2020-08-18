package com.example.tests.junit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.testingisdocumenting.webtau.junit4.WebTauRunner;

import static org.testingisdocumenting.webtau.Matchers.lessThan;
import static org.testingisdocumenting.webtau.WebTauDsl.graphql;

@RunWith(WebTauRunner.class)
public class GraphQLWeatherJavaIT {
    @Test
    public void checkWeather() {
        String query = "{ weather { temperature } }";
        graphql.execute(query, (header, body) -> {
            body.get("data.weather.temperature").shouldBe(lessThan(100));
        });
    }
}
