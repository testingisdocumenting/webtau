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

package org.testingisdocumenting.webtau.expectation.equality.handlers

import org.testingisdocumenting.webtau.data.ValuePath
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator
import org.junit.Test
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.expectation.equality.CompareToComparator.AssertionMode.*
import static org.junit.Assert.assertEquals
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runAndValidateOutput
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runExpectExceptionAndValidateOutput

class SetCompareToHandlerTest {
    @Test
    void "handles sets"() {
        def handler = new SetCompareToHandler()
        assert handler.handleEquality([] as Set, [] as Set)
        assert !handler.handleEquality([], [] as Set)
    }

    @Test
    void "order should not matter"() {
        actual([1, 2] as Set).should(equal([2, 1] as Set))
    }

    @Test
    void "should report missing and extra elements in equal mode"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to equal [~/worl./, ~/.ello1/]:\n' +
                '    missing, but expected values:\n' +
                '    \n' +
                '    ~/.ello1/\n' +
                '    \n' +
                '    unexpected values:\n' +
                '    \n' +
                '    [value][0]: "hello" (Xms)\n' +
                '  \n' +
                '  [**"hello"**, "world"]') {
            actual(["hello", "world"] as Set).should(equal([~/worl./, ~/.ello1/] as Set))
        }
    }

    @Test
    void "should report matched elements in not equal mode"() {
        runExpectExceptionAndValidateOutput(AssertionError, 'X failed expecting [value] to not equal [~/worl./, ~/.ello/]:\n' +
                '    [value][1]:  actual: "world" <java.lang.String>\n' +
                '               expected: not ~/worl./ <java.util.regex.Pattern>\n' +
                '    [value][0]:  actual: "hello" <java.lang.String>\n' +
                '               expected: not ~/.ello/ <java.util.regex.Pattern> (Xms)\n' +
                '  \n' +
                '  [**"hello"**, **"world"**]') {
            actual(["hello", "world"] as Set).shouldNot(equal([~/worl./, ~/.ello/] as Set))
        }
    }

    @Test
    void "should not report missing elements in not equal mode"() {
        runAndValidateOutput('. [value] doesn\'t equal [~/.ello1/] (Xms)') {
            actual(["hello"] as Set).shouldNot(equal([~/.ello1/] as Set))
        }
    }
}
