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

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.equal
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.webtau.http.datanode.DataNodeTestUtils.checkLevelIsRecordedForNonExistentNode

class ListDataNodeTest {
    @Test
    void "shortcut for children props"() {
        def node = DataNodeBuilder.fromList(new DataNodeId("body"), [
            [name: 'name1', score: 10],
            [name: 'name2', score: 20],
        ])

        node.name.should(equal(['name1', 'name2']))

        node.get(0).get('name').getTraceableValue().checkLevel.should == CheckLevel.ExplicitPassed
        node.get(1).get('name').getTraceableValue().checkLevel.should == CheckLevel.ExplicitPassed
        node.get(0).get('score').getTraceableValue().checkLevel.should == CheckLevel.None
        node.get(1).get('score').getTraceableValue().checkLevel.should == CheckLevel.None
    }

    @Test
    void "shortcut using on non existing field should produce null data node"() {
        def node = DataNodeBuilder.fromList(new DataNodeId("body"), [
            [name: 'name1'],
            [name: 'name2'],
        ])

        node.score.should == null

        code {
            node.score.shouldNot == null
        } should throwException(AssertionError, ~/body\.score/)
    }

    @Test
    void "should collect over list with path"() {
        def node = DataNodeBuilder.fromList(new DataNodeId("body"), [
            [key: [name: 'name1']],
            [key: [name: 'name2']]
        ])

        List<String> names = node.get("key.name").get()
        names.should == ['name1', 'name2']
    }

    @Test
    void "check level is recorded for non-existent nodes"() {
        def node = DataNodeBuilder.fromList(new DataNodeId("body"), [])

        checkLevelIsRecordedForNonExistentNode {
            node["key"]
        }

        checkLevelIsRecordedForNonExistentNode {
            node.get(0)
        }
    }
}
