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

import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.IgnoreAnsiString

import static org.testingisdocumenting.webtau.cli.Cli.cli
import static org.testingisdocumenting.webtau.cli.CliTestUtils.supportedPlatformOnly

class CliBackgroundCommandOutputTest implements ConsoleOutput {
    StringBuffer output

    @Before
    void init() {
        ConsoleOutputs.add(this)
        output = new StringBuffer()
    }

    @Before
    void cleanup() {
        ConsoleOutputs.remove(this)
    }

    @Test
    void "should not be killed twice at the end of test"() {
        supportedPlatformOnly {
            def sleep = cli.runInBackground("scripts/long-sleep")
            sleep.stop()

            CliBackgroundCommandManager.destroyActiveProcesses()

            normalizeOutput(output.toString()).should == '> running cli command in background scripts/long-sleep\n' +
                    '. ran cli command in background scripts/long-sleep (time)\n' +
                    '> stopping cli command in background scripts/long-sleep\n' +
                    '. stopped cli command in background scripts/long-sleep (time)\n'
        }
    }

    @Test
    void "should be killed at the end of test"() {
        supportedPlatformOnly {
            cli.runInBackground("scripts/long-sleep")
            CliBackgroundCommandManager.destroyActiveProcesses()

            normalizeOutput(output.toString()).should == '> running cli command in background scripts/long-sleep\n' +
                    '. ran cli command in background scripts/long-sleep (time)\n' +
                    '> stopping cli command in background scripts/long-sleep\n' +
                    '. stopped cli command in background scripts/long-sleep (time)\n'
        }
    }

    private static String normalizeOutput(String output) {
        return output.replaceAll(/\(\d+ms\)/, "(time)")
            .replace('. background cli command : scripts/long-sleep finished with exit code 143 (time)\n', '')
    }

    @Override
    void out(Object... styleOrValues) {
        output.append(new IgnoreAnsiString(styleOrValues).toString()).append('\n')
    }

    @Override
    void err(Object... styleOrValues) {
    }
}
