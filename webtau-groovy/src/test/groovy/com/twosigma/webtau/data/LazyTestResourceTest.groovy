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

package com.twosigma.webtau.data

import org.junit.Test

import static com.twosigma.webtau.data.LazyTestResource.createLazyResource

class LazyTestResourceTest {
    @Test
    void "wraps a provided bean with a delayed initialization"() {
        def sequence = []

        sequence << 1
        def data = new LazyTestResource("resource name", {
            sequence << 2
            return new LazyData(firstName: 'first-name', score: 100)
        })
        sequence << 3

        data.firstName.should == 'first-name'
        data.score.should == 100

        sequence.should == [1, 3, 2]
    }
}

class LazyData {
    String firstName
    Integer score
}
