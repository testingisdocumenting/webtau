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

package org.testingisdocumenting.webtau.expectation.equality.handlers

import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator
import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.*
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
                '      expected: "b" <java.lang.String>\n' +
                '                 ^',
                comparator.generateEqualMismatchReport())
    }

    @Test
    void "shows caret indicator for first mismatch of a one line strings"() {
        def comparator = CompareToComparator.comparator()
        comparator.compareIsEqual(createActualPath('text'), 'hello world', 'herlo world')

        assertEquals('mismatches:\n' +
                '\n' +
                'text:   actual: "hello world" <java.lang.String>\n' +
                '      expected: "herlo world" <java.lang.String>\n' +
                '                   ^',
                comparator.generateEqualMismatchReport())
    }

    @Test
    void "shows caret indicator for first match of a one line strings"() {
        def comparator = CompareToComparator.comparator()
        comparator.compareIsNotEqual(createActualPath('text'), 'hello world', 'herlo world')

        assertEquals('mismatches:\n' +
                '\n' +
                'text:   actual:     "hello world" <java.lang.String>\n' +
                '      expected: not "herlo world" <java.lang.String>\n' +
                '                       ^',
                comparator.generateEqualMismatchReport())
    }

    @Test
    void "renders multiline strings in blocks"() {
        def comparator = CompareToComparator.comparator()
        comparator.compareIsEqual(createActualPath('text'),
                'hello world world\nhello again',
                'hello world world\nhi again')

        assertEquals('mismatches:\n' +
                '\n' +
                'text:   actual: <java.lang.String>\n' +
                '      _________________\n' +
                '      hello world world\n' +
                '      hello again\n' +
                '      _________________\n' +
                '      \n' +
                '      expected: <java.lang.String>\n' +
                '      _________________\n' +
                '      hello world world\n' +
                '      hi again\n' +
                '      _________________\n' +
                '      \n' +
                '      first mismatch at line idx 1:\n' +
                '      hello again\n' +
                '      hi again\n' +
                '       ^',
                comparator.generateEqualMismatchReport())
    }

    @Test
    void "properly handles new line symbol at the end of a single line"() {
        def comparator = CompareToComparator.comparator()
        comparator.compareIsEqual(createActualPath('text'),
                'single line\n',
                'single lone\n')

        assertEquals('mismatches:\n' +
                '\n' +
                'text:   actual: <java.lang.String>\n' +
                '      ___________\n' +
                '      single line\n' +
                '      \n' +
                '      ___________\n' +
                '      \n' +
                '      expected: <java.lang.String>\n' +
                '      ___________\n' +
                '      single lone\n' +
                '      \n' +
                '      ___________\n' +
                '      \n' +
                '      first mismatch at line idx 0:\n' +
                '      single line\n' +
                '      single lone\n' +
                '              ^', comparator.generateEqualMismatchReport())
    }

    @Test
    void "properly handles multiple new line symbols at the end of a single line"() {
        def comparator = CompareToComparator.comparator()
        comparator.compareIsEqual(createActualPath('text'),
                'single line\n\n\n',
                'single lone\n\n\n')

        assertEquals('mismatches:\n' +
                '\n' +
                'text:   actual: <java.lang.String>\n' +
                '      ___________\n' +
                '      single line\n' +
                '      \n' +
                '      \n' +
                '      \n' +
                '      ___________\n' +
                '      \n' +
                '      expected: <java.lang.String>\n' +
                '      ___________\n' +
                '      single lone\n' +
                '      \n' +
                '      \n' +
                '      \n' +
                '      ___________\n' +
                '      \n' +
                '      first mismatch at line idx 0:\n' +
                '      single line\n' +
                '      single lone\n' +
                '              ^', comparator.generateEqualMismatchReport())
    }

    @Test
    void "reports different number of lines"() {
        def comparator = CompareToComparator.comparator()
        comparator.compareIsEqual(createActualPath('text'),
                '\nhello world world\nhello again',
                'hello world world\nhi again')

        assertEquals('mismatches:\n' +
                '\n' +
                'text: different number of lines, expected: 2, actual: 3\n' +
                '        actual: <java.lang.String>\n' +
                '      _________________\n' +
                '      \n' +
                '      hello world world\n' +
                '      hello again\n' +
                '      _________________\n' +
                '      \n' +
                '      expected: <java.lang.String>\n' +
                '      _________________\n' +
                '      hello world world\n' +
                '      hi again\n' +
                '      _________________\n' +
                '      \n' +
                '      first mismatch at line idx 0:\n' +
                '      \n' +
                '      hello world world\n' +
                '      ^',
                comparator.generateEqualMismatchReport())
    }

    @Test
    void "reports different line ending"() {
        def comparator = CompareToComparator.comparator()
        comparator.compareIsEqual(createActualPath('text'),
                'hello world world\n\rhello again',
                'hello world world\nhello again')

        assertEquals('mismatches:\n' +
                '\n' +
                'text: different line endings, expected doesn\'t contain \\r, but actual does',
                comparator.generateEqualMismatchReport())
    }
}
