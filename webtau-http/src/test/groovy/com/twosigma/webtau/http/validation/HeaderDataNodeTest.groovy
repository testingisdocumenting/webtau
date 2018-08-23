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

package com.twosigma.webtau.http.validation

import com.twosigma.webtau.http.datanode.DataNodeBuilder
import com.twosigma.webtau.http.datanode.DataNodeId
import org.junit.Test

class HeaderDataNodeTest {
    @Test
    void "access to children value should be case insensitive"() {
        def node = DataNodeBuilder.fromMap(new DataNodeId('header'), [
                customValue: 'value',
                Link: 'url'
        ])

        def headerNode = new HeaderDataNode(node)

        headerNode.get('link').should == 'url'
        headerNode.get('Link').should == 'url'
        headerNode.has('link').should == true
        headerNode.has('Link').should == true

        headerNode.get('CustomValue').should == 'value'
        headerNode.get('customvalue').should == 'value'
        headerNode.has('CustomValue').should == true
        headerNode.has('customvalue').should == true
    }
}
