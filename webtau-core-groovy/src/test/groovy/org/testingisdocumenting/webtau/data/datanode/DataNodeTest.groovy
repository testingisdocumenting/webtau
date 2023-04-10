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

package org.testingisdocumenting.webtau.data.datanode

import org.junit.Test
import org.testingisdocumenting.webtau.data.datanode.DataNodeBuilder
import org.testingisdocumenting.webtau.data.datanode.DataNodeId
import org.testingisdocumenting.webtau.data.render.PrettyPrintableTestBase
import org.testingisdocumenting.webtau.data.traceable.CheckLevel

class DataNodeTest extends PrettyPrintableTestBase {
    @Test
    void "pretty print single value"() {
        def dataNode = DataNodeBuilder.fromValue(new DataNodeId("single"), 100)
        dataNode.prettyPrint(printer)

        expectOutput("100")
    }

    @Test
    void "pretty print list value"() {
        def dataNode = DataNodeBuilder.fromList(new DataNodeId("single"), [100, "hello", 200])
        dataNode.prettyPrint(printer)

        expectOutput('[100, "hello", 200]')
    }

    @Test
    void "pretty print object value"() {
        def dataNode = DataNodeBuilder.fromMap(new DataNodeId("single"), [key1: 100, key2: "hello"])
        dataNode.prettyPrint(printer)

        expectOutput('{"key1": 100, "key2": "hello"}')
    }

    @Test
    void "pretty print list of objects"() {
        def dataNode = DataNodeBuilder.fromList(new DataNodeId("single"), [
                [key1: 100, key2: "hello"],
                [key1: 100, key2: "hello"]])

        dataNode.prettyPrint(printer)

        expectOutput('[{"key1": 100, "key2": "hello"}, {"key1": 100, "key2": "hello"}]')
    }

    @Test
    void "pretty print list of objects with check levels"() {
        def dataNode = DataNodeBuilder.fromList(new DataNodeId("single"), [
                [key1: 100, key2: "hello"],
                [key1: 100, key2: null]])

        dataNode.get(0).get("key1").traceableValue.updateCheckLevel(CheckLevel.FuzzyPassed)
        dataNode.get(0).get("key2").traceableValue.updateCheckLevel(CheckLevel.FuzzyFailed)
        dataNode.get(1).get("key1").traceableValue.updateCheckLevel(CheckLevel.ExplicitPassed)

        dataNode.prettyPrint(printer)

        expectOutput('[{"key1": ~~100~~, "key2": **"hello"**}, {"key1": __100__, "key2": null}]')
    }
}
