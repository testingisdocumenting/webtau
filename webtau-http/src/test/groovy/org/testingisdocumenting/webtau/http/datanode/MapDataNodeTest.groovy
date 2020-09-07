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

import static org.testingisdocumenting.webtau.http.datanode.DataNodeTestUtils.checkLevelIsRecordedForNonExistentNode

class MapDataNodeTest {
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

    @Test
    void "should access underlying value when provided path"() {
        def node = DataNodeBuilder.fromMap(new DataNodeId("body"), [
            key1: [name: 'name1'],
            key2: [name: 'name2'],
        ])

        String name = node.get('key1.name').get()
        name.should == 'name1'
    }

    @Test
    void "should access array element via path"() {
        def node = DataNodeBuilder.fromMap(new DataNodeId("body"), [
            key1: [[name: 'name1'], [name: 'name2']],
            root: [child: [[foo: 'boo'], [foo: 'bar']]]
        ])

        Map<String, String> firstElement = node.get('key1[0]').get()
        firstElement.should == [name: 'name1']

        String firstName = node.get('key1[0].name').get()
        firstName.should == 'name1'

        String lastName = node.get('key1[-1].name').get()
        lastName.should == 'name2'

        String nested = node.get('root.child[1].foo')
        nested.should == 'bar'
    }

    @Test
    void "should collect over list with path get"() {
        def node = DataNodeBuilder.fromMap(new DataNodeId("body"), [
            key1: [[name: 'name1'], [name: 'name2']]
        ])

        List<String> names = node.get("key1.name").get()
        names.should == ['name1', 'name2']
    }

    @Test
    void "should check full path in has"() {
        def node = DataNodeBuilder.fromMap(new DataNodeId("body"), [
            key1: 'name1',
            key2: [name: 'name2']
        ])

        boolean has = node.has('key1')
        has.should == true

        has = node.has('key2')
        has.should == true

        has = node.has('key2.name')
        has.should == true

        has = node.has('key2.foo')
        has.should == false

        has = node.has('key2.name.foo')
        has.should == false
    }

    @Test
    void "check level is recorded for non-existent nodes"() {
        def node = DataNodeBuilder.fromMap(new DataNodeId("body"), [:])
        checkLevelIsRecordedForNonExistentNode {
            node["key"]
        }

        checkLevelIsRecordedForNonExistentNode {
            node.get(0)
        }

        checkLevelIsRecordedForNonExistentNode {
            node["key"]["nested"]
        }

        checkLevelIsRecordedForNonExistentNode {
            node["key"]["nested"].get(1)
        }

        node["key"]["nested"].elements().traceableValue.checkLevel.should == [CheckLevel.None]
        node["key"]["nested"].elements().should == [null]
        node["key"]["nested"].elements().traceableValue.checkLevel.should == [CheckLevel.ExplicitPassed]

        node = DataNodeBuilder.fromMap(new DataNodeId("body"), [key: [:]])
        checkLevelIsRecordedForNonExistentNode {
            node["key"]["nested"]
        }
    }
}
