package org.testingisdocumenting.webtau.http.testserver;

import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

import static org.testingisdocumenting.webtau.http.testserver.ResponseUtils.echoHeaders;

public class TestServerRequestHeaderAndBodyEcho implements TestServerResponse {
    private final int statusCode;

    public TestServerRequestHeaderAndBodyEcho(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public byte[] responseBody(HttpServletRequest request) {
        try {
            return IOUtils.toByteArray(request.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> responseHeader(HttpServletRequest request) {
        return echoHeaders(request);
    }

    @Override
    public String responseType(HttpServletRequest request) {
        return "application/json";
    }

    @Override
    public int responseStatusCode() {
        return statusCode;
    }
}
