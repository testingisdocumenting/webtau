/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau

import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.IgnoreAnsiString

import static org.testingisdocumenting.webtau.Matchers.contain

class WebTauCoreTest implements ConsoleOutput {
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
    void "repeatStep should accept Closures"() {
        def result = []

        repeatStep("test step", 5) { ctx ->
            println ctx.attemptNumber
            result << ctx.attemptNumber
        }

        result.should == [1, 2, 3, 4, 5]
    }

    @Test
    void "step should accept map as step input and return value"() {
        def r = step("my custom step", [a: 1, b: 'hello']) {
            return 10
        }

        output.should contain('> my custom step\n' +
                '  a: 1\n' +
                '  b: hello')

        r.should == 10
    }

    @Test
    void "step should accept map as step input with no step return value"() {
        step("my custom step", [a: 1, b: 'hello']) {
        }

        output.should contain('> my custom step\n' +
                '  a: 1\n' +
                '  b: hello')
    }

    @Override
    void out(Object... styleOrValues) {
        output += new IgnoreAnsiString(styleOrValues).toString() + '\n'
    }

    @Override
    void err(Object... styleOrValues) {
    }
}
