/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.data;

import org.testingisdocumenting.webtau.MissingEntryPlaceholder;

import java.util.*;

public class MapWithTrackedMissingKeys extends LinkedHashMap<Object, Object> {
    private final Set<Object> missingKeys = new LinkedHashSet<>();

    public MapWithTrackedMissingKeys(Map<?, ?> original) {
        super(convertNestedMaps(original));
    }

    @Override
    public boolean containsKey(Object key) {
        boolean contains = super.containsKey(key);
        if (!contains) {
            missingKeys.add(key);
        }

        return contains;
    }

    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {
        LinkedHashSet<Map.Entry<Object, Object>> result = new LinkedHashSet<>();

        super.forEach((key, value) -> result.add(new SimpleEntry<>(key, wrapValueIfRequired(value))));

        missingKeys.forEach(key -> result.add(new SimpleEntry<>(key, MissingEntryPlaceholder.ENTRY)));

        return result;
    }

    @Override
    public int size() {
        return super.size() + missingKeys.size();
    }

    private static Map<?, ?> convertNestedMaps(Map<?, ?> original) {
        Map<Object, Object> result = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : original.entrySet()) {
            Object value = entry.getValue();
            result.put(entry.getKey(), wrapValueIfRequired(value));
        }

        return result;
    }

    private static Object wrapValueIfRequired(Object v) {
        return v instanceof Map ?
                new MapWithTrackedMissingKeys((Map<?, ?>) v) : v;
    }
}
