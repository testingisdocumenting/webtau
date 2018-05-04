package com.twosigma.webtau.http;

public interface HttpRequestBody {
    boolean isBinary();
    String type();
    String asString();
    default byte[] asBytes() {
        return asString().getBytes();
    }
}
