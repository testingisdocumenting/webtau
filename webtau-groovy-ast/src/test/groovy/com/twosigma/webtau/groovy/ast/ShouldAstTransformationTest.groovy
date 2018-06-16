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

package com.twosigma.webtau.groovy.ast

import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.throwException

class ShouldAstTransformationTest extends GroovyTestCase {
    void testShouldNotTransformationOnNull() {
        code {
            assertScript('2.shouldNot == 2')
        } should throwException('\nequals 2, but shouldn\'t\n' +
            'mismatches:\n' +
            '\n' +
            '[value]:   actual: 2 <java.lang.Integer>\n' +
            '         expected: 2 <java.lang.Integer>')
    }

    void testShouldTransformationOnNull() {
        code {
            assertScript('3.should == null')
        } should throwException('\ndoesn\'t equal [null]\n' +
            'mismatches:\n' +
            '\n' +
            '[value]:   actual: 3 <java.lang.Integer>\n' +
            '         expected: null')
    }

    void testShouldNotTransformationOnMap() {
        code {
            assertScript('[a:1, b:2].should == [a: 3]')
        } should throwException('\ndoesn\'t equal {a=3}\n' +
            'mismatches:\n' +
            '\n' +
            '[value].a:   actual: 1 <java.lang.Integer>\n' +
            '           expected: 3 <java.lang.Integer>\n' +
            '\n' +
            'unexpected values:\n' +
            '\n' +
            '[value].b: 2')
    }

    void testInsideClosure() {
        code {
            assertScript("""
            def code = { ->
                2.shouldNot == 2
            }
            
            code() """)
        } should throwException('\nequals 2, but shouldn\'t\n' +
            'mismatches:\n' +
            '\n' +
            '[value]:   actual: 2 <java.lang.Integer>\n' +
            '         expected: 2 <java.lang.Integer>')

    }
}
