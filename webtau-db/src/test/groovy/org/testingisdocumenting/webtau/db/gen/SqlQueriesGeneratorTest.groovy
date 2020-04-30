/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.db.gen

import org.junit.Test

class SqlQueriesGeneratorTest {
    @Test
    void "should generate insert statement with placeholders"() {
        def table = ["column_a" | "column_b"] {
                     _________________________
                     "v1"       | 2      }

        SqlQueriesGenerator.insert("MY_TABLE", table.row(0))
                .should == "INSERT INTO MY_TABLE (column_a, column_b) VALUES (?, ?)"
    }
}
