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

package org.testingisdocumenting.webtau.data;

import org.junit.Test;
import org.testingisdocumenting.webtau.data.table.TableData;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.testingisdocumenting.webtau.Matchers.*;
import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.data.Data.data;

public class DataJsonJavaTest {
    @Test
    public void readFlatList() {
        // read-json-list
        List<Map<String, ?>> list = data.json.list("data/flat-list.json");
        // read-json-list

        // validate-json-list
        actual(list.get(0).get("name")).should(equal("hello"));
        actual(list.get(1).get("payload")).should(
                equal(aMapOf("info", Pattern.compile("id2 payload"))));
        // validate-json-list
    }

    @Test
    public void readMap() {
        // read-json-map
        Map<String, ?> map = data.json.map("data/root-map.json");
        actual(map.get("payload")).should(equal(aMapOf("info", "additional id1 payload")));
        // read-json-map
    }

    @Test
    public void readTable() {
        // read-json-table
        TableData jsonTable = data.json.table("data/flat-list.json");
        TableData expected = table("id" , "name"  ,                                      "payload",
                                  ________________________________________________________________,
                                   "id1", "hello" , aMapOf("info", Pattern.compile("id1 payload")),
                                   "id2", "world" , aMapOf("info", Pattern.compile("id2 payload")));
        actual(jsonTable).should(equal(expected));
        // read-json-table
    }
}
