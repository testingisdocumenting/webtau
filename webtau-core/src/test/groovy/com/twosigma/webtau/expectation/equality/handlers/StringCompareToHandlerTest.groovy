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

import com.twosigma.webtau.expectation.equality.CompareToComparator
import org.junit.Test

import static com.twosigma.webtau.Ddjt.actual
import static com.twosigma.webtau.Ddjt.createActualPath
import static com.twosigma.webtau.Ddjt.equal
import static org.junit.Assert.assertEquals

class StringCompareToHandlerTest {
    @Test
    void "handles instances of CharSequence and Characters as actual or expected"() {
        def handler = new StringCompareToHandler()
        def var = 100

        assert handler.handleEquality('test', 'test')
        assert handler.handleEquality('test', "test $var")
        assert handler.handleEquality("test $var", 'test')
        assert handler.handleEquality('t', Character.valueOf('t'.toCharArray()[0]))
        assert handler.handleEquality(Character.valueOf('t'.toCharArray()[0]), 't')

        assert ! handler.handleEquality('test', 100)
        assert ! handler.handleEquality('test', ~/regexp/)
    }

    @Test
    void "compares string and character"() {
        actual('t').should(equal(Character.valueOf('t'.toCharArray()[0])))
    }

    @Test
    void "compares string and gstring"() {
        def var = 100
        actual('test ' + var).should(equal("test $var"))
        actual("test $var").should(equal('test ' + var))
    }

    @Test
    void "reports types before conversion to string in case of failure"() {
        def comparator = CompareToComparator.comparator()
        comparator.compareIsEqual(createActualPath('text'), Character.valueOf('t'.toCharArray()[0]), 'b')

        assertEquals('mismatches:\n' +
                '\n' +
                'text:   actual: "t" <java.lang.String>(before conversion: t <java.lang.Character>)\n' +
                '      expected: "b" <java.lang.String>',
                comparator.generateEqualMismatchReport())
    }
}
