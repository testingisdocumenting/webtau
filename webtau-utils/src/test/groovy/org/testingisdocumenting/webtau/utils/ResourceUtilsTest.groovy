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

package org.testingisdocumenting.webtau.utils

import org.junit.Test

class ResourceUtilsTest {
    @Test
    void "should read text from a single resource file"() {
        def content = ResourceUtils.textContent("single.txt")
        assert content == String.format("single resource%nfile")
    }

    @Test
    void "should read binary from a single resource file"() {
        def content = ResourceUtils.binaryContent("castle.jpg")
        assert content.length == 145460
        assert content[0..2] == [-1, -40, -1]
    }

    @Test(expected = RuntimeException)
    void "should validate single resource presence"() {
        ResourceUtils.textContent("not-found.txt")
    }

    @Test
    void "should read texts from multiple resource files with the same name"() {
        def contents = ResourceUtils.textContents("important/meta.txt")

        assert contents.size() == 2
        contents.any { it == "second hello meta\nsecond txt" }
        contents.any { it == "hello meta\ntxt" }
    }

    @Test(expected = RuntimeException)
    void "should validate multiple resource presence"() {
        ResourceUtils.textContents("not-found.txt")
    }
}
