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

import org.junit.Test

class ParsedCommandTest {
    @Test
    void "should recognize an index by given text"() {
        def parsedCommand = new ParsedCommand('199')
        parsedCommand.indexes.should == [199]
        parsedCommand.commands.should == []
    }

    @Test
    void "should capture multiple indexes by given text"() {
        def parsedCommand = new ParsedCommand('0 1 4 5')
        parsedCommand.indexes.should == [0, 1, 4, 5]
        parsedCommand.commands.should == []
    }

    @Test
    void "should capture indexes range and convert to numbers"() {
        def parsedCommand = new ParsedCommand('8 0 - 5 10')
        parsedCommand.indexes.should == [8, 0, 1, 2, 3, 4, 5, 10]
        parsedCommand.commands.should == []
    }

    @Test
    void "should capture indexes range without space and convert to numbers"() {
        def parsedCommand = new ParsedCommand('8 0-5 10')
        parsedCommand.indexes.should == [8, 0, 1, 2, 3, 4, 5, 10]
        parsedCommand.commands.should == []
    }

    @Test
    void "should use spaces or commas as a separator"() {
        def parsedCommand = new ParsedCommand('8, 3 4, 11')
        parsedCommand.indexes.should == [8, 3, 4, 11]
        parsedCommand.commands.should == []
    }

    @Test
    void "number max length should be three"() {
        def parsedCommand = new ParsedCommand('8341')
        parsedCommand.error.should == 'index is too big'
        parsedCommand.errorTextIdx.should == 3
        parsedCommand.indexes.should == []
        parsedCommand.commands.should == []
    }

    @Test
    void "should detect number parsing error"() {
        def parsedCommand = new ParsedCommand('83ab')
        parsedCommand.error.should == 'incorrect number'
        parsedCommand.errorTextIdx.should == 2
        parsedCommand.indexes.should == []
        parsedCommand.commands.should == []
    }

    @Test
    void "should recognize a command by given text"() {
        def parsedCommand = new ParsedCommand('watch')
        parsedCommand.indexes.should == []
        parsedCommand.commands.should == [InteractiveCommand.Watch]
    }

    @Test
    void "should recognize a command by given short form"() {
        def parsedCommand = new ParsedCommand('w')
        parsedCommand.indexes.should == []
        parsedCommand.commands.should == [InteractiveCommand.Watch]
    }

    @Test
    void "should capture multiple indexes and commands by given text"() {
        def parsedCommand = new ParsedCommand('r 0 1 4 5 b')
        parsedCommand.indexes.should == [0, 1, 4, 5]
        parsedCommand.commands.should == [InteractiveCommand.Run, InteractiveCommand.Back]
    }

    @Test
    void "should capture unrecognized commands"() {
        def parsedCommand = new ParsedCommand('15 red')
        parsedCommand.indexes.should == [15]
        parsedCommand.commands.should == []
        parsedCommand.unrecognized.should == ['red']
        parsedCommand.error.should == 'unrecognized command: red'
        parsedCommand.errorTextIdx.should == 6
    }
}
