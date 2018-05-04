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

package com.twosigma.webtau.expectation.contain.handlers

import com.twosigma.webtau.expectation.contain.ContainAnalyzer
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import static com.twosigma.webtau.Ddjt.createActualPath

class StringContainHandlerTest {
    private ContainAnalyzer analyzer

    @Before
    void init() {
        analyzer = ContainAnalyzer.containAnalyzer()
    }

    @Test
    void "handles only strings"() {
        def handler = new StringContainHandler()
        assert handler.handle("text", "ext")
        assert !handler.handle("text", 10)
        assert !handler.handle(10, 20)
    }

    @Test
    void "no mismatches when actual string contains expected"() {
        assert analyzer.contains(createActualPath("text"), "hello world", "world")
    }

    @Test
    void "mismatches report should have both values"() {
        assert ! analyzer.contains(createActualPath("text"), "hello world", "disc")

        Assert.assertEquals("does not contain:\n" +
                "\n" +
                "text:              actual: hello world\n" +
                "      expected to contain: disc", analyzer.generateMismatchReport())
    }
}
