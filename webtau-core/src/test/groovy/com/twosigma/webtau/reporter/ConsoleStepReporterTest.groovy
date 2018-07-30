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

package com.twosigma.webtau.reporter

import com.twosigma.webtau.console.ConsoleOutput
import com.twosigma.webtau.console.ConsoleOutputs
import com.twosigma.webtau.console.ansi.AnsiConsoleOutput
import com.twosigma.webtau.console.ansi.IgnoreAnsiString
import org.junit.After
import org.junit.Before
import org.junit.Test

import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.throwException
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.action
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.matcher
import static org.junit.Assert.assertEquals

class ConsoleStepReporterTest implements ConsoleOutput {
    private static ConsoleOutput ansiConsoleOutput = new AnsiConsoleOutput()
    List<String> lines

    @Before
    void init() {
        lines = new ArrayList<>()
        ConsoleOutputs.add(this)
        ConsoleOutputs.add(ansiConsoleOutput)
    }

    @After
    void cleanUp() {
        ConsoleOutputs.remove(this)
        ConsoleOutputs.remove(ansiConsoleOutput)
    }

    @Test
    void "should indent multiline assertion message at the end of a step message"() {
        def topLevelStep = TestStep.create(null, TokenizedMessage.tokenizedMessage(action("top level action")),
                { -> TokenizedMessage.tokenizedMessage(action("top level action completed")) }) {

            def validationStep = TestStep.create(null, TokenizedMessage.tokenizedMessage(action("validation")),
                    { -> TokenizedMessage.tokenizedMessage(action("validated"),
                            matcher(multilineMatcherMessage('matches'))) }) {
            }

            validationStep.execute(StepReportOptions.SKIP_START)
        }

        topLevelStep.execute(StepReportOptions.REPORT_ALL)

        assertEquals('> top level action\n' +
                '  . validated equals 100\n' +
                '      matches:\n' +
                '      \n' +
                '      body.price:   actual: 100 <java.lang.Integer>\n' +
                '                  expected: 100 <java.lang.Integer>\n' +
                '. top level action completed', lines.join('\n'))

    }

    @Test
    void "should indent multiline error message at the end of a step message"() {
        def topLevelStep = TestStep.create(null, TokenizedMessage.tokenizedMessage(action("top level action")),
                { -> TokenizedMessage.tokenizedMessage(action("top level action completed")) }) {

            def validationStep = TestStep.create(null, TokenizedMessage.tokenizedMessage(action("validation")),
                    { -> TokenizedMessage.tokenizedMessage(action("validation"),
                            matcher(multilineMatcherMessage('matches'))) }) {

                throw new AssertionError(multilineMatcherMessage('mismatches'))
            }

            validationStep.execute(StepReportOptions.SKIP_START)
        }

        code {
            topLevelStep.execute(StepReportOptions.REPORT_ALL)
        } should throwException(AssertionError)

        assertEquals('> top level action\n' +
                '  X failed validation : equals 100\n' +
                '      mismatches:\n' +
                '      \n' +
                '      body.price:   actual: 100 <java.lang.Integer>\n' +
                '                  expected: 100 <java.lang.Integer>\n' +
                'X failed top level action', lines.join('\n'))
    }

    private static String multilineMatcherMessage(String label) {
        return 'equals 100\n' +
                label + ':\n' +
                '\n' +
                'body.price:   actual: 100 <java.lang.Integer>\n' +
                '            expected: 100 <java.lang.Integer>'
    }

    @Override
    void out(Object... styleOrValues) {
        lines.add(new IgnoreAnsiString(styleOrValues).toString())
    }

    @Override
    void err(Object... styleOrValues) {
    }
}
