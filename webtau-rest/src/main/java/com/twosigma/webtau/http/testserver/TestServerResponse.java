package com.twosigma.webtau.http.testserver;

public interface TestServerResponse {
    String responseBody(TestServerRequest request);
    String responseType(TestServerRequest request);
}
