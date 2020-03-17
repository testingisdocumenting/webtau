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

package org.testingisdocumenting.webtau.console.ansi

import org.junit.After
import org.junit.Before
import org.junit.Test

import static org.testingisdocumenting.webtau.console.ansi.Color.BLUE
import static org.testingisdocumenting.webtau.console.ansi.Color.YELLOW
import static org.testingisdocumenting.webtau.console.ansi.FontStyle.BOLD
import static org.testingisdocumenting.webtau.console.ansi.FontStyle.NORMAL

class AnsiConsoleOutputTest {
    def console = new AnsiConsoleOutput()

    def savedOut
    def savedErr

    SpyPrintStream spyiedOut
    SpyPrintStream spyiedErr

    @Before
    void saveAndReplaceStdOutAndError() {
        savedOut = System.out
        savedErr = System.err

        spyiedOut = new SpyPrintStream(savedOut)
        spyiedErr = new SpyPrintStream(savedErr)

        System.out = spyiedOut
        System.err = spyiedErr
    }

    @After
    void restoreStdOutAndError() {
        System.out = savedOut
        System.err = savedErr
    }

    @Test
    void "should support multiple colors in one line"() {
        validateBoth(["normal", BLUE, "blue", YELLOW, "yellow"]) {
            assert it.contains("\u001B[34mblue\u001B[33myellow")
        }
    }

    @Test
    void "should auto reset line at the end"() {
        validateBoth([YELLOW, "yellow"]) {
            assert it.endsWith("\u001B[0m")
        }
    }

    @Test
    void "should support bold and colorful and the same time"() {
        validateBoth(["normal", BOLD, BLUE, "blue", NORMAL, YELLOW, "yellow"]) {
            assert it.contains("\u001B[1m\u001B[34mblue\u001B[0m\u001B[33myellow")

        }
    }

    void validateBoth(List styleOfValue, Closure validation) {
        def asArray = styleOfValue.toArray()

        console.out(asArray)
        validation(spyiedOut.firstLine)

        console.err(asArray)
        validation(spyiedErr.firstLine)
    }

    class SpyPrintStream extends PrintStream {
        private List<String> printedLines = []

        SpyPrintStream(OutputStream out) {
            super(out)
        }

        @Override
        void println(Object x) {
            super.println(x)
            printedLines.add(x.toString())
        }

        String getFirstLine() {
            if (printedLines.isEmpty()) {
                throw new IllegalStateException("no printed lines")
            }

            return printedLines[0]
        }
    }
}
