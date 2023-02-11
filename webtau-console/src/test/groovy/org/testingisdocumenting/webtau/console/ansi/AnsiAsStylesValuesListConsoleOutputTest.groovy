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

package org.testingisdocumenting.webtau.console.ansi

import org.junit.Before
import org.junit.Test

class AnsiAsStylesValuesListConsoleOutputTest {
    AnsiAsStylesValuesListConsoleOutput console

    @Before
    void initConsole() {
        console = new AnsiAsStylesValuesListConsoleOutput()
    }

    @Test
    void "styles should be combined with text that matches it"() {
        console.out("hello", Color.BLUE, FontStyle.BOLD, "world", 100, Color.GREEN, "trees")
        console.out("nextLine", Color.GREEN)
        console.out(Color.YELLOW, "yellowLine")
        console.out()
        console.out()
        console.out("lastLine")

        assert console.toListOfListsOfMaps() == [
                [[styles: [], text: "hello"], [styles: ["blue", "bold"], text: "world100"], [styles: ["green"], text: "trees"]],
                [[styles: [], text: "nextLine"]], [[styles: ["yellow"], text: "yellowLine"]],
                [],
                [],
                [[styles: [], text: "lastLine"]]]
    }

    @Test
    void "single word"() {
        console.out("hello")
        assert console.toListOfListsOfMaps() == [[[styles: [], text: "hello"]]]
    }
}
