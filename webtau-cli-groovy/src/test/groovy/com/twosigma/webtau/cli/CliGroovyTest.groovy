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

package com.twosigma.webtau.cli

import org.junit.Test

import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.contain
import static com.twosigma.webtau.Ddjt.greaterThan
import static com.twosigma.webtau.Ddjt.throwException
import static com.twosigma.webtau.cli.Cli.cli
import static com.twosigma.webtau.cli.CliTestUtils.nixOnly

class CliGroovyTest {
    @Test
    void "output and exit code validation"() {
        nixOnly {
            cli.run('scripts/hello "message to world"') {
                exitCode.should == 5

                output.should == ~/hello/
                output.should contain('world')
                output.should contain('"message to world"')
            }
        }
    }

    @Test
    void "capture validation result"() {
        nixOnly {
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
