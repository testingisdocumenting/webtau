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

class ValueExtractorByPathTest {
    @Test
    void "extract from map of maps"() {
        def value = ValueExtractorByPath.extractFromMapOrList([parent: [child: 100]], "parent.child")
        value.should == 100
    }

    @Test
    void "extract from map of lists of maps"() {
        def value = ValueExtractorByPath.extractFromMapOrList([parent: [[child: 100], [another: 200]]], "parent[1].another")
        value.should == 200
    }

    @Test
    void "extract from map of lists of maps negative index"() {
        def value = ValueExtractorByPath.extractFromMapOrList([parent: [[child: 100], [another: 200], [another: 500]]], "parent[-1].another")
        value.should == 500
    }

    @Test
    void "extract non existing path"() {
        ValueExtractorByPath.extractFromMapOrList([parent: [[child: 100], [another: 200]]], "par[1].another")
                .should == null

        ValueExtractorByPath.extractFromMapOrList([parent: [[child: 100], [another: 200]]], "parent[1].another.value")
                .should == null
    }
}
