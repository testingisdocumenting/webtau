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

import com.twosigma.webtau.data.table.header.Header
import org.junit.Test

import static com.twosigma.webtau.WebTauCore.*

class RecordTest {
    @Test
    void "should be convertible to map"() {
        def record = new Record(new Header(["n1", "n2"].stream()), ["v1", null].stream())
        actual(record.toMap()).should(equal([n1: 'v1', n2: null]))
    }

    @Test
    void "should have key defined if header has key columns"() {
        def record = new Record(new Header(["*id1", "*id2", "c1"].stream()), ["id1", "id2", "v1"].stream())
        assert record.key.values == ["id1", "id2"]
    }

    @Test
    void "should have null key if header has no key columns"() {
        def record = new Record(new Header(["c1", "c2", "c3"].stream()), ["v1", "v2", "v3"].stream())
        assert record.key == null
    }

    @Test
    void "should return defaultValue if a columnName is not present"() {
        def record = new Record(new Header(["c1", "c2", "c3"].stream()), ["v1", "v2", "v3"].stream())
        def valueC1 = record.get("c1", 42)
        def valueColNotFound = record.get("notPresentColumn", 42)

        assert valueC1 == "v1"
        assert valueColNotFound == 42
    }

    @Test
    void "should return defaultValue if an idx is not present"() {
        def record = new Record(new Header(["c1", "c2", "c3"].stream()), ["v1", "v2", "v3"].stream())
        def valueC2 = record.get(1, 42)
        def valueColNotFound = record.get(3, 42)
        def valueColInvalidIdx = record.get(-1, 42)

        assert valueC2 == "v2"
        assert valueColNotFound == 42
        assert valueColInvalidIdx == 42
    }
}
