package com.example.tests.junit5.config;

import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.config.WebTauHttpConfiguration;
import static org.testingisdocumenting.webtau.WebTauDsl.*;

public class HttpPersonaAuthHeaderProvider implements WebTauHttpConfiguration {
    @Override
    public HttpHeader fullHeader(String fullUrl, String passedUrl, HttpHeader given) {
        String token = generateTokenBasedOnPersona();
        return given.with("Authorization", "Bearer " + token);
    }

    private String generateTokenBasedOnPersona() {
        if (getCurrentPersona().isDefault()) { // check if we are inside persona context or outside
            return generateDefaultToken();
        }

        return generateTokenForSystemUserId(
                getCurrentPersona().getPayload().getOrDefault("authId", "").toString()); // use persona payload to generate required token
    }

    private String generateTokenForSystemUserId(String systemUserId) {
        return "dummy:" + systemUserId; // this is where you generate specific user auth token
    }

    private String generateDefaultToken() {
        return "dummy:default-user"; // this is where you generate default user auth token
    }
}
