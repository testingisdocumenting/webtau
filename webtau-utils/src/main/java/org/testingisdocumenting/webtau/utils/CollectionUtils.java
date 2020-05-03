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
import java.util.stream.IntStream;

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
