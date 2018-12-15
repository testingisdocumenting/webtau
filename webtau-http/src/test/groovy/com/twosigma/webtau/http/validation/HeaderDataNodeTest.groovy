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

import com.twosigma.webtau.http.HttpResponse
import com.twosigma.webtau.http.datanode.NullDataNode
import org.junit.Before
import org.junit.Test

class HeaderDataNodeTest {
    def headerNode

    @Before
    void setUp() {
        def response = new HttpResponse()
        response.addHeader('customValue', 'value')
        response.addHeader('Link', 'url')

        headerNode = new HeaderDataNode(response)
    }

    @Test
    void "access to children value should be case insensitive"() {
        headerNode.get('link').should == 'url'
        headerNode.get('Link').should == 'url'
        headerNode.has('link').should == true
        headerNode.has('Link').should == true

        headerNode.get('CustomValue').should == 'value'
        headerNode.get('customvalue').should == 'value'
        headerNode.has('CustomValue').should == true
        headerNode.has('customvalue').should == true
    }

    @Test
    void "special shortcuts are generated in a case insensitive manner"() {
        def response = new HttpResponse()
        response.addHeader('content-location', 'foo')
        response.addHeader('content-length', '10')
        def node = new HeaderDataNode(response)

        node.contentLocation.should == 'foo'
        node.contentLength.should == 10
        node.contentLength.get().getClass().should == Integer
    }

    @Test
    void "non existing header node should return null data node"() {
        def nonExisting = headerNode.get('NonExisting')
        nonExisting.getClass().should == NullDataNode
        nonExisting.id().path.should == 'header.NonExisting'
        nonExisting.id().name.should == 'NonExisting'
    }
}
