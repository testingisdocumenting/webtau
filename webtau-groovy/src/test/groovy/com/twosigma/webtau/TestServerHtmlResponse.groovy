package com.twosigma.webtau

import com.twosigma.webtau.http.testserver.TestServerRequest
import com.twosigma.webtau.http.testserver.TestServerResponse

class TestServerHtmlResponse implements TestServerResponse {
    private String response

    TestServerHtmlResponse(String response) {
        this.response = response
    }

    @Override
    String responseBody(final TestServerRequest request) {
        return response
    }

    @Override
    String responseType(TestServerRequest request) {
        return "text/html"
    }
}
