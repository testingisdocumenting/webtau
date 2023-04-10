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

class DataNodeIdTest {
    @Test
    void "peer and child"() {
        def id = new DataNodeId("root")
        def peer = id.peer(3)

        peer.name.should == "root"
        peer.path.should == "root[3]"
        peer.normalizedPath.should == "root"

        def child = peer.child("value")
        child.path.should == "root[3].value"
        child.normalizedPath.should == "root.value"
    }

    @Test
    void "concat"() {
        def id = new DataNodeId("root").peer(3).child("value")

        def concat = id.concat("[4].nested")
        concat.name.should == "nested"
        concat.path.should == "root[3].value[4].nested"
        concat.normalizedPath.should == "root.value.nested"
    }

    @Test
    void "concat index only"() {
        def id = new DataNodeId("root").peer(3).child("value")

        def concat = id.concat("[4]")
        concat.name.should == "value"
        concat.path.should == "root[3].value[4]"
        concat.normalizedPath.should == "root.value"

    }
}
