/*
 * Copyright 2023 webtau maintainers
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

import org.testingisdocumenting.webtau.data.ValuePath
import org.testingisdocumenting.webtau.expectation.ExpectationHandler
import org.testingisdocumenting.webtau.expectation.ExpectationHandlers
import org.testingisdocumenting.webtau.expectation.ValueMatcher
import org.testingisdocumenting.webtau.expectation.equality.EqualMatcher
import org.testingisdocumenting.webtau.expectation.equality.GreaterThanMatcher
import org.testingisdocumenting.webtau.expectation.equality.GreaterThanOrEqualMatcher
import org.testingisdocumenting.webtau.expectation.equality.LessThanMatcher
import org.testingisdocumenting.webtau.expectation.equality.LessThanOrEqualMatcher
import org.testingisdocumenting.webtau.expectation.equality.NotEqualMatcher
import org.testingisdocumenting.webtau.reporter.TokenizedMessage

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runAndValidateOutput

class ShouldAstTransformationTest extends GroovyTestCase {
    void testShouldNotTransformation() {
        runAndValidateOutput("X failed expecting [value] to not equal 2: \n" +
                "    mismatches:\n" +
                "    \n" +
                "    [value]:   actual: 2 <java.lang.Integer>\n" +
                "             expected: not 2 <java.lang.Integer> (Xms)") {
            assertScript('2.shouldNot == 2')
        }
    }

    void testShouldTransformationOnNull() {
        code {
            assertScript('3.should == null')
        } should throwException(AssertionError)
    }

    void testShouldTransformationOnMap() {
        runAndValidateOutput('X failed expecting [value] to equal {\n' +
                '                                      "a": 3\n' +
                '                                    }: \n' +
                '    mismatches:\n' +
                '    \n' +
                '    [value].a:   actual: 1 <java.lang.Integer>\n' +
                '               expected: 3 <java.lang.Integer>\n' +
                '    \n' +
                '    unexpected values:\n' +
                '    \n' +
                '    [value].b: 2 (Xms)\n' +
                '  \n' +
                '  {\n' +
                '    "a": **1**,\n' +
                '    "b": 2\n' +
                '  }') {
            assertScript('[a:1, b:2].should == [a: 3]')
        }
    }

    void testInsideClosure() {
        code {
            assertScript("""
            def code = { ->
                2.shouldNot == 2
            }
            
            code() """)
        } should throwException(AssertionError)
    }

    void testOperationsOverload() {
        def failedMatchers = []

        def expectationHandler = new ExpectationHandler() {
            @Override
            ExpectationHandler.Flow onValueMismatch(ValueMatcher valueMatcher, ValuePath actualPath, Object actualValue, TokenizedMessage message) {
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
