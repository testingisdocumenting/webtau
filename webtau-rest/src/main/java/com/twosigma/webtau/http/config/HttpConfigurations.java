package com.twosigma.webtau.http.config;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.twosigma.webtau.http.HttpRequestHeader;
import com.twosigma.webtau.utils.ServiceUtils;

public class HttpConfigurations {
    private static final AtomicBoolean enabled = new AtomicBoolean(true);

    private static List<HttpConfiguration> configurations = ServiceUtils.discover(HttpConfiguration.class);

    public static void disable() {
        enabled.set(false);
    }

    public static void enable() {
        enabled.set(true);
    }

    public static void add(HttpConfiguration configuration) {
        configurations.add(configuration);
    }

    public static void remove(HttpConfiguration configuration) {
        configurations.remove(configuration);
    }

    public static String fullUrl(String url) {
        if (! enabled.get()) {
            return url;
        }

        String finalUrl = url;
        for (HttpConfiguration configuration : configurations) {
            finalUrl = configuration.fullUrl(finalUrl);
        }

        return finalUrl;
    }

    public static HttpRequestHeader fullHeader(HttpRequestHeader given) {
        if (! enabled.get()) {
            return given;
        }

        HttpRequestHeader finalHeaders = given;
        for (HttpConfiguration configuration : configurations) {
            finalHeaders = configuration.fullHeader(finalHeaders);
        }

        return finalHeaders;
    }
}
