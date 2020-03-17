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

package org.testingisdocumenting.webtau.groovy.ast

import org.testingisdocumenting.webtau.expectation.ActualPath
import org.testingisdocumenting.webtau.expectation.ExpectationHandler
import org.testingisdocumenting.webtau.expectation.ExpectationHandlers
import org.testingisdocumenting.webtau.expectation.ValueMatcher
import org.testingisdocumenting.webtau.expectation.equality.EqualMatcher
import org.testingisdocumenting.webtau.expectation.equality.GreaterThanMatcher
import org.testingisdocumenting.webtau.expectation.equality.GreaterThanOrEqualMatcher
import org.testingisdocumenting.webtau.expectation.equality.LessThanMatcher
import org.testingisdocumenting.webtau.expectation.equality.LessThanOrEqualMatcher
import org.testingisdocumenting.webtau.expectation.equality.NotEqualMatcher

import static org.testingisdocumenting.webtau.WebTauCore.*

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

        def expectationHandler = new ExpectationHandler() {
            @Override
            ExpectationHandler.Flow onValueMismatch(ValueMatcher valueMatcher, ActualPath actualPath, Object actualValue, String message) {
                failedMatchers.add(valueMatcher.getClass())
                return ExpectationHandler.Flow.Terminate
            }
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
