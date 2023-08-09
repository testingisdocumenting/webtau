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
import org.testingisdocumenting.webtau.data.table.TableData

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.WebTauCore.*

class TableDataMatchersGroovyExamplesTest {
    @Test
    void equalityMismatch() {
        code {
            // table-equal-mismatch
            def summary = loadFromCsv("summary.csv")
            def expected = [  "ColumnA" | "ColumnB" ] {
                           ____________________________
                                10      | greaterThan(15)
                          anyOf(20, 22) | 40 }
            summary.should == expected
            // table-equal-mismatch
        } should throwException(AssertionError)
    }

    @Test
    void containsMismatch() {
        code {
            // table-contain-mismatch
            def summary = loadFromCsv("summary.csv")
            summary.should contain(["ColumnA": 20, "ColumnB": greaterThan(15)])
            // table-contain-mismatch
        } should throwException(AssertionError)
    }

    private static TableData loadFromCsv(String fileName) {
        return ["ColumnA" | "ColumnB" ] {
               _________________________
                        10| 20
                        30| 40 }
    }
}
