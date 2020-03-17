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

package org.testingisdocumenting.webtau.cli

import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.cli.Cli.cli
import static org.testingisdocumenting.webtau.cli.CliTestUtils.supportedPlatformOnly

class CliGroovyTest {
    @Test
    void "output and exit code validation"() {
        supportedPlatformOnly {
            cli.run('scripts/hello "message to world"') {
                exitCode.should == 5

                output.should == ~/hello/
                output.should contain('world')
                output.should contain('message to world')

                error.should contain("error line two")
            }
        }
    }

    @Test
    void "capture validation result"() {
        supportedPlatformOnly {
            code {
                cli.run('scripts/hello') {
                    output.shouldNot contain('line')
                }
            } should throwException(AssertionError)

            def validationResult = cli.getLastValidationResult().toMap()
            validationResult.startTime.shouldBe greaterThan(0)
            validationResult.elapsedTime.shouldBe greaterThan(0)
            validationResult.mismatches.should == ['process output expect to not contain "line"\n' +
                                                           'process output[1]: equals "line in the middle"']
        }
    }
}
