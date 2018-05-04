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

package com.twosigma.webtau.data.table

import org.junit.Test

class CompositeKeyTest {
    @Test
    void "should equal when all the parts are equal"() {
        def key1 = new CompositeKey(['a', 'b'].stream())
        def key2 = new CompositeKey(['a', 'b'].stream())

        assert key1 == key2
        assert key1.hashCode() == key2.hashCode()
    }

    @Test
    void "hash code should be calculated for all parts of the key"() {
        assert new CompositeKey([10].stream()).hashCode() != new CompositeKey([10, 20].stream()).hashCode()
    }

    @Test
    void "order of parts should matter"() {
        def key1 = new CompositeKey([10, 20].stream())
        def key2 = new CompositeKey([20, 10].stream())

        assert key1 != key2
        assert key1.hashCode() != key2.hashCode()
    }
}
