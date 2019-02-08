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

package com.twosigma.webtau.utils

import org.junit.Test

class NumberUtilsTest {
    @Test
    void "should convert double as string to double"() {
        def number = NumberUtils.convertStringToNumber("12.45")
        assert number.class == Double
        assert number == 12.45
    }

    @Test
    void "should convert number without decimals as string to long"() {
        def number = NumberUtils.convertStringToNumber("12")
        assert number.class == Long
        assert number == 12
    }
}
