/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.http.request;

import org.testingisdocumenting.webtau.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpQueryParams {
    public static final HttpQueryParams EMPTY = new HttpQueryParams(Collections.emptyMap());

    private final Map<String, Object> params;
    private final String asString;

    public HttpQueryParams(Map<?, ?> params) {
        this.params = new LinkedHashMap<>();
        params.forEach((k, v) -> this.params.put(StringUtils.toStringOrNull(k), v));

        this.asString = this.params.entrySet().stream()
                .map(e -> encode(e.getKey()) + "=" + encode(e.getValue().toString()))
                .collect(Collectors.joining("&"));
    }

    public String attachToUrl(String url) {
        return params.isEmpty() ?
                url:
                url + "?" + toString();
    }

    @Override
    public String toString() {
        return asString;
    }

    private static String encode(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpQueryParams that = (HttpQueryParams) o;
        return Objects.equals(params, that.params) &&
                Objects.equals(asString, that.asString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(params, asString);
    }
}
