/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.data

import org.junit.Test

class LazyTestResourceTest {
    @Test
    void "wraps a provided bean with a lazy initialization"() {
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

    @Test
    void "intercepts method calls and initialize resource on first access"() {
        def sequence = []

        sequence << 1
        def data = new LazyTestResource("resource name", {
            sequence << 2
            return "test"
        })
        sequence << 3

        data.size().should == 4
        data.toString().should == "test"

        sequence.should == [1, 3, 2]
    }

    @Test
    void "provides access to the underlying value"() {
        def data = new LazyTestResource("resource name", {
            return new LazyData(firstName: 'first-name', score: 100)
        })

        data.get().class.should == LazyData
        data.get().firstName.should == 'first-name'
    }
}

class LazyData {
    String firstName
    Integer score
}
