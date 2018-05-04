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

package com.twosigma.webtau.data.converters

import org.junit.Test

class ToMapConvertersTest {
    @Test
    void "defaults to javabean to map converter"() {
        assert ToMapConverters.convert(100) == [DummyNumberToMapConverter: 100]

        def asMap = ToMapConverters.convert(new SimpleBean())
        assert asMap.containsKey("a")
        assert asMap.containsKey("b")
    }

    static class SimpleBean {
        Integer a
        String b
    }
}
