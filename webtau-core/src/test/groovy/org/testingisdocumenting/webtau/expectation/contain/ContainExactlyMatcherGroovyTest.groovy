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

package org.testingisdocumenting.webtau.expectation.contain

import org.junit.Test
import org.testingisdocumenting.webtau.data.Person

import java.util.stream.IntStream

import static org.testingisdocumenting.webtau.WebTauCore.*

class ContainExactlyMatcherGroovyTest {
    @Test
    void matchRecordsAndMaps() {
        code {
            // possible-mismatches-example
            def list = [
                    new Person("id1", 3, 12),
                    new Person("id1", 4, 10),
                    new Person("id2", 4, 20)
            ]
            actual(list).should(containExactly(
                    [id: "id2", level: 4, monthsAtCompany: 20],
                    [id: "id1", level: 8, monthsAtCompany: 10],
                    [id: "id1", level: 7, monthsAtCompany: 12]))
            // possible-mismatches-example
        } should throwException(AssertionError)
    }
}
