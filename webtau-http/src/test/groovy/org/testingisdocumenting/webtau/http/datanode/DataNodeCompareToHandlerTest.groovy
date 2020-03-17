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

package org.testingisdocumenting.webtau.http.datanode

import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator
import org.junit.Test

import static org.testingisdocumenting.webtau.data.traceable.CheckLevel.ExplicitPassed
import static org.testingisdocumenting.webtau.data.traceable.CheckLevel.None
import static org.junit.Assert.assertEquals

class DataNodeCompareToHandlerTest {
    @Test
    void "should only check explicitly specified properties when compared against map"() {
        def node = DataNodeBuilder.fromMap(new DataNodeId('node'), [
            a: [a1: 'va1', a2: [a21: 'va21', a22: 'v22']], b: 2, c: 3])
        node.should == [a: [a1: 'va1', a2: [a21: 'va21']], c:3]

        node.get('a').get('a1').getTraceableValue().checkLevel.should == ExplicitPassed
        node.get('a').get('a2').get('a21').getTraceableValue().checkLevel.should == ExplicitPassed
        node.get('a').get('a2').get('a22').getTraceableValue().checkLevel.should == None
        node.get('b').getTraceableValue().checkLevel.should == None
        node.get('c').getTraceableValue().checkLevel.should == ExplicitPassed
    }

    @Test
    void "should handle comparison against list"() {
        def node = DataNodeBuilder.fromList(new DataNodeId('node'), [1, 2, 3])
        node.should == [1, 2, 3]

        node.get(0).getTraceableValue().checkLevel.should == ExplicitPassed
        node.get(1).getTraceableValue().checkLevel.should == ExplicitPassed
        node.get(2).getTraceableValue().checkLevel.should == ExplicitPassed
    }

    @Test
    void "should handle comparison against table data"() {
        def node = DataNodeBuilder.fromList(new DataNodeId('node'), [
            [a: 1, b: 2, c: 3],
            [a: 4, b: 5, c: 6],
            [a: 7, b: 8, c: 9]])

        node.should == ['a' | 'b' ] {
                       ___________
                         1  | 2
                         4  | 5
                         7  | 8 }

        node.get(0).get('a').getTraceableValue().checkLevel.should == ExplicitPassed
        node.get(0).get('b').getTraceableValue().checkLevel.should == ExplicitPassed
        node.get(0).get('c').getTraceableValue().checkLevel.should == None

        node.get(1).get('a').getTraceableValue().checkLevel.should == ExplicitPassed
        node.get(1).get('b').getTraceableValue().checkLevel.should == ExplicitPassed
        node.get(1).get('c').getTraceableValue().checkLevel.should == None

        node.get(2).get('a').getTraceableValue().checkLevel.should == ExplicitPassed
        node.get(2).get('b').getTraceableValue().checkLevel.should == ExplicitPassed
        node.get(2).get('c').getTraceableValue().checkLevel.should == None
    }

    @Test
    void "should handle not-null comparison between structured node and null"() {
        def comparator = CompareToComparator.comparator()
        def node = DataNodeBuilder.fromMap(new DataNodeId('node'), [node: [k1: 'v1', k2: 'v2']])

        assert comparator.compareIsNotEqual(node.actualPath(), node, null)
        assertEquals('matches:\n' +
            '\n' +
            'node:   actual: {node={k1: v1, k2: v2}} <java.util.Collections.UnmodifiableMap>\n' +
            '      expected: not null', comparator.generateNotEqualMatchReport())
    }
}
