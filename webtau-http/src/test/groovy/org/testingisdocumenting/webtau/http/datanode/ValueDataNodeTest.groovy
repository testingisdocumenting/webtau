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

package org.testingisdocumenting.webtau.http.datanode

import org.junit.Test
import org.testingisdocumenting.webtau.data.traceable.CheckLevel
import org.testingisdocumenting.webtau.data.traceable.TraceableValue

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.http.datanode.DataNodeTestUtils.checkLevelIsRecordedForNonExistentNode

class ValueDataNodeTest {
    @Test
    void "value should be marked as explicitly failed when it mismatches"() {
        def node = new ValueDataNode(new DataNodeId("value"), new TraceableValue(10))

        code {
            node.should(equal(8))
        } should throwException(~/mismatches/)

        node.getTraceableValue().checkLevel.should == CheckLevel.ExplicitFailed
    }

    @Test
    void "value should be marked as fuzzy passed when it mismatches and should mismatch"() {
        def node = new ValueDataNode(new DataNodeId("value"), new TraceableValue(10))

        node.shouldNot(equal(8))
        node.getTraceableValue().checkLevel.should == CheckLevel.FuzzyPassed
    }

    @Test
    void "value should be marked as explicitly passed when it matches"() {
        def node = new ValueDataNode(new DataNodeId("value"), new TraceableValue(10))

        node.should(equal(10))
        node.getTraceableValue().checkLevel.should == CheckLevel.ExplicitPassed
    }

    @Test
    void "value should be marked as explicitly failed when it matches"() {
        def node = new ValueDataNode(new DataNodeId("value"), new TraceableValue(10))

        code {
            node.shouldNot(equal(10))
        } should throwException(~/actual: 10/)

        node.getTraceableValue().checkLevel.should == CheckLevel.ExplicitFailed
    }

    @Test
    void "check level is recorded for non-existent nodes"() {
        def node = DataNodeBuilder.fromValue(new DataNodeId("body"), null)
        checkLevelIsRecordedForNonExistentNode {
            node
        }

        checkLevelIsRecordedForNonExistentNode {
            node["key"]
        }

        checkLevelIsRecordedForNonExistentNode {
            node.get(0)
        }
    }

    @Test
    void "comparing null to list should record correct check levels"() {
        def node = DataNodeBuilder.fromValue(new DataNodeId("body"), null)

        code {
            node.should == [1, [key: 'value']]
        } should throwException(~/mismatches/)

        node.get(0).traceableValue.checkLevel.should == CheckLevel.ExplicitFailed
        node.get(1).get('key').traceableValue.checkLevel.should == CheckLevel.ExplicitFailed
    }
}
