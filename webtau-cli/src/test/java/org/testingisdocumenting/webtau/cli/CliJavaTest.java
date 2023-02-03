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

import org.testingisdocumenting.webtau.cfg.ConfigValue;
import org.testingisdocumenting.webtau.documentation.DocumentationArtifactsLocation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Pattern;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.cli.Cli.cli;
import static org.testingisdocumenting.webtau.cli.CliTestUtils.supportedPlatformOnly;
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*;

public class CliJavaTest {
    private static Path existingDocRoot;

    @BeforeClass
    public static void init() {
        existingDocRoot = DocumentationArtifactsLocation.getRoot();
        DocumentationArtifactsLocation.setRoot(
                Paths.get("cli-doc-artifacts"));
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
            CliRunResult result = cli.run("scripts/hello", ((exitCode, output, error) -> exitCode.should(equal(5))));

            actual(result.getExitCode()).should(equal(5));
            actual(result.getError()).should(contain("error line two"));
            actual(result.getOutput()).should(contain("more text"));
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
            runExpectExceptionAndValidateOutput(AssertionError.class, contain("process output[1]: equals \"line in the middle\""),
                    () -> cli.run("scripts/hello", ((output, error) -> {
                        output.shouldNot(contain("line"));
                    })));
        });
    }

    @Test
    public void pathBasedLocation() {
        ConfigValue pathConfigValue = CliConfig.getCliPathConfigValue();
        pathConfigValue.set("test", Arrays.asList("my-path-one", "additional-scripts"));

        supportedPlatformOnly(() -> {
            cli.run("nested-dir/world", ((output, error) -> {
                output.should(contain("hello world path detection"));
            }));
        });
    }

    @Test
    public void timeOut() {
        supportedPlatformOnly(() -> {
            ConfigValue timeoutConfigValue = CliConfig.getCliTimeoutConfigValue();
            try {
                timeoutConfigValue.set("manual", 20);
                code(() -> cli.run("sleep 2")).should(throwException("process timed-out"));
            } finally {
                timeoutConfigValue.reset();
            }
        });
    }
}
