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

package com.twosigma.webtau.data.table

import org.junit.Test

import static com.twosigma.webtau.Ddjt.table

class TableDataTest {
    @Test
    void "should create table using header and values convenient methods"() {
        def tableData = table("Col A", "Col B", "Col C").values(
                                 "v1a",   "v1b", "v1c",
                                 "v2a",   "v2b", "v2c")

        assert tableData.numberOfRows() == 2
        assert tableData.row(0).toMap() == ["Col A": "v1a", "Col B": "v1b", "Col C": "v1c"]
        assert tableData.row(1).toMap() == ["Col A": "v2a", "Col B": "v2b", "Col C": "v2c"]
    }

    @Test(expected = IllegalArgumentException)
    void "should report columns number mismatch during table creation using header and values vararg methods"() {
        table("Col A", "Col B", "Col C").values(
                 "v1a",   "v1b", "v1c",
                 "v2a",   "v2b")
    }
}
