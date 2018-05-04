package com.twosigma.webtau.http.config

import com.twosigma.webtau.http.HttpRequestHeader
import com.twosigma.webtau.http.HttpUrl
import com.twosigma.webtau.http.config.HttpConfiguration
import com.twosigma.webtau.cfg.WebTauConfig

class WebTauHttpConfiguration implements HttpConfiguration {
    private WebTauConfig cfg = WebTauConfig.INSTANCE

    @Override
    String fullUrl(String url) {
        if (HttpUrl.isFull(url)) {
            return url
        }

        return HttpUrl.concat(cfg.baseUrl, url)
    }

    @Override
    HttpRequestHeader fullHeader(HttpRequestHeader given) {
        return given
    }
}
