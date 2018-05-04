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

class JavaBeanUtilsTest {
    @Test
    void "converts classic get/set bean without fields to a map"() {
        def asMap = JavaBeanUtils.convertBeanToMap(new ClassicBean())
        assert asMap == [description: "d1", price: 100]
    }

    @Test
    void "converts read-only bean with fields to a map"() {
        def asMap = JavaBeanUtils.convertBeanToMap(new BeanWithFields(20, "text"))
        assert asMap == [propA: 20, propB: "text"]
    }

    @Test
    void "converts bean with nulls to a map"() {
        def asMap = JavaBeanUtils.convertBeanToMap(new BeanWithFields(10, null))
        assert asMap == [propA: 10, propB: null]
    }
}
