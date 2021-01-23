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

package org.testingisdocumenting.webtau.documentation

import org.junit.Test
import org.testingisdocumenting.webtau.utils.FileUtils

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.WebTauCore.*

class CoreDocumentationTest {
    @Test
    void "captures Strings artifact into text file"() {
        doc.capture("text-artifact-id", "hello world")
        actual(FileUtils.fileTextContent(Paths.get("doc-artifacts/text-artifact-id.txt"))).should(
                equal("hello world"))
    }

    @Test
    void "should check artifact uniqueness"() {
        doc.capture("my-unique-id", "hello")

        code {
            doc.capture("my-unique-id", "hello")
        } should throwException("doc artifact name <my-unique-id.txt> was already used")
    }
}
