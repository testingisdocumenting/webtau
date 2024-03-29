/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.data.render

import org.junit.Before
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput

import static org.testingisdocumenting.webtau.Matchers.*

class PrettyPrintableTestBase {
    def consoleOutput = new TestConsoleOutput()
    PrettyPrinter printer

    @Before
    void init() {
        consoleOutput.clear()
        printer = new PrettyPrinter(0)
    }

    void expectOutput(String expected) {
        printer.flushCurrentLine()
        printer.renderToConsole(consoleOutput)

        println consoleOutput.colorOutput
        actual(consoleOutput.noColorOutput, "prettyPrinted").should(equal(expected))
    }
}
