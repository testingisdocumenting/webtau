/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.http.datanode

import com.twosigma.webtau.data.traceable.CheckLevel
import com.twosigma.webtau.data.traceable.TraceableValue
import org.junit.Test

import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.equal
import static com.twosigma.webtau.Ddjt.throwException

class StructuredDataNodeTest {
    @Test
    void "value should be marked as explicitly failed when it mismatches"() {
        def node = new StructuredDataNode(new DataNodeId("value"), new TraceableValue(10))

        code {
            node.should(equal(8))
        } should throwException(~/mismatches/)

        node.getTraceableValue().checkLevel.should == CheckLevel.ExplicitFailed
    }

    @Test
    void "value should be marked as fuzzy passed when it mismatches and should mismatch"() {
        def node = new StructuredDataNode(new DataNodeId("value"), new TraceableValue(10))

        node.shouldNot(equal(8))
        node.getTraceableValue().checkLevel.should == CheckLevel.FuzzyPassed
    }

    @Test
    void "value should be marked as explicitly passed when it matches"() {
        def node = new StructuredDataNode(new DataNodeId("value"), new TraceableValue(10))

        node.should(equal(10))
        node.getTraceableValue().checkLevel.should == CheckLevel.ExplicitPassed
    }

    @Test
    void "value should be marked as explicitly failed when it matches"() {
        def node = new StructuredDataNode(new DataNodeId("value"), new TraceableValue(10))

        code {
            node.shouldNot(equal(10))
        } should throwException(~/equals 10/)

        node.getTraceableValue().checkLevel.should == CheckLevel.ExplicitFailed
    }

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
    void "should access underlying value in case of simple"() {
        def node = DataNodeBuilder.fromMap(new DataNodeId("body"), [
                key1: [name: 'name1'],
                key2: [name: 'name2'],
        ])

        String name = node.get('key1').get('name').get()
        name.should == 'name1'
    }

    @Test
    void "should extract map value in case of object"() {
        def node = DataNodeBuilder.fromMap(new DataNodeId("body"), [
                key1: [name: 'name1'],
                key2: [name: 'name2'],
        ])

        Map<String, Object> map = node.get('key1').get()
        map.should == [name: 'name1']
    }

    @Test
    void "should extract map value in case of array"() {
        def node = DataNodeBuilder.fromMap(new DataNodeId("body"), [
                key1: [[name: 'name1'], [name: 'name2']]
        ])

        List<Map<String, Object>> list = node.get('key1').get()
        list.should == [[name: 'name1'], [name: 'name2']]
    }
}
