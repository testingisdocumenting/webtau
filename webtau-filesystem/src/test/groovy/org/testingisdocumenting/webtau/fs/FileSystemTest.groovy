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


import org.junit.Test
import org.testingisdocumenting.webtau.utils.FileUtils

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.fs.FileSystem.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*

class FileSystemTest {
    @Test
    void "creating temp dir has dir path in completion step"() {
        runAndValidateOutput(containAll("> creating temp directory", "prefix: \"custom-prefix\"",
                ".created temp directory")) {
            fs.tempDir("custom-prefix")
        }
    }

    @Test
    void "creating temp dir with dir specified has dir as step input"() {
        runAndValidateOutput(~/dir: .*my-dir/) {
            fs.tempDir("my-dir", "custom-prefix")
        }
    }

    @Test
    void "creating temp file with dir specified has dir, prefix and suffix as step input"() {
        runAndValidateOutput(containAll("prefix: \"custom-prefix\"", "suffix: \"my-suffix\"")) {
            fs.tempFile("my-dir", "custom-prefix", "my-suffix")
        }
    }

    @Test
    void "replacing text with regexp report number of matches"() {
        def tempDir = fs.tempDir("custom-prefix")
        def tempFile = fs.writeText(tempDir.resolve("text.txt"), "hello 200 world 300")

        fs.replaceText(tempFile, ~/(\d+)/, '$1!')
        FileUtils.fileTextContent(tempFile).should == "hello 200! world 300!"
//
//        output.should contain("> replacing text content")
//        output.should contain(" replacement: \$1!")
//        output.should contain("> reading text from")
//        output.should contain("> writing text content of size 21")
//        output.should contain(". replaced text content: 2 matches")
    }
}
