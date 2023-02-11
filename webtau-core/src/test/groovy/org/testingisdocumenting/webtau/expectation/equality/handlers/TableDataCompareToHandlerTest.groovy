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

package org.testingisdocumenting.webtau.expectation.equality.handlers

import org.junit.Test
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput

import static org.testingisdocumenting.webtau.WebTauCore.*

class TableDataCompareToHandlerTest {
    @Test
    void "compare two same size tables"() {
        def actualTable = table( "colA", "colB",
                                _______________,
                                "hello", "world",
                                    100, 200,
                                     10,  20,
                                     30,  40,
                                    130, 140)

        def expected = table( "colA", "colB",
                             _______________,
                             "hellp", "world",
                                 100, 200,
                                  10,  20,
                                  30,  40,
                                 130, 150)

        TestConsoleOutput.runAndValidateOutput('X failed expecting [value] to equal colA    │ colB   \n' +
                '                                    "hellp" │ "world"\n' +
                '                                        100 │     200\n' +
                '                                         10 │      20\n' +
                '                                         30 │      40\n' +
                '                                    ...: \n' +
                '    mismatches:\n' +
                '    \n' +
                '    [value][0].colA:   actual: "hello" <java.lang.String>\n' +
                '                     expected: "hellp" <java.lang.String>\n' +
                '                                    ^\n' +
                '    [value][4].colB:   actual: 140 <java.lang.Integer>\n' +
                '                     expected: 150 <java.lang.Integer> (Xms)\n' +
                '  \n' +
                '  colA        │ colB   \n' +
                '  **"hello"** │ "world"\n' +
                '          100 │     200\n' +
                '           10 │      20\n' +
                '           30 │      40\n' +
                '          130 │ **140**') {
            actual(actualTable).should(equal(expected))
        }
    }

    @Test
    void "compare two same size small tables"() {
        def actualTable = table( "colA", "colB",
                                _______________,
                                "hello", "world",
                                    100, 200)

        def expectedTable = table( "colA", "colB",
                                  _______________,
                                  "hellp", "world",
                                      100, 200)

        TestConsoleOutput.runAndValidateOutput('X failed expecting [value] to equal colA    │ colB   \n' +
                '                                    "hellp" │ "world"\n' +
                '                                        100 │     200: \n' +
                '    mismatches:\n' +
                '    \n' +
                '    [value][0].colA:   actual: "hello" <java.lang.String>\n' +
                '                     expected: "hellp" <java.lang.String>\n' +
                '                                    ^ (Xms)\n' +
                '  \n' +
                '  colA        │ colB   \n' +
                '  **"hello"** │ "world"\n' +
                '          100 │     200') {
            actual(actualTable).should(equal(expectedTable))
        }
    }

    @Test
    void "compare extra rows and missing columns tables"() {
        def actualTable = table( "colA", "colB",
                                _______________,
                                "hello", "world",
                                 100, 200)

        def expected = table( "colA", "colB", "colC",
                              ______________________,
                               "help", "world", "value")

        TestConsoleOutput.runAndValidateOutput('X failed expecting [value] to equal colA   │ colB    │ colC   \n' +
                '                                    "help" │ "world" │ "value": \n' +
                '    mismatches:\n' +
                '    \n' +
                '    [value][0].colA:   actual: "hello" <java.lang.String>\n' +
                '                     expected: "help" <java.lang.String>\n' +
                '                                   ^\n' +
                '    [value]: missing columns: colC\n' +
                '    [value]: extra rows:\n' +
                '             :colA|colB:\n' +
                '             .____.____.\n' +
                '             |100 |200 |\n' +
                '             .____.____| (Xms)\n' +
                '  \n' +
                '  colA        │ colB   \n' +
                '  **"hello"** │ "world"\n' +
                '          100 │     200') {
            actual(actualTable).should(equal(expected))
        }
    }
}
