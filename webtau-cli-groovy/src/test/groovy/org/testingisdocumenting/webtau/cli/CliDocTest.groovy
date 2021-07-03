/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.cli

import org.junit.Test
import org.testingisdocumenting.webtau.documentation.DocumentationArtifactsLocation
import org.testingisdocumenting.webtau.utils.FileUtils

import java.nio.file.Files
import java.nio.file.Path
import java.util.regex.Pattern

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.cli.Cli.cli
import static org.testingisdocumenting.webtau.cli.CliTestUtils.supportedPlatformOnly

class CliDocTest {
    @Test
    void "doc capture"() {
        supportedPlatformOnly {
            cli.run("scripts/hello") {
                exitCode.should(equal(5))

                output.should(contain("line in the middle"))
                output.should(contain(Pattern.compile("line in the")))
                output.should(contain("more text"))

                error.should(contain("error line one"))
            }

            def artifactName = "hello-script"
            cli.doc.capture(artifactName)

            validateCapturedDocs(artifactName, "out.txt", "hello world\n" +
                    "line in the middle\n" +
                    "more text")

            validateCapturedDocs(artifactName, "err.txt", "error line one\n" +
                    "error line two")

            validateCapturedDocs(artifactName, "out.matched.txt", "line in the middle\nmore text")
            validateCapturedDocs(artifactName, "err.matched.txt", "error line one")
            validateCapturedDocs(artifactName, "exitcode.txt", "5")
        }
    }

    @Test
    void "doc capture of background process"() {
        supportedPlatformOnly {
            CliCommand command = cli.command("scripts/hello")
            CliBackgroundCommand running = command.runInBackground("test-param")

            running.getOutput().waitTo contain("more text")
            running.getError().waitTo contain("error line two")

            def artifactName = "background-hello-script"
            cli.doc.capture(artifactName)

            validateCapturedDocs(artifactName, "out.txt", "hello world test-param\n" +
                    "line in the middle\n" +
                    "more text")

            validateCapturedDocs(artifactName, "err.txt", "error line one\n" +
                    "error line two")

            validateCapturedDocs(artifactName, "out.matched.txt", "more text")
            validateCapturedDocs(artifactName, "err.matched.txt", "error line two")
        }
    }

    @Test
    void "should make sure artifact name is unique"() {
        supportedPlatformOnly {
            cli.run("scripts/hello") {
                exitCode.should == 5
            }

            def artifactName = "captured-non-unique-artifact-name"
            cli.doc.capture(artifactName)

            code {
                cli.doc.capture(artifactName)
            } should throwException("doc artifact name <${artifactName}> was already used")
        }
    }

    private static void validateCapturedDocs(String artifactName, String fileName, String expectedContent) {
        Path path = DocumentationArtifactsLocation.resolve(artifactName).resolve(fileName)
        Files.exists(path).should == true

        FileUtils.fileTextContent(path).should == expectedContent
    }
}
