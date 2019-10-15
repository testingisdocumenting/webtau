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

package com.twosigma.webtau.cli;

import com.twosigma.webtau.documentation.DocumentationArtifactsLocation;
import com.twosigma.webtau.utils.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static com.twosigma.webtau.Ddjt.*;
import static com.twosigma.webtau.cli.Cli.cli;
import static com.twosigma.webtau.cli.CliTestUtils.supportedPlatformOnly;

public class CliJavaTest {
    private static Path existingDocRoot;

    @BeforeClass
    public static void init() {
        existingDocRoot = DocumentationArtifactsLocation.getRoot();
        DocumentationArtifactsLocation.setRoot(
                DocumentationArtifactsLocation.classBasedLocation(CliJavaTest.class).resolve("doc-artifacts"));
    }

    @AfterClass
    public static void clean() {
        DocumentationArtifactsLocation.setRoot(existingDocRoot);
    }

    @Test
    public void outputOnlyValidation() {
        supportedPlatformOnly(() -> {
            cli.run("ls -l", (output, error) -> {

            });
        });
    }

    @Test
    public void outputAndExitCodeValidation() {
        supportedPlatformOnly(() -> {
            cli.run("scripts/hello \"message to world\"", (exitCode, output, error) -> {
                exitCode.should(equal(5));

                output.should(equal(Pattern.compile("hello")));
                output.should(contain("world"));
                output.should(contain("message to world"));

                error.should(contain("error line two"));
            });
        });
    }

    @Test
    public void envVars() {
        supportedPlatformOnly(() -> {
            cli.run("scripts/hello", cli.env("NAME", "Java"), (exitCode, output, error) -> {
                output.should(contain("hello world Java"));
                error.should(contain("error line two"));
            });
        });
    }

    @Test
    public void docCapture() {
        supportedPlatformOnly(() -> {
            cli.run("scripts/hello", ((output, error) -> {
                output.should(contain("line in the middle"));
                output.should(contain(Pattern.compile("line in the")));
                output.should(contain("more text"));

                error.should(contain("error line one"));
            }));

            String artifactName = "hello-script";
            cli.doc.capture(artifactName);

            validateCapturedDocs(artifactName, "out.txt", "hello world\n" +
                    "line in the middle\n" +
                    "more text");

            validateCapturedDocs(artifactName, "err.txt", "error line one\n" +
                    "error line two");

            validateCapturedDocs(artifactName, "out.matched.txt", "line in the middle\nmore text");
            validateCapturedDocs(artifactName, "err.matched.txt", "error line one");
            validateCapturedDocs(artifactName, "exitcode.txt", "5");
        });
    }

    @Test
    public void linesWithNotContain() {
        supportedPlatformOnly(() -> {
            code(() -> {
                cli.run("scripts/hello", ((output, error) -> {
                    output.shouldNot(contain("line"));
                }));
            }).should(throwException(Pattern.compile("output\\[1]: equals \"line in the middle\"")));
        });
    }

    private static void validateCapturedDocs(String artifactName, String fileName, String expectedContent) {
        Path path = DocumentationArtifactsLocation.resolve(artifactName).resolve(fileName);
        actual(Files.exists(path)).should(equal(true));

        actual(FileUtils.fileTextContent(path)).should(equal(expectedContent));
    }
}
