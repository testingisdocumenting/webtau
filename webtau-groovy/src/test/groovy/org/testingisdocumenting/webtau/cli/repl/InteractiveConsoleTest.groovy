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

package org.testingisdocumenting.webtau.cli.repl

import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.IgnoreAnsiString
import org.junit.Before
import org.junit.Test

class InteractiveConsoleTest {
    private ConsoleCapture capturedConsole = new ConsoleCapture()

    @Before
    void init() {
        capturedConsole.reset()
        ConsoleOutputs.add(capturedConsole)
    }

    @Before
    void clean() {
        ConsoleOutputs.remove(capturedConsole)
    }

    @Test
    void "should repeatedly ask for an index or a command if input is not valid"() {
        def console = createConsole('wrong input\nwrong input2\n  3 \n')
        def command = console.readCommand([InteractiveCommand.Quit, InteractiveCommand.Back])

        capturedConsole.toString().should == 'unrecognized command: wrong\n' +
                'wrong input\n' +
                '    ^\n' +
                'enter a number or a command\n' +
                'quit, q - stop interactive mode\n' +
                'back, b - go back\n' +
                'unrecognized command: wrong\n' +
                'wrong input2\n' +
                '    ^\n' +
                'enter a number or a command\n' +
                'quit, q - stop interactive mode\n' +
                'back, b - go back'

        command.indexes.should == [3]
    }

    @Test
    void "should parse command and make sure it is part of allowed commands"() {
        def console = createConsole('  run \n quit stop \nback  \n')
        def command = console.readCommand([InteractiveCommand.Quit, InteractiveCommand.Back, InteractiveCommand.StopWatch])

        command.commands*.name().should == ['Back']
    }

    private static InteractiveConsole createConsole(String userInput) {
        Reader inputString = new StringReader(userInput)
        BufferedReader input = new BufferedReader(inputString)

        return new InteractiveConsole(input)
    }

    private static class ConsoleCapture implements ConsoleOutput {
        List<String> parts = []

        void reset() {
            parts.clear()
        }

        @Override
        void out(Object... styleOrValues) {
            parts.add(new IgnoreAnsiString(styleOrValues).toString())
        }

        @Override
        void err(Object... styleOrValues) {
        }

        @Override
        String toString() {
            return parts.join('\n')
        }
    }
}
