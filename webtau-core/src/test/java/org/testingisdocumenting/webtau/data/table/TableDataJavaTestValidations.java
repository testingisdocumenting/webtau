/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.data.table;

import java.time.LocalDate;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class TableDataJavaTestValidations {
    public static void validateSimpleTableData(TableData tableData) {
        actual(tableData.numberOfRows()).should(equal(2));
        actual(tableData.row(0).toMap()).should(equal(
                mapOf("Col A", "v1a", "Col B", "v1b", "Col C", "v1c")));
        actual(tableData.row(1).toMap()).should(equal(
                mapOf("Col A", "v2a", "Col B", "v2b", "Col C", "v2c")));
    }

    public static void validateSimpleTableDataAfterReplace(TableData tableData) {
        actual(tableData.numberOfRows()).should(equal(2));
        actual(tableData.row(0).toMap()).should(equal(
                mapOf("Col A", "v1a", "Col B", "v1b_", "Col C", "v1c")));
        actual(tableData.row(1).toMap()).should(equal(
                mapOf("Col A", "v2a", "Col B", "v2b", "Col C", "v2c")));
    }

    public static void validatePermute(TableData tableData) {
        actual(tableData.numberOfRows()).should(equal(6));
        actual(tableData.row(0).toMap()).should(equal(
                mapOf("Col A", true, "Col B", "v1b", "Col C", "a")));
        actual(tableData.row(1).toMap()).should(equal(
                mapOf("Col A", false, "Col B", "v1b", "Col C", "a")));
        actual(tableData.row(2).toMap()).should(equal(
                mapOf("Col A", true, "Col B", "v1b", "Col C", "b")));
        actual(tableData.row(3).toMap()).should(equal(
                mapOf("Col A", false, "Col B", "v1b", "Col C", "b")));
        actual(tableData.row(4).toMap()).should(equal(
                mapOf("Col A", "v2a", "Col B", 10, "Col C", "v2c")));
        actual(tableData.row(5).toMap()).should(equal(
                mapOf("Col A", "v2a", "Col B", 20, "Col C", "v2c")));
    }

    public static void validatePermuteAndGuid(TableData tableData) {
        actual(tableData.numberOfRows()).should(equal(6));
        actual(tableData.row(0).toMap()).should(equal(
                mapOf("ID", notEqual(""), "Col A", true, "Col B", "v1b", "Col C", "a")));
        actual(tableData.row(1).toMap()).should(equal(
                mapOf("ID", notEqual(""), "Col A", false, "Col B", "v1b", "Col C", "a")));
        actual(tableData.row(2).toMap()).should(equal(
                mapOf("ID", notEqual(""), "Col A", true, "Col B", "v1b", "Col C", "b")));
        actual(tableData.row(3).toMap()).should(equal(
                mapOf("ID", notEqual(""), "Col A", false, "Col B", "v1b", "Col C", "b")));
        actual(tableData.row(4).toMap()).should(equal(
                mapOf("ID", notEqual(""), "Col A", "v2a", "Col B", 10, "Col C", "v2c")));
        actual(tableData.row(5).toMap()).should(equal(
                mapOf("ID", notEqual(""), "Col A", "v2a", "Col B", 20, "Col C", "v2c")));

        actual((String) tableData.row(0).get("ID")).shouldNot(equal(tableData.row(1).get("ID")));
        actual((String) tableData.row(0).get("ID")).shouldNot(equal(tableData.row(2).get("ID")));
        actual((String) tableData.row(0).get("ID")).shouldNot(equal(tableData.row(3).get("ID")));
        actual((String) tableData.row(0).get("ID")).shouldNot(equal(tableData.row(4).get("ID")));
        actual((String) tableData.row(0).get("ID")).shouldNot(equal(tableData.row(5).get("ID")));
    }

    public static void validateAboveValue(TableData tableData) {
        actual(tableData.numberOfRows()).should(equal(6));

        LocalDate firstDate = LocalDate.of(2016, 6, 20);
        actual(tableData.row(0).toMap()).should(equal(
                mapOf("Name", "John", "Start Date", firstDate, "Games To Play", 10)));
        actual(tableData.row(1).toMap()).should(equal(
                mapOf("Name", "Bob", "Start Date", firstDate, "Games To Play", 8)));
        actual(tableData.row(2).toMap()).should(equal(
                mapOf("Name", "Mike", "Start Date", firstDate, "Games To Play", 14)));

        LocalDate secondDate = LocalDate.of(2016, 6, 22);
        actual(tableData.row(3).toMap()).should(equal(
                mapOf("Name", "Drew", "Start Date", secondDate, "Games To Play", 10)));
        actual(tableData.row(4).toMap()).should(equal(
                mapOf("Name", "Pete", "Start Date", secondDate, "Games To Play", 11)));
        actual(tableData.row(5).toMap()).should(equal(
                mapOf("Name", "Max", "Start Date", secondDate, "Games To Play", 3)));
    }

    public static void validateAboveValueWithMath(TableData tableData) {
        actual(tableData.numberOfRows()).should(equal(3));

        LocalDate firstDate = LocalDate.of(2016, 6, 20);
        actual(tableData.row(0).toMap()).should(equal(
                mapOf("Name", "John", "Start Date", firstDate, "Games To Play", 10)));
        actual(tableData.row(1).toMap()).should(equal(
                mapOf("Name", "Bob", "Start Date", firstDate, "Games To Play", 11)));
        actual(tableData.row(2).toMap()).should(equal(
                mapOf("Name", "Mike", "Start Date", firstDate, "Games To Play", 12)));
    }
}
