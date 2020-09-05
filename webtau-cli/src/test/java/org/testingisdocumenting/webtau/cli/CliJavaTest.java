/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.cli;

import org.testingisdocumenting.webtau.documentation.DocumentationArtifactsLocation;
import org.testingisdocumenting.webtau.utils.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.cli.Cli.cli;
import static org.testingisdocumenting.webtau.cli.CliTestUtils.supportedPlatformOnly;

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
    public void runResultWithoutValidation() {
        supportedPlatformOnly(() -> {
            CliRunResult result = cli.run("ls -l");

            actual(result.getExitCode()).should(equal(0));
            actual(result.getError()).should(equal(""));
            actual(result.getOutput()).should(contain("pom.xml"));
        });
    }

    @Test
    public void runResultWhenFailToRun() {
        supportedPlatformOnly(() -> {
            CliRunResult result = cli.run("a_ls -l", ((exitCode, output, error) -> exitCode.should(equal(127))));

            actual(result.getExitCode()).should(equal(127));
            actual(result.getError()).should(contain("not found"));
            actual(result.getOutput()).should(equal(""));
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
    public void outputAndExitCodeValidationAndResult() {
        supportedPlatformOnly(() -> {
            CliRunResult result = cli.run("scripts/hello \"message to world\"", (exitCode, output, error) -> {
                exitCode.should(equal(5));

                output.should(equal(Pattern.compile("hello")));
                output.should(contain("world"));
                output.should(contain("message to world"));

                error.should(contain("error line two"));
            });

            actual(result.getExitCode()).should(equal(5));
            actual(result.getOutput()).should(contain("world"));
            actual(result.getError()).should(contain("error line two"));
        });
    }

    @Test
    public void envVars() {
        supportedPlatformOnly(() -> {
            cli.run("scripts/hello", cli.env("NAME", "Java"), (exitCode, output, error) -> {
                exitCode.should(equal(5));

                output.should(contain("hello world Java"));
                error.should(contain("error line two"));
            });
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
}
