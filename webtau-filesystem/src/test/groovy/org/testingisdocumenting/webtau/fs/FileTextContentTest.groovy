/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.fs

import org.junit.Test

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class FileTextContentTest {
    @Test
    void "extract file content by regexp"() {
        def textContent = new FileTextContent(Paths.get("src/test/resources/file-for-text-extraction.txt"))
        def id = textContent.extractByRegexp("id=([^&]+)")
        id.should == '23544643'
    }

    @Test
    void "extract file content by regexp throws error when no match"() {
        def textContent = new FileTextContent(Paths.get("src/test/resources/file-for-text-extraction.txt"))
        code {
            textContent.extractByRegexp("ID=([^&]+)")
        } should throwException(~/can't find content to extract using regexp </)
    }
}
