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

package org.testingisdocumenting.webtau.utils;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionUtils {
    private CollectionUtils() {
    }

    /**
     * creates a map from var args key value with enforced first key type
     * @param firstKey first key
     * @param firstValue first value
     * @param rest rest of the values
     * @param <K> type of key
     * @return map with preserved order
     */
    @SuppressWarnings("unchecked")
    public static <K> Map<K, Object> map(K firstKey, Object firstValue, Object... rest) {
        if (rest.length % 2 != 0) {
            throw new IllegalArgumentException("key value sequence must have even number of values");
        }

        Map<K, Object> result = new LinkedHashMap<>();
        result.put(firstKey, firstValue);

        for (int idx = 0; idx < rest.length; idx += 2) {
            result.put((K) rest[idx], rest[idx+1]);
        }

        return result;
    }

    /**
     * creates a map from original map and var args key value overrides
     * @param original original map
     * @param firstKey first key
     * @param firstValue first value
     * @param <K> type of key
     * @return map with preserved order
     */
    public static <K> Map<K, Object> map(Map<K, ?> original, K firstKey, Object firstValue, Object... restKv) {
        Map<K, Object> result = new LinkedHashMap<>(original);
        Map<K, Object> overrides = map(firstKey, firstValue, restKv);

        result.putAll(overrides);
        return result;
    }

    public static Map<String, Object> toStringObjectMap(Map<?, ?> original) {
        Map<String, Object> result = new LinkedHashMap<>();
        original.forEach((k, v) -> result.put(StringUtils.toStringOrNull(k), v));

        return result;
    }

    public static Map<String, String> toStringStringMap(Map<?, ?> original) {
        Map<String, String> result = new LinkedHashMap<>();
        original.forEach((k, v) -> result.put(StringUtils.toStringOrNull(k), StringUtils.toStringOrNull(v)));

        return result;
    }

    /**
     * converts an array of primitive or generic objects into a list
     * @param actual array of primitives or objects
     * @return list of boxed values
     */
    @SuppressWarnings("unchecked")
    public static <E> List<E> convertArrayToList(Object actual) {
        Class<?> componentType = actual.getClass().getComponentType();
        if (componentType.equals(boolean.class)) {
            return (List<E>) toList((boolean[]) actual);
        } else if (componentType.equals(byte.class)) {
            return (List<E>) toList((byte[]) actual);
        } else if (componentType.equals(char.class)) {
            return (List<E>) toList((char[]) actual);
        } else if (componentType.equals(short.class)) {
            return (List<E>) toList((short[]) actual);
        } else if (componentType.equals(int.class)) {
            return (List<E>) Arrays.stream((int[])actual).boxed().collect(Collectors.toList());
        } else if (componentType.equals(long.class)) {
            return (List<E>) Arrays.stream((long[])actual).boxed().collect(Collectors.toList());
        } else if (componentType.equals(float.class)) {
            return (List<E>) toList((float[]) actual);
        } else if (componentType.equals(double.class)) {
            return (List<E>) Arrays.stream((double[])actual).boxed().collect(Collectors.toList());
        } else {
            return (List<E>) Arrays.stream((Object[])actual).collect(Collectors.toList());
        }
    }

    public static <K, V> boolean nullOrEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    public static <K, V> boolean notNullOrEmpty(Map<K, V> map) {
        return !nullOrEmpty(map);
    }

    /**
     * @deprecated use {@link CollectionUtils#map} instead
     */
    @Deprecated
    public static <K> Map<K, Object> aMapOf(K firstKey, Object firstValue, Object... rest) {
        return map(firstKey, firstValue, rest);
    }

    /**
     * @deprecated use {@link CollectionUtils#map} instead
     */
    @Deprecated
    public static <K> Map<K, Object> aMapOf(Map<K, ?> original, K firstKey, Object firstValue, Object... restKv) {
        return map(original, firstKey, firstValue, restKv);
    }

    private static List<Boolean> toList(boolean[] booleans) {
        List<Boolean> list = new ArrayList<>(booleans.length);
        for (boolean bool : booleans) {
            list.add(bool);
        }

        return list;
    }

    private static List<Byte> toList(byte[] bytes) {
        List<Byte> list = new ArrayList<>(bytes.length);
        for (byte aByte : bytes) {
            list.add(aByte);
        }

        return list;
    }

    private static List<Character> toList(char[] chars) {
        List<Character> list = new ArrayList<>(chars.length);
        for (char aChar : chars) {
            list.add(aChar);
        }

        return list;
    }

    private static List<Short> toList(short[] shorts) {
        List<Short> list = new ArrayList<>(shorts.length);
        for (short aShort : shorts) {
            list.add(aShort);
        }

        return list;
    }

    private static List<Float> toList(float[] floats) {
        List<Float> list = new ArrayList<>(floats.length);
        for (float aFloat : floats) {
            list.add(aFloat);
        }

        return list;
    }
}
