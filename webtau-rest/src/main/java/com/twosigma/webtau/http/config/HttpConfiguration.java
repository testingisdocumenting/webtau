package com.twosigma.webtau.http.config;

import com.twosigma.webtau.http.HttpRequestHeader;

public interface HttpConfiguration {
    String fullUrl(String url);
    HttpRequestHeader fullHeader(HttpRequestHeader given);
}
