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

import org.junit.Test;

import java.util.regex.Pattern;

import static com.twosigma.webtau.Ddjt.contain;
import static com.twosigma.webtau.Ddjt.equal;
import static com.twosigma.webtau.cli.Cli.cli;
import static com.twosigma.webtau.cli.CliTestUtils.nixOnly;

public class CliJavaTest {
    @Test
    public void outputOnlyValidation() {
        cli.run("ls -l", (output, error) -> {

        });
    }

    @Test
    public void outputAndExitCodeValidation() {
        nixOnly(() -> {
            cli.run("scripts/hello", (exitCode, output, error) -> {
                exitCode.should(equal(0));

                output.should(equal(Pattern.compile("hello")));
                output.should(contain("world"));
            });
        });
    }

    @Test
    public void envVars() {
        nixOnly(() -> {
            cli.run("scripts/hello", cli.env("NAME", "Java"), (exitCode, output, error) -> {
                output.should(contain("hello world Java"));
            });
        });
    }
}
