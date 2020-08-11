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
    String output

    @Before
    void init() {
        ConsoleOutputs.add(this)
        output = ""
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

            output = output.replaceAll(/\(\d+ms\)/, "(time)")
            def optionalLastLine = '. background cli command : scripts/long-sleep finished with exit code 143 (time)\n'
            output = output.replace(optionalLastLine, '')

            output.should == '> running cli command in background scripts/long-sleep\n' +
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

            output = output.replaceAll(/\(\d+ms\)/, "(time)")
            output.should == '> running cli command in background scripts/long-sleep\n' +
                    '. ran cli command in background scripts/long-sleep (time)\n' +
                    '> stopping cli command in background scripts/long-sleep\n' +
                    '. stopped cli command in background scripts/long-sleep (time)\n'
        }
    }

    @Override
    void out(Object... styleOrValues) {
        output += new IgnoreAnsiString(styleOrValues).toString() + '\n'
    }

    @Override
    void err(Object... styleOrValues) {
    }
}
