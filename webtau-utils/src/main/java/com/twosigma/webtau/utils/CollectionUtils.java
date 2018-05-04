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
    public static <K, V> Map<K, V> createMap(Object... kvs) {
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
