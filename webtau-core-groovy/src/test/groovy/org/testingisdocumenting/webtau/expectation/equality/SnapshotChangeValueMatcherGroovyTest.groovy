/*
 * Copyright 2024 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.equality

import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.*

class SnapshotChangeValueMatcherGroovyTest {
    @Test
    void "should change"() {
        def value = new SnapshotAwareDummyValue()
        value.takeSnapshot()

        value.doOperation()
        // value-should-change
        value.should change
        // value-should-change
    }

    @Test
    void "wait change"() {
        def value = new SnapshotAwareDummyValue()
        value.takeSnapshot()
        value.enableAutoIncrement()

        // value-wait-to-change
        value.waitTo change
        // value-wait-to-change
    }
}
