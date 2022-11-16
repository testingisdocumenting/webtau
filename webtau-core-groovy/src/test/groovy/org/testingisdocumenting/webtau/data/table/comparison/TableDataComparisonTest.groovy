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

package org.testingisdocumenting.webtau.data.table.comparison

import org.junit.Test
import org.testingisdocumenting.webtau.data.table.TableData

class TableDataComparisonTest {
    @Test
    void "should have no mismatches when values in rows match"() {
        def actual = ["a" | "b" | "c"] {
                      ______________
                       10 | 20  | 30
                       20 | 40  | 60 }

        def expected = ["a" | "b" | "c"] {
                        ______________
                         10 | 20  | 30
                         20 | 40  | 60 }


        def result = TableDataComparison.compare(actual, expected)
        assertNoMismatches(result)
    }

    @Test
    void "should have no mismatches when values match by key"() {
        def actual = ["*id" | "b" | "c"] {
                      _________________
                         10 | 20  | 30
                         20 | 40  | 60 }

        def expected = ["*id" | "b" | "c"] {
                        __________________
                           20 | 40  | 60
                           10 | 20  | 30 }

        def result = TableDataComparison.compare(actual, expected)
        assertNoMismatches(result)
    }

    @Test
    void "should have no mismatches when values match by key and keys defined only on expected data set"() {
        def actual = ["id" | "b" | "c"] {
                      _________________
                        10 | 20  | 30
                        20 | 40  | 60 }

        def expected = ["*id" | "b" | "c"] {
                        __________________
                           20 | 40  | 60
                           10 | 20  | 30 }

        def result = TableDataComparison.compare(actual, expected)
        assertNoMismatches(result)
    }

    @Test
    void "should record mismatches associated with row and column"() {
        def actual = ["a" | "b" | "c"] {
                      ______________
                       10 | 20  | 30
                       20 | 40  | 60 }

        def expected = ["a" | "b" | "c"] {
                        ______________
                         10 | 22  | 30
                         20 | 40  | 61 }


        def result = TableDataComparison.compare(actual, expected)
        result.messageByActualRowIdxAndColumn.should == [
                0: [b: ~/expected: 22/],
                1: [c: ~/expected: 61/]]
        result.messageByExpectedRowIdxAndColumn.should == [
                0: [b: ~/expected: 22/],
                1: [c: ~/expected: 61/]]
    }

    @Test
    void "should report missing columns"() {
        def actual = ["a" | "b" | "c"] {
                      ______________
                       10 | 20  | 30 }

        def expected = ["a" | "b" | "e" | "c" | "d"] {
                        ____________________________
                         10 | 20  | 1   | 30  | 40 }

        def result = TableDataComparison.compare(actual, expected)
        result.missingColumns.should == ["d", "e"]
    }

    @Test
    void "should report missing columns when no rows are present"() {
        def actual = new TableData(["a", "b"])
        def expected = new TableData(["A", "B"])

        def result = TableDataComparison.compare(actual, expected)
        result.missingColumns.should == ["A", "B"]

        result.areEqual().should == false
    }

    @Test
    void "should report missing rows (no key columns)"() {
        def actual = ["a" | "b" | "c"] {
                      ______________
                       10 | 20  | 30 }

        def expected = ["a" | "b" | "c"] {
                        ______________
                        10 | 20 | 30
                        20 | 60 | 130
                        40 | 80 | 230 }

        def result = TableDataComparison.compare(actual, expected)
        def missingRows = result.getMissingRows()

        missingRows.size().should == 2
        missingRows.row(0).should == [a: 20, b: 60, c: 130]
        missingRows.row(1).should == [a: 40, b: 80, c: 230]
    }

    @Test
    void "should report missing rows (with key columns)"() {
        def actual = ["*id" | "b" | "c"] {
                      _________________
                         10 | 20  | 30 }

        def expected = ["*id" | "b" | "c"] {
                         _______________
                          20 | 60 | 130
                          10 | 20 | 30
                          40 | 80 | 230 }

        def result = TableDataComparison.compare(actual, expected)
        def missingRows = result.getMissingRows()

        missingRows.size().should == 2
        missingRows.row(0).should == [id: 20, b: 60, c: 130]
        missingRows.row(1).should == [id: 40, b: 80, c: 230]
    }

    @Test
    void "should report extra rows (no key columns)"() {
        def actual = ["a" | "b" | "c"] {
                       ______________
                       10 | 20  | 30
                       22 | 62  | 132
                       42 | 82  | 232 }

        def expected = ["a" | "b" | "c"] {
                        ______________
                        10 | 20 | 30 }

        def result = TableDataComparison.compare(actual, expected)
        def extraRows = result.getExtraRows()

        extraRows.numberOfRows().should == 2
        extraRows.row(0).should == [a: 22, b: 62, c: 132]
        extraRows.row(1).should == [a: 42, b: 82, c: 232]
    }

    @Test
    void "should report extra rows (with key columns)"() {
        def actual = ["*id" | "b" | "c"] {
                      ________________
                         22 | 62  | 132
                         10 | 20  | 30
                         42 | 82  | 232 }

        def expected = ["*id" | "b" | "c"] {
                        _________________
                           10 | 20 | 30 }

        def result = TableDataComparison.compare(actual, expected)
        def extraRows = result.getExtraRows()

        extraRows.numberOfRows().should == 2
        extraRows.row(0).should == [id: 22, b: 62, c: 132]
        extraRows.row(1).should == [id: 42, b: 82, c: 232]
    }

    @Test
    void "should report extra rows (with key columns only in expected)"() {
        def actual = ["id" | "b" | "c"] {
                      _________________
                        22 | 62  | 132
                        10 | 20  | 30
                        42 | 82  | 232 }

        def expected = ["*id" | "b" | "c"] {
                        _________________
                           10 | 20 | 30 }

        def result = TableDataComparison.compare(actual, expected)
        def extraRows = result.getExtraRows()

        extraRows.numberOfRows().should == 2
        extraRows.row(0).should == [id: 22, b: 62, c: 132]
        extraRows.row(1).should == [id: 42, b: 82, c: 232]
    }

    private static void assertNoMismatches(TableDataComparisonResult result) {
        result.missingColumns.should == []
        result.extraRows.should == []
        result.missingRows.should == []
        result.messageByActualRowIdxAndColumn.should == [:]
    }
}
