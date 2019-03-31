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

package com.twosigma.webtau.data.table;

import com.twosigma.webtau.data.table.autogen.TableDataCellValueGenerator;
import org.junit.Test;

import static com.twosigma.webtau.Ddjt.*;

public class TableDataJavaSyntaxTest {
    @Test
    public void makingSureJavaCodeCompiles() {
        TableData tableData = createTableDataWithAccessToPrevious();
        actual(tableData.numberOfRows()).should(equal(3));
    }

    // TODO move table data java tests to this test file
    // mdoc needs to support removal of "return" and semicolon from function body first
    private static TableData createTableDataWithAccessToPrevious() {
        TableDataCellValueGenerator<?> increment = cell.previous.plus(10);

        return table("Col A", "Col B", "Col C",
                    ________________________________________________,
                       "v1a",   "v1b", 10,
                       "v2a",   "v2b", increment,
                       "v2a",   "v2b", increment);
    }
}
