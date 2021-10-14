/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.cache;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

class CacheValueConverter {
    @SuppressWarnings("unchecked")
    static Object convertToCached(Object v) {
        if (v == null) {
            throw new IllegalArgumentException("can't cache null value");
        }

        if (v instanceof CharSequence) {
            return v.toString();
        }

        if (v instanceof Path) {
            return ((Path) v).toAbsolutePath().toString();
        }

        if (v instanceof Iterable) {
            return convertIterableToCached((Iterable<Object>) v);
        }

        if (v instanceof Map) {
            return convertMapToCached((Map<Object, Object>) v);
        }

        if (v instanceof Number) {
            return v;
        }

        throw new IllegalArgumentException("unsupported cache type <" + v.getClass() + ">: " + v);
    }

    private static Map<String, Object> convertMapToCached(Map<Object, Object> v) {
        List<Object> nonStringKeys = v.keySet().stream().filter(key -> !(key instanceof String)).collect(Collectors.toList());
        if (!nonStringKeys.isEmpty()) {
            throw new IllegalArgumentException("can only cache maps with string keys\n" +
                    "  given map: " + v + "\n" +
                    "  non string keys: " + nonStringKeys.stream().map(Objects::toString).collect(Collectors.joining("; ")));
        }

        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<Object, Object> entry : v.entrySet()) {
            result.put(entry.getKey().toString(), convertToCached(entry.getValue()));
        }

        return result;
    }

    private static List<Object> convertIterableToCached(Iterable<Object> iterable) {
        List<Object> result = new ArrayList<>();
        for (Object v : iterable) {
            result.add(convertToCached(v));
        }

        return result;
    }
}
