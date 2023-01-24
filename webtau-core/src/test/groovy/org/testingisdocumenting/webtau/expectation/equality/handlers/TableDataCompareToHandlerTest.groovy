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
                                    100, 200)

        def expected = table( "colA", "colB",
                             _______________,
                             "help", "world",
                                100, 220)

        TestConsoleOutput.runAndValidateOutput('X failed expecting [value] to equal \n' +
                ':colA  |colB   :\n' +
                '.______._______.\n' +
                '|"help"|"world"|\n' +
                '.______._______|\n' +
                '|100   |220    |\n' +
                '.______._______|\n' +
                ': \n' +
                '    mismatches:\n' +
                '    \n' +
                '    [value][0].colA:   actual: "hello" <java.lang.String>\n' +
                '                     expected: "help" <java.lang.String>\n' +
                '                                   ^\n' +
                '    [value][1].colB:   actual: 200 <java.lang.Integer>\n' +
                '                     expected: 220 <java.lang.Integer> (Xms)\n' +
                '  colA        │ colB   \n' +
                '  **"hello"** │ "world"\n' +
                '          100 │ **200**\n  ') {
            actual(actualTable).should(equal(expected))
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

        TestConsoleOutput.runAndValidateOutput('X failed expecting [value] to equal \n' +
                ':colA  |colB   |colC   :\n' +
                '.______._______._______.\n' +
                '|"help"|"world"|"value"|\n' +
                '.______._______._______|\n' +
                ': \n' +
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
                '  colA        │ colB   \n' +
                '  **"hello"** │ "world"\n' +
                '          100 │     200\n' +
                '  ') {
            actual(actualTable).should(equal(expected))
        }
    }
}
