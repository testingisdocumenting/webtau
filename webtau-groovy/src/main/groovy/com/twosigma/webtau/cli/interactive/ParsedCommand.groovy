/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

import com.twosigma.webtau.utils.NumberUtils
import groovy.transform.PackageScope

@PackageScope
class ParsedCommand {
    List<Integer> indexes = []
    List<InteractiveCommand> commands = []
    List<String> unrecognized = []
    String error
    int errorTextIdx

    ParsedCommand(String text) {
        def parser = new Parser(text)
        parser.parse()
    }

    private static InteractiveCommand findCommand(String text) {
        return InteractiveCommand.values().find { it.matches(text) }
    }

    private static Integer convertToNumber(String text) {
        return NumberUtils.convertStringToNumber(text)
    }

    private static enum ParserState {
        Seeking,
        SeekingPotentialRange,
        Command,
        Number,
        RangeSecondNumber,
        Error
    }

    private class Parser {
        private final String text

        private StringBuilder current = new StringBuilder()

        private ParserState state
        private int idx

        Parser(String text) {
            this.text = text
            this.state = ParserState.Seeking
        }

        void parse() {
            for (idx = 0; idx < text.length(); idx++) {
                def c = text.charAt(idx)

                handle(c)

                if (state == Error) {
                    break
                }
            }

            handleDelimiter()
        }

        void handle(char c) {
            if (isDelimiter(c)) {
                if (current.size() > 0) {
                    handleDelimiter()
                }
                return
            }

            switch (state) {
                case ParserState.Seeking:
                    handleFirstChar(c)
                    break
                case ParserState.Command:
                    handleCommandChar(c)
                    break
                case ParserState.Number:
                    handleNumberChar(c)
                    break
                case ParserState.SeekingPotentialRange:
                    handlePotentialRange(c)
                    break
                case ParserState.RangeSecondNumber:
                    handleSecondNumberChar(c)
                    break
            }
        }

        void handleDelimiter() {
            switch (state) {
                case ParserState.Command:
                    def commandAsText = flush()
                    def command = findCommand(commandAsText)
                    if (command) {
                        commands.add(command)
                    } else {
                        unrecognized.add(commandAsText)
                        markError('unrecognized command: ' + commandAsText)
                    }
                    state = ParserState.Seeking
                    break

                case ParserState.Number:
                    indexes.add(convertToNumber(flush()))
                    state = ParserState.SeekingPotentialRange
                    break

                case ParserState.RangeSecondNumber:
                    indexes.addAll(createRange(indexes.last() + 1, convertToNumber(flush())))
                    state = ParserState.Seeking
                    break
            }
        }

        void handleFirstChar(char c) {
            if (c.isDigit()) {
                state = ParserState.Number
            } else {
                state = ParserState.Command
            }

            current.append(c)
        }

        void handleCommandChar(char c) {
            current.append(c)
        }

        void handleNumberChar(char c) {
            if (isRangeSymbol(c)) {
                handleDelimiter()
                state = ParserState.RangeSecondNumber
                return
            }

            if (!c.isDigit()) {
                markError('incorrect number')
            }

            current.append(c)

            if (current.length() > 3) {
                markError('number is too big')
            }
        }

        void handleSecondNumberChar(char c) {
            if (!c.isDigit()) {
                markError('incorrect number')
            }

            current.append(c)
        }

        void handlePotentialRange(char c) {
            if (isRangeSymbol(c)) {
                state = ParserState.RangeSecondNumber
            } else {
                state = ParserState.Seeking
                handle(c)
            }
        }

        List<Integer> createRange(Integer a, Integer b) {
            return a..b
        }

        String flush() {
            String result = current.toString()
            current = new StringBuilder()
            return result
        }

        void markError(message) {
            error = message
            errorTextIdx = idx
            state = ParserState.Error
        }

        private static boolean isRangeSymbol(char c) {
            return c == '-'
        }

        private static boolean isDelimiter(char c) {
            return c == ' ' || c == ','
        }
    }
}
