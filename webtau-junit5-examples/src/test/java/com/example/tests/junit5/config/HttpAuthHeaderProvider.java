package com.example.tests.junit5.config;

import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.config.WebTauHttpConfiguration;

import static org.testingisdocumenting.webtau.WebTauDsl.*;

public class HttpAuthHeaderProvider implements WebTauHttpConfiguration {
    @Override
    public HttpHeader fullHeader(String fullUrl, String passedUrl, HttpHeader given) {
        String token = step("generate auth token", () -> "jwt-token");
        return given.with("Authorization", "Bearer " + token);
    }
}
