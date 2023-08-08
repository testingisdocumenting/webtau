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

package org.testingisdocumenting.webtau.expectation.equality.handlers;

import org.junit.Test;
import org.testingisdocumenting.webtau.data.table.TableData;

import static org.testingisdocumenting.webtau.Matchers.*;
import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.*;

public class TableDataMatchersJavaExamplesTest {
    @Test
    public void equalityMismatch() {
        runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "table-equal-console-output", """
                X failed expecting [value] to equal ColumnA           │ ColumnB         \s
                                                                   10 │ <greater than 15>
                                                    <any of [20, 22]> │                40:
                    [value][1].ColumnA:  actual: 30 <java.lang.Integer>
                                       expected: 20 <java.lang.Integer>
                                         actual: 30 <java.lang.Integer>
                                       expected: 22 <java.lang.Integer> (Xms)
                 \s
                  ColumnA │ ColumnB
                       10 │      20
                   **30** │      40""", () -> {
            // table-equal-mismatch
            var summary = loadFromCsv("summary.csv");
            TableData expected = table("ColumnA", "ColumnB",
                                      _________________________,
                                              10, greaterThan(15),
                                   anyOf(20, 22), 40);

            actual(summary).should(equal(expected));
            // table-equal-mismatch
        });
    }

    @Test
    public void containsMismatch() {
        runExpectExceptionCaptureAndValidateOutput(AssertionError.class, "table-contain-console-output", """
                X failed expecting [value] to contain {"ColumnA": 20, "ColumnB": <greater than 15>}: no match found (Xms)
                 \s
                  ColumnA │ ColumnB
                       10 │      20
                       30 │      40""", () -> {
            // table-contain-mismatch
            var summary = loadFromCsv("summary.csv");
            actual(summary).should(contain(map("ColumnA", 20, "ColumnB", greaterThan(15))));
            // table-contain-mismatch
        });
    }

    private static TableData loadFromCsv(String fileName) {
        return table("ColumnA", "ColumnB",
                     ___________________,
                            10, 20,
                            30, 40);
    }
}
