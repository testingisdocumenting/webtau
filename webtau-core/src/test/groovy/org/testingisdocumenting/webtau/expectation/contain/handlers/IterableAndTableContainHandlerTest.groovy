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

package org.testingisdocumenting.webtau.expectation.contain.handlers

import org.junit.Test

import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runExpectExceptionAndValidateOutput

class IterableAndTableContainHandlerTest {
    @Test
    void "list of maps contains table data"() {
        def maps = [
                [a: "a1", b: "b1"],
                [a: "a2", b: "b2"],
                [a: "a3", b: "b3"],
                [a: "a4", b: "b4"]]

        def table = table("a" , "b",
                          ___________,
                          "a1", "b1",
                          "a2", "b2",
                          "a3", "b3" )

        actual(maps).should(contain(table))
    }

    @Test
    void "list of maps contain table data few rows mismatches"() {
        def maps = [
                [a: "a1", b: "b1"],
                [a: "a2", b: "b2"],
                [a: "a3", b: "b3"],
                [a: "a4", b: "b4"]]

        def table = table("a" , "b",
                          ___________,
                          "a1", "b1",
                          "a2", "b3",
                          "a3", "b4" )

        runExpectExceptionAndValidateOutput(AssertionError.class, 'X failed expecting [value] to contain a    │ b   \n' +
                '                                      "a1" │ "b1"\n' +
                '                                      "a2" │ "b3"\n' +
                '                                      "a3" │ "b4":\n' +
                '    no matches found for: [{"a": "a2", "b": "b3"}, {"a": "a3", "b": "b4"}] (Xms)\n' +
                '  \n' +
                '  [{"a": "a1", "b": "b1"}, {"a": "a2", "b": **"b2"**}, {"a": **"a3"**, "b": **"b3"**}, {"a": **"a4"**, "b": "b4"}]') {
            actual(maps).should(contain(table))
        }
    }

    @Test
    void "list of maps should not contain table data"() {
        def maps = [
                [a: "a1", b: "b1"],
                [a: "a2", b: "b2"],
                [a: "a3", b: "b3"],
                [a: "a4", b: "b4"]]

        def table = table("a" , "b",
                          ___________,
                          "a1", "b2",
                          "a2", "b3",
                          "a3", "b4" )

        actual(maps).shouldNot(contain(table))
    }

    @Test
    void "list of maps should not contain table data print rows that match"() {
        def maps = [
                [a: "a1", b: "b1"],
                [a: "a2", b: "b2"],
                [a: "a3", b: "b3"],
                [a: "a4", b: "b4"]]

        def table = table("a" , "b",
                          ___________,
                          "a1", "b2",
                          "a2", "b2",
                          "a3", "b3" )

        runExpectExceptionAndValidateOutput(AssertionError.class, 'X failed expecting [value] to not contain a    │ b   \n' +
                '                                          "a1" │ "b2"\n' +
                '                                          "a2" │ "b2"\n' +
                '                                          "a3" │ "b3":\n' +
                '    [value][1].a:  actual:     "a2" <java.lang.String>\n' +
                '                 expected: not "a2" <java.lang.String>\n' +
                '    [value][1].b:  actual:     "b2" <java.lang.String>\n' +
                '                 expected: not "b2" <java.lang.String>\n' +
                '    [value][2].a:  actual:     "a3" <java.lang.String>\n' +
                '                 expected: not "a3" <java.lang.String>\n' +
                '    [value][2].b:  actual:     "b3" <java.lang.String>\n' +
                '                 expected: not "b3" <java.lang.String> (Xms)\n' +
                '  \n' +
                '  [{"a": "a1", "b": "b1"}, {"a": **"a2"**, "b": **"b2"**}, {"a": **"a3"**, "b": **"b3"**}, {"a": "a4", "b": "b4"}]') {
            actual(maps).shouldNot(contain(table))
        }
    }
}
