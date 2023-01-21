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

package org.testingisdocumenting.webtau.utils

import org.junit.Test

class CollectionUtilsTest {
    @Test
    void "should create map from the vararg sequence"() {
        def map = CollectionUtils.mapOf("key1", 10, "key2", 20, "key3", 30)
        assert map == [key1: 10, key2: 20, key3: 30]
    }

    @Test(expected = IllegalArgumentException)
    void "should validate completeness of the map when create from varargs"() {
        CollectionUtils.mapOf("key1", 10, "key2")
    }

    @Test
    void "should create a new map from given map and value overrides"() {
        def original = CollectionUtils.mapOf("key1", 10, "key2", 20, "key3", 30, "key4", 40)
        def withOverrides = CollectionUtils.mapOf(original, "key2", 22, "key3", 33)

        assert original == [key1: 10, key2: 20, key3: 30, key4: 40]
        assert withOverrides == [key1: 10, key2: 22, key3: 33, key4: 40]
    }

    @Test
    void "should convert a map to string string map"() {
        def original = CollectionUtils.mapOf("key1", 10, "key2", 20, "key3", 30, "key4", 40)
        def converted = CollectionUtils.toStringStringMap(original)

        assert converted == [key1: "10", key2: "20", key3: "30", key4: "40"]
    }

    @Test
    void "should convert array of boolean to list of Boolean"() {
        boolean[] array = [true, false]
        List<Boolean> converted = CollectionUtils.convertArrayToList(array)

        assert converted[0].class == Boolean
        assert converted[0]
        assert !converted[1]
    }

    @Test
    void "should convert array of byte to list of Byte"() {
        byte[] array = [1, 2]
        List<Byte> converted = CollectionUtils.convertArrayToList(array)

        assert converted[0].class == Byte
        assert converted[0] == 1
        assert converted[1] == 2
    }

    @Test
    void "should convert array of char to list of Character"() {
        char[] array = ['a' as char, 'b' as char]
        List<Character> converted = CollectionUtils.convertArrayToList(array)

        assert converted[0].class == Character
        assert converted[0] == 'a'
        assert converted[1] == 'b'
    }

    @Test
    void "should convert array of short to list of Short"() {
        short[] array = [1, 2]
        List<Short> converted = CollectionUtils.convertArrayToList(array)

        assert converted[0].class == Short
        assert converted[0] == 1
        assert converted[1] == 2
    }

    @Test
    void "should convert array of int to list of Integer"() {
        int[] array = [1, 2]
        List<Integer> converted = CollectionUtils.convertArrayToList(array)

        assert converted[0].class == Integer
        assert converted[0] == 1
        assert converted[1] == 2
    }

    @Test
    void "should convert array of long to list of Long"() {
        long[] array = [1, 2]
        List<Long> converted = CollectionUtils.convertArrayToList(array)

        assert converted[0].class == Long
        assert converted[0] == 1
        assert converted[1] == 2
    }

    @Test
    void "should convert array of float to list of Float"() {
        float[] array = [1, 2]
        List<Float> converted = CollectionUtils.convertArrayToList(array)

        assert converted[0].class == Float
        assert converted[0] == 1
        assert converted[1] == 2
    }

    @Test
    void "should convert array of double to list of Double"() {
        double[] array = [1, 2]
        List<Double> converted = CollectionUtils.convertArrayToList(array)

        assert converted[0].class == Double
        assert converted[0] == 1
        assert converted[1] == 2
    }

    @Test
    void "should convert array of String to list of String"() {
        String[] array = ['a', 'b']
        List<String> converted = CollectionUtils.convertArrayToList(array)

        assert converted[0].class == String
        assert converted[0] == 'a'
        assert converted[1] == 'b'
    }
}
