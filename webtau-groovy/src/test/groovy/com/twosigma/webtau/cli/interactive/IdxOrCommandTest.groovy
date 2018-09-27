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

class IdxOrCommandTest {
    @Test
    void "should recognize an index by given text"() {
        def idxOrCommand = new IdxOrCommand('199')
        idxOrCommand.idx.should == 199
        idxOrCommand.command.should == null
    }

    @Test
    void "should recognize a command by given text"() {
        def idxOrCommand = new IdxOrCommand('watch')
        idxOrCommand.idx.should == null
        idxOrCommand.command.should == InteractiveCommand.Watch
    }

    @Test
    void "should recognize a command by given short form"() {
        def idxOrCommand = new IdxOrCommand('w')
        idxOrCommand.idx.should == null
        idxOrCommand.command.should == InteractiveCommand.Watch
    }
}
