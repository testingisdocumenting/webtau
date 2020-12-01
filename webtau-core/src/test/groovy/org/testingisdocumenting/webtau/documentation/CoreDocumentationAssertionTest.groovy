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

package org.testingisdocumenting.webtau.documentation

import org.testingisdocumenting.webtau.data.components.Account
import org.testingisdocumenting.webtau.utils.FileUtils
import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.*

class CoreDocumentationAssertionTest {
    @Test
    void "should provide a way to capture last assertion expected value"() {
        def score = 100

        actual(score).shouldBe(lessThan(150))

        doc.expected.capture(CoreDocumentationAssertionTest, "score")
        validateArtifactContent("score", "150")
    }

    @Test
    void "should provide a way to capture last assertion actual value"() {
        def level = 10

        actual(level).shouldBe(greaterThan(3))

        doc.actual.capture(CoreDocumentationAssertionTest, "level")
        validateArtifactContent("level", "10")
    }

    @Test
    void "should convert TableData to json"() {
        def accounts = [
                new Account("a1", "w1", "a1"),
                new Account("a2", "w2", "a2")]


        actual(accounts).should(equal(table("id" , "walletId",
                                            ________________,
                                            "a1" , "w1",
                                            "a2" , "w2")))
        doc.expected.capture(CoreDocumentationAssertionTest, "accounts")
        validateArtifactContent("accounts", '[ {\n' +
                '  "id" : "a1",\n' +
                '  "walletId" : "w1"\n' +
                '}, {\n' +
                '  "id" : "a2",\n' +
                '  "walletId" : "w2"\n' +
                '} ]')
    }

    @Test
    void "should capture actual exception"() {
        code {
            throw new RuntimeException("actual full\nexception")
        } should throwException(~/actual/)

        doc.actual.capture(CoreDocumentationAssertionTest, "actual-exception")
        validateArtifactContent("actual-exception", "txt",
                "actual full\nexception")
    }

    @Test
    void "should capture expected exception"() {
        code {
            throw new RuntimeException("actual full\nexception")
        } should throwException(RuntimeException)

        doc.expected.capture(CoreDocumentationAssertionTest, "expected-exception")
        validateArtifactContent("expected-exception", "json",
                '"java.lang.RuntimeException"')
    }

    private static void validateArtifactContent(String artifactName, String expectedFileContent) {
        validateArtifactContent(artifactName, "json", expectedFileContent)
    }

    private static void validateArtifactContent(String artifactName, String extension, String expectedFileContent) {
        def path = DocumentationArtifactsLocation.classBasedLocation(CoreDocumentationAssertionTest)
                .resolve(artifactName + "." + extension)

        actual(FileUtils.fileTextContent(path)).should(equal(expectedFileContent))
    }
}
