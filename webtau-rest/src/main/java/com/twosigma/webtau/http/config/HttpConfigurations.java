/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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
