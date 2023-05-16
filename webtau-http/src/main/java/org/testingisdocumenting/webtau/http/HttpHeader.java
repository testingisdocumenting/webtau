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

import org.testingisdocumenting.webtau.utils.CollectionUtils;

import static java.util.stream.Collectors.joining;
import static org.testingisdocumenting.webtau.utils.StringUtils.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpHeader {
    private static final Set<String> KEYS_TO_REDACT = new HashSet<>(Arrays.asList("authorization", "cookie", "set-cookie"));
    private static final Set<String> RESTRICTED_REQUEST_PROPERTIES = new HashSet<>(Arrays.asList("host", "connection", "content-length", "upgrade"));

    public static final HttpHeader EMPTY = new HttpHeader(Collections.emptyMap());

    private final Map<String, String> header;

    public HttpHeader() {
        this.header = new LinkedHashMap<>();
    }

    HttpHeader(Map<String, String> header) {
        this.header = header;
    }

    public void forEachProperty(BiConsumer<String, String> consumer) {
        header.forEach((k, v) -> consumer.accept(toStringOrNull(k), toStringOrNull(v)));
    }

    public void forEachNonRestrictedRequestProperty(BiConsumer<String, String> consumer) {
        header.entrySet().stream()
                .filter(e -> (!RESTRICTED_REQUEST_PROPERTIES.contains(e.getKey().toLowerCase())))
                .forEach(e -> consumer.accept(toStringOrNull(e.getKey()), toStringOrNull(e.getValue())));
    }

    public <T> Stream<T> mapProperties(BiFunction<String, String, T> mapper) {
        return header.entrySet().stream().map(e -> mapper.apply(toStringOrNull(e.getKey()),
                toStringOrNull(e.getValue())));
    }

    /**
     * This method is now deprecated use {@link HttpHeader#with(Map)} instead
     * @deprecated
     * @param properties properties to merge with
     * @return new instance of header with merged properties
     */
    public HttpHeader merge(Map<CharSequence, CharSequence> properties) {
        Map<String, String> copy = new LinkedHashMap<>(this.header);
        properties.forEach((k, v) -> copy.put(toStringOrNull(k), toStringOrNull(v)));

        return new HttpHeader(copy);
    }

    public boolean containsKey(String key) {
        return header.containsKey(key);
    }

    public String get(String key) {
        return toStringOrNull(header.get(key));
    }

    public String caseInsensitiveGet(String key) {
        return header.entrySet().stream()
                .filter(entry -> key.equalsIgnoreCase(entry.getKey()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    /**
     * Adds an addition header to this HttpHeader object.  This function
     * may throw UnsupportedOperationException depending on how HttpHeader
     * was constructed.
     * <p>
     * For that reason, this method is deprecated and you should use either
     * <code>with("MY_HEADER", "my_value")</code> or one of the <code>merge</code>
     * methods which are non-mutating.
     *
     * @deprecated use <code>with</code>
     *             or <code>merge</code>
     */
    @Deprecated
    public void add(String key, String value) {
        header.put(key, value);
    }

    /**
     * Creates a new header from the current one with an additional key-value
     * @param firstKey first key
     * @param firstValue first value
     * @param restKv vararg key value sequence, e.g. "HEADER_ONE", "value_one", "HEADER_TWO", "value_two"
     * @return new header
     */
    public HttpHeader with(CharSequence firstKey, CharSequence firstValue, CharSequence... restKv) {
        Map<Object, Object> mapFromVararg = CollectionUtils.map(firstKey, firstValue, (Object[]) restKv);

        Map<String, String> copy = new LinkedHashMap<>(this.header);
        mapFromVararg.forEach((k, v) -> copy.put(toStringOrNull(k), toStringOrNull(v)));

        return new HttpHeader(copy);
    }

    /**
     * Creates a new header from the current one with an additional key values
     * @param additionalValues additional values
     * @return new header with combined values
     */
    public HttpHeader with(Map<CharSequence, CharSequence> additionalValues) {
        Map<String, String> copy = new LinkedHashMap<>(this.header);
        additionalValues.forEach((k, v) -> copy.put(toStringOrNull(k), toStringOrNull(v)));

        return new HttpHeader(copy);
    }

    /**
     * Creates a new header from the current one with an additional key values copied from a given header
     * @deprecated use {@link HttpHeader#with(HttpHeader)}
     * @param otherHeader other header to take values from
     * @return new header with combined values
     */
    public HttpHeader merge(HttpHeader otherHeader) {
        return with(otherHeader);
    }

    /**
     * Creates a new header from the current one with an additional key values copied from a given header
     * @param otherHeader other header to take values from
     * @return new header with combined values
     */
    public HttpHeader with(HttpHeader otherHeader) {
        Map<String, String> copy = new LinkedHashMap<>(this.header);
        copy.putAll(otherHeader.header);

        return new HttpHeader(copy);
    }

    public HttpHeader redactSecrets() {
        Map<String, String> redacted = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : header.entrySet()) {
            redacted.put(entry.getKey(), redactValueIfRequired(entry.getKey(), entry.getValue()));
        }

        return new HttpHeader(redacted);
    }

    public List<Map<String, String>> toListOfMaps() {
        return mapProperties((k, v) -> {
            Map<String, String> entry = new LinkedHashMap<>();
            entry.put("key", k);
            entry.put("value", v);

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

    private String redactValueIfRequired(String key, String value) {
        if (key == null) {
            return value;
        }

        return KEYS_TO_REDACT.contains(key.toLowerCase()) ?
                "................" :
                value;
    }
}
