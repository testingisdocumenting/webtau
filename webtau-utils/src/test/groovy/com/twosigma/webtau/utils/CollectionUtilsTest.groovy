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

package com.twosigma.webtau.utils

import org.junit.Test

class CollectionUtilsTest {
    @Test
    void "should create map from the vararg sequence"() {
        def map = CollectionUtils.aMapOf("key1", 10, "key2", 20, "key3", 30)
        assert map == [key1: 10, key2: 20, key3: 30]
    }

    @Test(expected = IllegalArgumentException)
    void "should validate completeness of the map when create from varargs"() {
        CollectionUtils.aMapOf("key1", 10, "key2")
    }
}
