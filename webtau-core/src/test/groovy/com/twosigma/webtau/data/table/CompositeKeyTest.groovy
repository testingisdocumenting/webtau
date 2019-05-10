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

package com.twosigma.webtau.data.table

import com.twosigma.webtau.data.table.header.CompositeKey
import org.junit.Test

class CompositeKeyTest {
    @Test
    void "keys are equal when all the parts are equal"() {
        def a = new CompositeKey([10, 11].stream())
        def b = new CompositeKey([10, 11].stream())
        def c = new CompositeKey([10, 12].stream())
        def d = new CompositeKey([7, 11].stream())

        assert a.equals(b)
        assert !b.equals(c)
        assert !b.equals(d)
    }

    @Test
    void "when keys are equal hash code is equal"() {
        def a = new CompositeKey([10, 11].stream())
        def b = new CompositeKey([10, 11].stream())

        assert a.hashCode().equals(b.hashCode())
    }
}
