/*
 * Copyright 2022 webtau maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.tests.junitlike.cfg;

import org.testingisdocumenting.webtau.cfg.WebTauConfig;
import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.config.WebTauHttpConfiguration;
import org.testingisdocumenting.webtau.utils.UrlUtils;

public class DynamicPortBaseUrlConfig implements WebTauHttpConfiguration {
    private static final String SPRING_BOOT_EXAMPLE_URL_PREFIX = "/customers";
    private static final String DEFAULT_SPRINGBOOT_APP_PORT = "8080";

    @Override
    public String fullUrl(String url) {
        if (url.contains(SPRING_BOOT_EXAMPLE_URL_PREFIX)) {
            int prefixIdx = url.indexOf(SPRING_BOOT_EXAMPLE_URL_PREFIX);
            return baseUrl() + url.substring(prefixIdx);
        }

        if (UrlUtils.isFull(url)) {
            return url;
        }

        return UrlUtils.concat(WebTauConfig.getCfg().getUrl(), url);
    }

    @Override
    public HttpHeader fullHeader(String fullUrl, String passedUrl, HttpHeader given) {
        return given;
    }

    public static String baseUrl() {
        return "http://localhost:" + getSpringbootAppPort();
    }

    public static String getSpringbootAppPort() {
        String port = System.getProperty("springboot.http.port", DEFAULT_SPRINGBOOT_APP_PORT);
        if (port == null || port.isEmpty()) {
            return DEFAULT_SPRINGBOOT_APP_PORT;
        }

        return port;
    }
}
