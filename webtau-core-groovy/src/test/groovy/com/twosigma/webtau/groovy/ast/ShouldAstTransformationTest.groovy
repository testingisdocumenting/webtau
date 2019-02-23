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

package com.twosigma.webtau.groovy.ast

import com.twosigma.webtau.expectation.ActualPath
import com.twosigma.webtau.expectation.ExpectationHandler
import com.twosigma.webtau.expectation.ExpectationHandlers
import com.twosigma.webtau.expectation.ValueMatcher
import com.twosigma.webtau.expectation.equality.EqualMatcher
import com.twosigma.webtau.expectation.equality.GreaterThanMatcher
import com.twosigma.webtau.expectation.equality.GreaterThanOrEqualMatcher
import com.twosigma.webtau.expectation.equality.LessThanMatcher
import com.twosigma.webtau.expectation.equality.LessThanOrEqualMatcher
import com.twosigma.webtau.expectation.equality.NotEqualMatcher

import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.throwException

class ShouldAstTransformationTest extends GroovyTestCase {
    void testShouldNotTransformationOnNull() {
        code {
            assertScript('2.shouldNot == 2')
        } should throwException('\nmismatches:\n' +
            '\n' +
            '[value]:   actual: 2 <java.lang.Integer>\n' +
            '         expected: not 2 <java.lang.Integer>')
    }

    void testShouldTransformationOnNull() {
        code {
            assertScript('3.should == null')
        } should throwException('\nmismatches:\n' +
            '\n' +
            '[value]:   actual: 3 <java.lang.Integer>\n' +
            '         expected: null')
    }

    void testShouldTransformationOnMap() {
        code {
            assertScript('[a:1, b:2].should == [a: 3]')
        } should throwException('\nmismatches:\n' +
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
        } should throwException('\nmismatches:\n' +
            '\n' +
            '[value]:   actual: 2 <java.lang.Integer>\n' +
            '         expected: not 2 <java.lang.Integer>')

    }

    void testOperationsOverload() {
        def failedMatchers = []

        def expectationHandler = { ValueMatcher valueMatcher, ActualPath actualPath, Object actualValue, String message ->
            failedMatchers.add(valueMatcher.getClass())

            return ExpectationHandler.Flow.Terminate
        }

        ExpectationHandlers.withAdditionalHandler(expectationHandler) {
            assertScript("3.should == 2")
            assertScript("3.should != 3")

            assertScript("3.shouldBe < 2")
            assertScript("3.shouldBe <= 2")
            assertScript("2.shouldBe > 3")
            assertScript("2.shouldBe >= 3")

            assertScript("3.shouldNotBe > 2")
            assertScript("3.shouldNotBe >= 2")
            assertScript("2.shouldNotBe < 3")
            assertScript("2.shouldNotBe <= 3")
        }

        failedMatchers.should == [
                EqualMatcher, NotEqualMatcher,
                LessThanMatcher, LessThanOrEqualMatcher, GreaterThanMatcher, GreaterThanOrEqualMatcher,
                GreaterThanMatcher, GreaterThanOrEqualMatcher, LessThanMatcher, LessThanOrEqualMatcher]
    }
}
