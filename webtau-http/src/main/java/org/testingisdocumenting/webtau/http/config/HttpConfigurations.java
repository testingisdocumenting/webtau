/*
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

package org.testingisdocumenting.webtau.http.config;

import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class HttpConfigurations {
    private static final ThreadLocal<Boolean> enabled = ThreadLocal.withInitial(() -> true);

    private static final List<HttpConfiguration> configurations = Collections.synchronizedList(
            ServiceLoaderUtils.load(HttpConfiguration.class));

    public static void add(HttpConfiguration configuration) {
        configurations.add(configuration);
    }

    public static void remove(HttpConfiguration configuration) {
        configurations.remove(configuration);
    }

    public static <E> E withDisabledConfigurations(Supplier<E> code) {
        try {
            disable();
            return code.get();
        } finally {
            enable();
        }
    }

    public static String fullUrl(String url) {
        if (!enabled.get()) {
            return url;
        }

        return fullUrlWithoutEnabledCheck(url);
    }

    public static String fullUrlWithoutEnabledCheck(String url) {
        String finalUrl = url;
        for (HttpConfiguration configuration : configurations) {
            finalUrl = configuration.fullUrl(finalUrl);
        }

        return finalUrl;
    }

    public static HttpHeader fullHeader(String fullUrl, String passedUrl, HttpHeader given) {
        if (! enabled.get()) {
            return given;
        }

        HttpHeader finalHeaders = given;
        for (HttpConfiguration configuration : configurations) {
            finalHeaders = configuration.fullHeader(fullUrl, passedUrl, finalHeaders);
        }

        return finalHeaders;
    }

    private static void disable() {
        enabled.set(false);
    }

    private static void enable() {
        enabled.set(true);
    }
}
