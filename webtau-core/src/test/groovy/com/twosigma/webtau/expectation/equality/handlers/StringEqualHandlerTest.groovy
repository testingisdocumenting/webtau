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

package com.twosigma.webtau.expectation.equality.handlers

import com.twosigma.webtau.expectation.equality.EqualComparator
import org.junit.Test

import static com.twosigma.webtau.Ddjt.*
import static org.junit.Assert.assertEquals

class StringEqualHandlerTest {
    @Test
    void "handles instances of String and Characters as actual or expected"() {
        def handler = new StringEqualHandler()

        assert handler.handle("test", "test")
        assert handler.handle("t", Character.valueOf('t'.toCharArray()[0]))
        assert handler.handle(Character.valueOf('t'.toCharArray()[0]), "t")

        assert ! handler.handle("test", 100)
        assert ! handler.handle("test", ~/regexp/)
    }

    @Test
    void "compares string and character"() {
        actual("t").should(equal(Character.valueOf('t'.toCharArray()[0])))
    }

    @Test
    void "reports types before conversion to string in case of failure"() {
        def comparator = EqualComparator.comparator()
        comparator.compare(createActualPath("text"), Character.valueOf('t'.toCharArray()[0]), "b")

        assertEquals("mismatches:\n" +
                "\n" +
                "text:   actual: t <java.lang.String>(before conversion: t <java.lang.Character>)\n" +
                "      expected: b <java.lang.String>",
                comparator.generateMismatchReport())

    }
}
