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

package com.example.tests.junit5;

import org.junit.jupiter.api.Test;
import org.testingisdocumenting.webtau.junit5.WebTau;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.WebTauDsl.*;

@WebTau
public class BrowserTablesJavaTest {
    @Test
    public void standardTable() {
        browser.open("/tables");

        // table-data-validation
        var summaryTable = browser.table("#summary");

        summaryTable.should(equal(table("column A", "column B", "column C",
                                       ____________________________________,
                                            "A-1" ,      "B-1", "C-1",
                                            "A-2" ,      "B-2", "C-2" )));
        // table-data-validation
    }

    @Test
    public void extractSingleTable() {
        browser.open("/tables");

        // extract-single-table-data
        var summaryTable = browser.table("#summary");
        var tableData = summaryTable.extractTableData();

        data.csv.write("table-data.csv", tableData);
        // extract-single-table-data

        defer(() -> fs.delete("table-data.csv"));
    }

    @Test
    public void extractFromAllTables() {
        // extract-all-table-data
        var tablesList = browser.table("table");
        var combinedTableData = tablesList.extractAndMergeTableData();

        data.csv.write("combined-table-data.csv", combinedTableData);
        // extract-all-table-data

        defer(() -> fs.delete("combined-table-data.csv"));
    }
}
