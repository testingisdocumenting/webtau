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

package com.twosigma.webtau.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class CollectionUtils {
    private CollectionUtils() {
    }

    /**
     * creates map from var args key value
     * @param kvs key value pairs
     * @param <K> type of key
     * @param <V> type of value
     * @return map with preserved order
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> aMapOf(Object... kvs) {
        Map<K, V> result = new LinkedHashMap<>();

        if (kvs.length % 2 != 0) {
            throw new IllegalArgumentException("key value sequence must have even number of values");
        }

        for (int idx = 0; idx < kvs.length; idx += 2) {
            result.put((K) kvs[idx], (V) kvs[idx + 1]);
        }

        return result;
    }
}
