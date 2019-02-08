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

package com.twosigma.webtau.cli.interactive

import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.AutoResetAnsiString
import com.twosigma.webtau.console.ansi.Color
import groovy.transform.PackageScope

@PackageScope
class InteractiveConsole {
    private BufferedReader inReader

    InteractiveConsole(BufferedReader inReader) {
        this.inReader = inReader
    }

    void readAndHandleParsedCommand(List<InteractiveCommand> allowedCommands, Closure commandHandler, Closure idxHandler) {
        while (true) {
            def parsedCommand = readCommand(allowedCommands)

            if (!parsedCommand.commands.isEmpty()) {
                commandHandler(parsedCommand)
                return
            }

            def done = idxHandler(parsedCommand.indexes)
            if (done) {
                return
            }
        }
    }

    ParsedCommand readCommand(List<InteractiveCommand> allowedCommands) {
        while (true) {
            def line = readLine().toLowerCase().trim()
            def parsedCommand = new ParsedCommand(line)

            def commands = parsedCommand.commands

            if ((commands.isEmpty() || commands.size() > 1 || !parsedCommand.unrecognized.isEmpty() || !allowedCommands.find { it == commands[0] })
                    && parsedCommand.indexes.isEmpty()) {
                displayError(parsedCommand, line)
                ConsoleOutputs.out(Color.RED, 'enter a number or a command')

                displayCommands(allowedCommands)
            } else {
                return parsedCommand
            }
        }
    }

    static void displayPrompt() {
        print new AutoResetAnsiString(Color.GREEN, 'webtau', Color.YELLOW, ' > ')
    }

    static void println(Object... styleOrValues) {
        ConsoleOutputs.out(styleOrValues)
    }

    static void displayCommands(List<InteractiveCommand> commands) {
        commands.each InteractiveConsole.&displayCommand
    }

    static void displayCommand(InteractiveCommand command) {
        ConsoleOutputs.out(Color.BLUE, command.prefixes.join(', '),
                Color.YELLOW, ' - ', command.description)
    }

    static void displayError(ParsedCommand parsedCommand, String line) {
        ConsoleOutputs.out(Color.RED, parsedCommand.error)
        ConsoleOutputs.out(Color.BLUE, line)

        def indicator = "^"
        ConsoleOutputs.out(Color.RED, parsedCommand.errorTextIdx > 0 ?
                String.format("%" + parsedCommand.errorTextIdx + "s", indicator):
                indicator)
    }

    private String readLine() {
        displayPrompt()
        return inReader.readLine()
    }
}