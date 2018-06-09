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

import org.junit.Test

import static com.twosigma.webtau.data.traceable.CheckLevel.ExplicitPassed
import static com.twosigma.webtau.data.traceable.CheckLevel.None

class DataNodeCompareToHandlerTest {
    @Test
    void "should only check explicitly specified properties when compared against map"() {
        def node = DataNodeBuilder.fromMap(new DataNodeId("node"), [
            a: [a1: 'va1', a2: [a21: 'va21', a22: 'v22']], b: 2, c: 3])
        node.should == [a: [a1: 'va1', a2: [a21: 'va21']], c:3]

        node.get('a').get('a1').get().checkLevel.should == ExplicitPassed
        node.get('a').get('a2').get('a21').get().checkLevel.should == ExplicitPassed
        node.get('a').get('a2').get('a22').get().checkLevel.should == None
        node.get('b').get().checkLevel.should == None
        node.get('c').get().checkLevel.should == ExplicitPassed
    }

    @Test
    void "should handle comparison against list"() {
        def node = DataNodeBuilder.fromList(new DataNodeId("node"), [1, 2, 3])
        node.should == [1, 2, 3]

        node.get(0).get().checkLevel.should == ExplicitPassed
        node.get(1).get().checkLevel.should == ExplicitPassed
        node.get(2).get().checkLevel.should == ExplicitPassed
    }

    @Test
    void "should handle comparison against table data"() {
        def node = DataNodeBuilder.fromList(new DataNodeId("node"), [
            [a: 1, b: 2, c: 3],
            [a: 4, b: 5, c: 6],
            [a: 7, b: 8, c: 9]])

        node.should == ['a' | 'b' ] {
                       ___________
                         1  | 2
                         4  | 5
                         7  | 8 }

        node.get(0).get('a').get().checkLevel.should == ExplicitPassed
        node.get(0).get('b').get().checkLevel.should == ExplicitPassed
        node.get(0).get('c').get().checkLevel.should == None

        node.get(1).get('a').get().checkLevel.should == ExplicitPassed
        node.get(1).get('b').get().checkLevel.should == ExplicitPassed
        node.get(1).get('c').get().checkLevel.should == None

        node.get(2).get('a').get().checkLevel.should == ExplicitPassed
        node.get(2).get('b').get().checkLevel.should == ExplicitPassed
        node.get(2).get('c').get().checkLevel.should == None
    }
}
