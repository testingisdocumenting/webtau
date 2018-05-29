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

package com.twosigma.webtau.http;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static java.util.stream.Collectors.joining;

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
        return properties.entrySet()
                .stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(joining("\n"));
    }
}
