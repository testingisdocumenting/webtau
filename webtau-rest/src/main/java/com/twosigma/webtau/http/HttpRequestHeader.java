package com.twosigma.webtau.http;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class HttpRequestHeader {
    public static final HttpRequestHeader EMPTY = new HttpRequestHeader(Collections.emptyMap());

    private Map<String, String> properties;

    public HttpRequestHeader(Map<String, String> properties) {
        this.properties = properties;
    }

    public void forEachProperty(BiConsumer<String, String> consumer) {
        properties.forEach(consumer);
    }

    public HttpRequestHeader merge(Map<String, String> properties) {
        Map<String, String> copy = new LinkedHashMap<>(this.properties);
        copy.putAll(properties);

        return new HttpRequestHeader(copy);
    }

    @Override
    public String toString() {
        return properties.toString();
    }
}
