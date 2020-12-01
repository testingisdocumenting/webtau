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

package org.testingisdocumenting.webtau.http;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class HttpHeader {
    private static final Set<String> KEYS_TO_REDACT = new HashSet<>(Arrays.asList("authorization", "cookie", "set-cookie"));

    public static final HttpHeader EMPTY = new HttpHeader(Collections.emptyMap());

    private final Map<String, CharSequence> header;

    public HttpHeader() {
        this.header = new LinkedHashMap<>();
    }

    public HttpHeader(Map<String, CharSequence> header) {
        this.header = header;
    }

    public void forEachProperty(BiConsumer<String, String> consumer) {
        header.forEach((k, v) -> consumer.accept(k, v.toString()));
    }

    public <T> Stream<T> mapProperties(BiFunction<String, CharSequence, T> mapper) {
        return header.entrySet().stream().map(e -> mapper.apply(e.getKey(), e.getValue()));
    }

    public HttpHeader merge(Map<String, CharSequence> properties) {
        Map<String, CharSequence> copy = new LinkedHashMap<>(this.header);
        copy.putAll(properties);

        return new HttpHeader(copy);
    }

    public HttpHeader merge(HttpHeader otherHeaders) {
        return merge(otherHeaders.header);
    }

    public boolean containsKey(String key) {
        return header.containsKey(key);
    }

    public String get(String key) {
        return header.get(key).toString();
    }

    public String caseInsensitiveGet(String key) {
        return header.entrySet().stream()
                .filter(entry -> key.equalsIgnoreCase(entry.getKey()))
                .findFirst()
                .map(e -> e.getValue().toString())
                .orElse(null);
    }

    /**
     * Adds an addition header to this HttpHeader object.  This function
     * may throw UnsupportedOperationException depending on how HttpHeader
     * was constructed.
     *
     * For that reason, this method is deprecated and you should use either
     * <code>with(String key, String value)</code> or one of the <code>merge</code>
     * methods which are non-mutating.
     *
     * @deprecated use <code>with(String key, String value)</code>
     *             or <code>merge(HttpHeader otherHeader)</code>
     *             or <code>merge(Map&lt;String, String&gt; properties)</code>
     */
    @Deprecated
    public void add(String key, String value) {
        header.put(key, value);
    }

    /**
     * Creates a new header from the current one with an additional key-value
     * @param key additional key
     * @param value additional value
     * @return new header
     */
    public HttpHeader with(String key, CharSequence value) {
        Map<String, CharSequence> copy = new LinkedHashMap<>(this.header);
        copy.put(key, value);

        return new HttpHeader(copy);
    }

    /**
     * Creates a new header from the current one with an additional key values
     * @param additionalValues additional values
     * @return new header
     */
    public HttpHeader with(Map<String, CharSequence> additionalValues) {
        Map<String, CharSequence> copy = new LinkedHashMap<>(this.header);
        copy.putAll(additionalValues);

        return new HttpHeader(copy);
    }

    public HttpHeader redactSecrets() {
        Map<String, CharSequence> redacted = new LinkedHashMap<>();
        for (Map.Entry<String, CharSequence> entry : header.entrySet()) {
            redacted.put(entry.getKey(), redactValueIfRequired(entry.getKey(), entry.getValue()));
        }

        return new HttpHeader(redacted);
    }

    public List<Map<String, String>> toListOfMaps() {
        return mapProperties((k, v) -> {
            Map<String, String> entry = new LinkedHashMap<>();
            entry.put("key", k);
            entry.put("value", v.toString());

            return entry;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HttpHeader that = (HttpHeader) o;
        return Objects.equals(header, that.header);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header);
    }

    @Override
    public String toString() {
        return header.entrySet()
                .stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(joining("\n"));
    }

    private CharSequence redactValueIfRequired(String key, CharSequence value) {
        if (key == null) {
            return value;
        }

        return KEYS_TO_REDACT.contains(key.toLowerCase()) ?
                "................" :
                value;
    }
}
