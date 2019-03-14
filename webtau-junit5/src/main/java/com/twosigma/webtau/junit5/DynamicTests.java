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

package com.twosigma.webtau.junit5;

import com.twosigma.webtau.data.table.Record;
import com.twosigma.webtau.data.table.TableData;
import org.junit.jupiter.api.DynamicTest;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class DynamicTests {
    private static final String LABEL_COLUMN = "label";

    private DynamicTests() {
    }

    public static Stream<DynamicTest> fromTable(String useCasesPrefix, TableData useCases, Consumer<Record> test) {
        String displayNamePrefix = useCasesPrefix.isEmpty() ? "" : useCasesPrefix + ": ";
        return useCases.rowsStream().map(row -> DynamicTest.dynamicTest(
                getDisplayName(displayNamePrefix, row),
                () -> test.accept(row)));
    }

    private static String getDisplayName(String prefix, Record row) {
        String rowLabel = row.getHeader().hasColumn(LABEL_COLUMN)
            ? row.get(LABEL_COLUMN).toString()
            : row.toString();
        return prefix + rowLabel;
  }
}
