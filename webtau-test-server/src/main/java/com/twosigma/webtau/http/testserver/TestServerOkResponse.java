package com.twosigma.webtau.http.testserver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TestServerOkResponse implements TestServerResponse {
    private final byte[] responseBody;
    private final String contentType;

    public TestServerOkResponse(byte[] responseBody, String contentType) {
        this.responseBody = responseBody;
        this.contentType = contentType;
    }

    @Override
    public byte[] responseBody(HttpServletRequest request) throws IOException, ServletException {
        return responseBody;
    }

    @Override
    public String responseType(HttpServletRequest request) {
        return contentType;
    }
}
