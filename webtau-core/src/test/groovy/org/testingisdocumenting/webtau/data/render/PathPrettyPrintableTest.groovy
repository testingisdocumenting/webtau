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

import org.junit.Test

import java.nio.file.Paths

class PathPrettyPrintableTest extends PrettyPrintableTestBase {
    @Test
    void "print full path"() {
        printer.printObject(Paths.get("/parent/child/file.ext"))
        expectOutput("/parent/child/file.ext")
    }

    @Test
    void "print name only"() {
        printer.printObject(Paths.get("file.ext"))
        expectOutput("file.ext")
    }

    @Test
    void "print name only without ext"() {
        printer.printObject(Paths.get("file"))
        expectOutput("file")
    }

    @Test
    void "print dir only"() {
        printer.printObject(Paths.get("/dir"))
        expectOutput("/dir")
    }

    @Test
    void "print relative dir and file"() {
        printer.printObject(Paths.get("dir/file"))
        expectOutput("dir/file")
    }
}
