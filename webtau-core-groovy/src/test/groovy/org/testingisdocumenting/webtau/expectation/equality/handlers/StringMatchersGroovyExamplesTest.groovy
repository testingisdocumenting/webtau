/*
 * Copyright 2023 webtau maintainers
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

import static org.testingisdocumenting.webtau.Matchers.*

class StringMatchersGroovyExamplesTest {
    @Test
    void singleLine() {
        code {
            // single-line-compare
            def output = "hallo world"
            output.should == "hello world"
            // single-line-compare
        } should throwException(AssertionError)
    }

    @Test
    void multiLine() {
        code {
            // multi-line-compare
            String output = buildOutput()
            output.should == "line one\nline 2"
            // multi-line-compare
        } should throwException(AssertionError)
    }

    @Test
    void extraEmptyLines() {
        code {
            // extra-empty-line-compare
            String output = buildOutput()
            output.should == "line one\nline two\nline three\n"
            // extra-empty-line-compare
        } should throwException(AssertionError)
    }

    @Test
    void regexp() {
        // single-line-regexp
        String output = 'final price: $8998'
        output.should == ~/final price: \$\d+/
        // single-line-regexp
    }

    @Test
    void contains() {
        code {
            // multi-line-contains
            String output = buildOutput()
            output.should contain("four")
            // multi-line-contains
        } should throwException(AssertionError)
    }

    private static String buildOutput() {
        return "line one\nline two\nline three"
    }
}
