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

import java.util.LinkedHashMap;
import java.util.Map;

public class WebTauTestMeta {
    private final Map<String, Object> meta;

    public WebTauTestMeta() {
        meta = new LinkedHashMap<>();
    }

    /**
     * register key/value meta
     * @param key key
     * @param value value
     * @return previously registered value for key
     */
    public Object add(String key, Object value) {
        return meta.put(key, value);
    }

    public void add(WebTauTestMeta meta) {
        this.meta.putAll(meta.meta);
    }

    public boolean isEmpty() {
        return meta.isEmpty();
    }

    public Map<String, Object> toMap() {
        return meta;
    }
}
