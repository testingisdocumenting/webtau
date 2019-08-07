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

package com.twosigma.webtau.http

import com.twosigma.webtau.data.traceable.CheckLevel
import com.twosigma.webtau.http.datanode.DataNodeBuilder
import com.twosigma.webtau.http.datanode.DataNodeId
import com.twosigma.webtau.http.datanode.GroovyDataNode
import org.junit.Test

class GroovyDataNodeTest {
    @Test
    void "compare to single numeric values"() {
        def node = new GroovyDataNode(DataNodeBuilder.fromValue(new DataNodeId('test'), 30))
        def messages = []

        if (node > 10) {
            messages << "node > 10"
        }

        if (node < 40) {
            messages << "node < 40"
        }

        if (node == 30) {
            messages << "node == 30"
        }

        if (node >= 30) {
            messages << "node >= 30"
        }

        if (node <= 30) {
            messages << "node <= 30"
        }

        if (node != 33) {
            messages << "node != 33"
        }

        messages.should == [
                "node > 10",
                "node < 40",
                "node == 30",
                "node >= 30",
                "node <= 30",
                "node != 33"
        ]
    }

    @Test
    void "compare to single string values"() {
        def node = new GroovyDataNode(DataNodeBuilder.fromValue(new DataNodeId('test'), 'hello'))
        def messages = []

        if (node == 'hello') {
            messages << "node == 'hello'"
        }

        if (node != 'hello2') {
            messages << "node != 'hello2'"
        }

        messages.should == [
                "node == 'hello'",
                "node != 'hello2'"
        ]
    }

    @Test
    void "data node check level is marked as fuzzy passed if no stronger marking is available"() {
        def node = new GroovyDataNode(DataNodeBuilder.fromValue(new DataNodeId('test'), 'hello'))

        if (node != 'hello') {
            println "not equal"
        }

        node.getTraceableValue().checkLevel.should == CheckLevel.FuzzyPassed

        node.getTraceableValue().updateCheckLevel(CheckLevel.ExplicitFailed)
        if (node != 'hello') {
            println "not equal"
        }

        node.getTraceableValue().checkLevel.should == CheckLevel.ExplicitFailed
    }

    @Test
    void "compare to complex values"() {
        def node = new GroovyDataNode(DataNodeBuilder.fromValue(new DataNodeId('test'), [1, 2, 3]))

        if (node == [1, 2, 3]) {
            // TODO
            // https://issues.apache.org/jira/browse/GROOVY-7954
            // this case is still not covered: left side is Comparable and its implementation would return 0 in this case
            // but the compareTo code is not being triggered
            throw new UnsupportedOperationException("once this is fixed in groovy we should update examples and docs")
        }
    }
}
