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

package org.testingisdocumenting.webtau.fs

import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.IgnoreAnsiString

import static org.testingisdocumenting.webtau.Matchers.contain
import static org.testingisdocumenting.webtau.fs.FileSystem.fs

class FileSystemTest implements ConsoleOutput {
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
    void "creating temp dir has dir path in completion step"() {
        fs.tempDir("custom-prefix")
        println output

        output.should contain("> creating temp directory with prefix custom-prefix")
        output.should == ~/\. created temp directory .*custom-prefix.+\s\(.+ms\)/
    }

    @Override
    void out(Object... styleOrValues) {
        output += new IgnoreAnsiString(styleOrValues).toString() + '\n'
    }

    @Override
    void err(Object... styleOrValues) {
    }
}
