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

package com.twosigma.webtau.reporter;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class WebTauTestMetadata {
    private final Map<String, Object> metadata;

    public WebTauTestMetadata() {
        metadata = new LinkedHashMap<>();
    }

    /**
     * register key/value metadata
     * @param values metadata values to add
     * @return previously registered values
     */
    public Map<String, Object> add(Map<String, Object> values) {
        Map<String, Object> previousMetadataValues = new HashMap<>();
        values.forEach((k, v) -> {
            Object previous = metadata.put(k, v);
            if (previous != null) {
                previousMetadataValues.put(k, previous);
            }
        });

        return previousMetadataValues;
    }

    public boolean has(String key) {
        return metadata.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public <E> E get(String key) {
        return (E) metadata.get(key);
    }

    @SuppressWarnings("unchecked")
    public <E> E get(String key, E defaultValue) {
        return (E) metadata.getOrDefault(key, defaultValue);
    }

    public void add(WebTauTestMetadata meta) {
        this.metadata.putAll(meta.metadata);
    }

    public boolean isEmpty() {
        return metadata.isEmpty();
    }

    public Map<String, Object> toMap() {
        return Collections.unmodifiableMap(metadata);
    }
}
