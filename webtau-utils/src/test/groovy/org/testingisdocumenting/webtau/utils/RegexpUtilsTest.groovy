/*
 * Copyright 2021 webtau maintainers
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

import org.junit.Assert
import org.junit.Test

class RegexpUtilsTest {
    @Test
    void "replace matches with a callback result"() {
        def replaced = RegexpUtils.replaceAll("hello 10 world of 42 numbers", ~/(\d)\d*/, { m -> '"' + m.group(1) + '"' })
        Assert.assertEquals('hello "1" world of "4" numbers', replaced)
    }

    @Test
    void "extract value by regexp"() {
        Assert.assertEquals("123",
                RegexpUtils.extractByRegexp("line 1\nline 2\nhello id=123 ere\n", "id=(\\d+)"))

        Assert.assertEquals("123",
                RegexpUtils.extractByRegexp("line 1\nline 2\nhello id=123 ere\n", ~/id=(\d+)/))

        Assert.assertEquals(null,
                RegexpUtils.extractByRegexp("line 1\nline 2\nhello id=123 ere\n", "bid=(\\d+)"))
    }
}
