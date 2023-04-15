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

package org.testingisdocumenting.webtau.data.table

import org.junit.Test
import org.testingisdocumenting.webtau.data.render.PrettyPrintableTestBase
import org.testingisdocumenting.webtau.data.table.header.TableDataHeader

class RecordPrettyPrintTest extends PrettyPrintableTestBase {
    @Test
    void "record prints as map"() {
        def record = new Record(new TableDataHeader(["*id1", "*id2", "c1"].stream()), ["id1", "id2", "v1"].stream())
        printer.printObject(record)

        expectOutput('{"id1": "id1", "id2": "id2", "c1": "v1"}')
    }
}
