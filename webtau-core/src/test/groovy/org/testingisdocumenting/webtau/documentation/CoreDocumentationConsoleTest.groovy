/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.documentation

import org.junit.Test
import org.testingisdocumenting.webtau.console.ansi.Color
import org.testingisdocumenting.webtau.console.ansi.FontStyle
import org.testingisdocumenting.webtau.utils.FileUtils

import static org.testingisdocumenting.webtau.Matchers.actual
import static org.testingisdocumenting.webtau.Matchers.equal
import static org.testingisdocumenting.webtau.WebTauCore.doc

class CoreDocumentationConsoleTest {
    @Test
    void "console capture only output from the current thread"() {
        doc.console.capture("my-output-example") {
            println Color.GREEN.toString() + "hello world test" + FontStyle.NORMAL

            def t1 = Thread.start {
                sleep 10
                println "from thread one"
                sleep 10
            }

            def t2 = Thread.start {
                sleep 10
                println "from thread two"
                sleep 10
            }

            t1.join()
            t2.join()
        }

        def path = DocumentationArtifactsLocation.resolve("my-output-example.txt")
        actual(FileUtils.fileTextContent(path)).should(equal("\u001B[32mhello world test\u001B[0m\n"))
    }
}
