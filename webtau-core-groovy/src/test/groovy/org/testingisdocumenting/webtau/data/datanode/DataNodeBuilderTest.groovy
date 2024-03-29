/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.data.datanode

import org.junit.Test

class DataNodeBuilderTest {
    @Test
    void "should convert a map to data node"() {
        def n = DataNodeBuilder.fromMap(new DataNodeId("body"), [childA: "valueA", childB: "valueB"])

        n.numberOfChildren().should == 2
        n.numberOfElements().should == 0

        assertSingleValue(n.get("childA"), "valueA")
        assertSingleValue(n.get("childB"), "valueB")
    }

    @Test
    void "should convert a list to data node"() {
        def n = DataNodeBuilder.fromList(new DataNodeId("body"), ["valueA", "valueB"])

        n.numberOfChildren().should == 0
        n.numberOfElements().should == 2

        assertSingleValue(n.get(0), "valueA")
        assertSingleValue(n.get(1), "valueB")
    }

    @Test
    void "should convert list of maps to data node"() {
        def n = DataNodeBuilder.fromList(new DataNodeId("body"), [[k11: "v11", k12: "v12"], [k21: "v21", k22: "v22"]])

        n.numberOfChildren().should == 0
        n.numberOfElements().should == 2

        assertSingleValue(n.get(0).get("k11"), "v11")
        assertSingleValue(n.get(0).get("k12"), "v12")
        assertSingleValue(n.get(1).get("k21"), "v21")
        assertSingleValue(n.get(1).get("k22"), "v22")
    }

    private static void assertSingleValue(DataNode actual, expected) {
        actual.numberOfChildren().should == 0
        actual.numberOfElements().should == 0
        actual.getTraceableValue().value.should == expected
    }
}
