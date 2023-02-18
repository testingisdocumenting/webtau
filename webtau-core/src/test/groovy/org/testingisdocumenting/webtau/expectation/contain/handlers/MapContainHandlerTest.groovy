/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.contain.handlers

import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.*

class MapContainHandlerTest {
    @Test
    void "should pass when matched"() {
        def actualMap = [hello: 1, world: 2]
        def expectedMap = [hello: 1, world: 2]

        actual(actualMap).should(contain(expectedMap))
    }

    @Test
    void "should ignore extra actual properties"() {
        def actualMap = [hello: 1, world: 2]
        def expectedMap = [hello: 1]

        actual(actualMap).should(contain(expectedMap))
    }

    @Test
    void "should detect missing keys"() {
        def actualMap = [hello: 1, world: 2]
        def expectedMap = [hello: 1, world: 3, test: 5]

        code {
            actual(actualMap).should(contain(expectedMap))
        } should throwException("no match found")
    }

    @Test
    void "should pass when not contain"() {
        def actualMap = [hello: 1, world: 2]
        def expectedMap = [three: 3]

        actual(actualMap).shouldNot(contain(expectedMap))
    }

    @Test
    void "should pass when key is present but value is not matched"() {
        def actualMap = [hello: 1, world: 2]
        def expectedMap = [world: 3]

        actual(actualMap).shouldNot(contain(expectedMap))
    }

    @Test
    void "should fail when key is present and value is matched"() {
        def actualMap = [hello: 1, world: 2]
        def expectedMap = [world: 2]

        code {
            actual(actualMap).shouldNot(contain(expectedMap))
        } should throwException("match is found")
    }
}
