package com.twosigma.webtau.http.testserver;

public class TestServerJsonResponse implements TestServerResponse {
    private String response;

    public TestServerJsonResponse(String response) {
        this.response = response;
    }

    @Override
    public String responseBody(TestServerRequest request) {
        return response;
    }

    @Override
    public String responseType(TestServerRequest request) {
        return "application/json";
    }
}
