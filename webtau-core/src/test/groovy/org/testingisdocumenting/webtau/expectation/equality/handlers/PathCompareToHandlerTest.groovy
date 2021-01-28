/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.equality.handlers

import org.junit.Test

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.*

class PathCompareToHandlerTest {
    @Test
    void "handles equality with path on the left"() {
        def handler = new PathCompareToHandler()
        assert handler.handleEquality(Paths.get("abc"), Paths.get("abc"))
        assert handler.handleEquality(Paths.get("abc"), "test")
        assert handler.handleEquality(Paths.get("abc"), 100)

        assert !handler.handleEquality("abc", 100)
        assert !handler.handleEquality(200, 100)
    }

    @Test
    void "handles compareTo with path on the left"() {
        def handler = new PathCompareToHandler()
        assert handler.handleGreaterLessEqual(Paths.get("abc"), Paths.get("abc"))
        assert handler.handleGreaterLessEqual(Paths.get("abc"), "test")
        assert handler.handleGreaterLessEqual(Paths.get("abc"), 100)

        assert !handler.handleGreaterLessEqual("abc", 100)
        assert !handler.handleGreaterLessEqual(200, 100)
    }

    @Test
    void "compares two paths are equal"() {
        actual(Paths.get("abc")).should(equal(Paths.get("abc")))
        actual(Paths.get("abc")).shouldNot(equal(Paths.get("cde")))
    }

    @Test
    void "compares path and string are equal"() {
        actual(Paths.get("abc")).should(equal("abc"))
        actual(Paths.get("abc")).shouldNot(equal("cde"))
    }

    @Test
    void "compares two path with greater"() {
        actual(Paths.get("zabc")).shouldBe(greaterThan(Paths.get("abc")))
    }

    @Test
    void "compares path and string with greater"() {
        actual(Paths.get("zabc")).shouldBe(greaterThan("abc"))
    }
}
