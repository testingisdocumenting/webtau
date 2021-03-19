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

package org.testingisdocumenting.webtau.reporter

import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.AnsiConsoleOutput
import org.testingisdocumenting.webtau.console.ansi.IgnoreAnsiString
import org.testingisdocumenting.webtau.time.ControlledTimeProvider
import org.testingisdocumenting.webtau.time.Time
import org.junit.After
import org.junit.Before
import org.junit.Test

import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.action
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.matcher
import static org.junit.Assert.assertEquals

class ConsoleStepReporterTest implements ConsoleOutput {
    private static ConsoleOutput ansiConsoleOutput = new AnsiConsoleOutput()

    List<String> lines

    @Before
    void init() {
        ConsoleOutputs.add(this)
        ConsoleOutputs.add(ansiConsoleOutput)

        Time.setTimeProvider(new ControlledTimeProvider(0))
    }

    @After
    void cleanUp() {
        ConsoleOutputs.remove(this)
        ConsoleOutputs.remove(ansiConsoleOutput)

        Time.setTimeProvider(null)
    }

    @Test
    void "should indent multiline assertion message at the end of a step message"() {
        def topLevelStep = WebTauStep.createStep(null, TokenizedMessage.tokenizedMessage(action("top level action")),
                { -> TokenizedMessage.tokenizedMessage(action("top level action completed")) }) {

            def validationStep = WebTauStep.createStep(null, TokenizedMessage.tokenizedMessage(action("validation")),
                    { -> TokenizedMessage.tokenizedMessage(action("validated"),
                            matcher(multilineMatcherMessage('matches'))) }) {
            }

            validationStep.execute(StepReportOptions.SKIP_START)
        }

        expectReport(Integer.MAX_VALUE, '> top level action\n' +
                '  . validated equals 100\n' +
                '      matches:\n' +
                '      \n' +
                '      body.price:   actual: 100 <java.lang.Integer>\n' +
                '                  expected: 100 <java.lang.Integer> (0ms)\n' +
                '. top level action completed (0ms)') {
            topLevelStep.execute(StepReportOptions.REPORT_ALL)
        }
    }

    @Test
    void "should indent multiline error message at the end of a step message"() {
        def topLevelStep = WebTauStep.createStep(null, TokenizedMessage.tokenizedMessage(action("top level action")),
                { -> TokenizedMessage.tokenizedMessage(action("top level action completed")) }) {

            def validationStep = WebTauStep.createStep(null, TokenizedMessage.tokenizedMessage(action("validation")),
                    { -> TokenizedMessage.tokenizedMessage(action("validation"),
                            matcher(multilineMatcherMessage('matches'))) }) {

                throw new AssertionError(multilineMatcherMessage('mismatches'))
            }

            validationStep.execute(StepReportOptions.SKIP_START)
        }

        expectReport(Integer.MAX_VALUE, '> top level action\n' +
                '  X failed validation : equals 100\n' +
                '      mismatches:\n' +
                '      \n' +
                '      body.price:   actual: 100 <java.lang.Integer>\n' +
                '                  expected: 100 <java.lang.Integer> (0ms)\n' +
                'X failed top level action (0ms)') {
            topLevelStep.execute(StepReportOptions.REPORT_ALL)
        }
    }

    @Test
    void "should render time step took in milliseconds"() {
        Time.setTimeProvider(new ControlledTimeProvider([100, 350]))
        def action = WebTauStep.createStep(null, TokenizedMessage.tokenizedMessage(action("action")),
                { -> TokenizedMessage.tokenizedMessage(action("action completed")) }) {
        }

        expectReport(Integer.MAX_VALUE, '> action\n' +
                '. action completed (250ms)') {
            action.execute(StepReportOptions.REPORT_ALL)
        }
    }

    @Test
    void "should render failed step time took in milliseconds"() {
        Time.setTimeProvider(new ControlledTimeProvider([100, 350]))
        def action = WebTauStep.createStep(null, TokenizedMessage.tokenizedMessage(action("action")),
                { -> TokenizedMessage.tokenizedMessage(action("action completed")) }) {
            throw new RuntimeException("error")
        }

        expectReport(Integer.MAX_VALUE, '> action\n' +
                'X failed action : error (250ms)') {
            action.execute(StepReportOptions.REPORT_ALL)
        }
    }

    @Test
    void "should render long running step time in seconds"() {
        Time.setTimeProvider(new ControlledTimeProvider([100, 5350]))
        def action = WebTauStep.createStep(null, TokenizedMessage.tokenizedMessage(action("action")),
                { -> TokenizedMessage.tokenizedMessage(action("action completed")) }) {
        }

        expectReport(Integer.MAX_VALUE, '> action\n' +
                '. action completed (5s 250ms)') {
            action.execute(StepReportOptions.REPORT_ALL)
        }
    }

    @Test
    void "should not render step if a nest level is greater than provided max"() {
        expectReport(0, '') {
            executeNestedSteps()
        }

        expectReport(1,
                '> top level action\n' +
                'X failed top level action : dummy out of memory (0ms)') {
            executeNestedSteps()
        }

        expectReport(2,
                '> top level action\n' +
                        '  > second level action\n' +
                        '  . second level action completed (0ms)\n' +
                        '  > second level action with error\n' +
                        '  X failed second level action with error : dummy out of memory (0ms)\n' +
                        'X failed top level action (0ms)') {
            executeNestedSteps()
        }

        expectReport(3, '> top level action\n' +
                '  > second level action\n' +
                '  . second level action completed (0ms)\n' +
                '  > second level action with error\n' +
                '    > third level action\n' +
                '    X failed third level action : dummy out of memory (0ms)\n' +
                '  X failed second level action with error (0ms)\n' +
                'X failed top level action (0ms)') {
            executeNestedSteps()
        }
    }

    private static void executeNestedSteps() {
        def topLevelStep = WebTauStep.createStep(null, TokenizedMessage.tokenizedMessage(action("top level action")),
                { -> TokenizedMessage.tokenizedMessage(action("top level action completed")) }) {

            def secondLevelStepSuccess = WebTauStep.createStep(null, TokenizedMessage.tokenizedMessage(action("second level action")),
                    { -> TokenizedMessage.tokenizedMessage(action("second level action completed")) }) {
            }

            secondLevelStepSuccess.execute(StepReportOptions.REPORT_ALL)

            def secondLevelStepFailure = WebTauStep.createStep(null, TokenizedMessage.tokenizedMessage(action("second level action with error")),
                    { -> TokenizedMessage.tokenizedMessage(action("second level action with error completed")) }) {

                def thirdLevelStep = WebTauStep.createStep(null, TokenizedMessage.tokenizedMessage(action("third level action")),
                        { -> TokenizedMessage.tokenizedMessage(action("third level action completed")) }) {
                    throw new RuntimeException('dummy out of memory')
                }

                thirdLevelStep.execute(StepReportOptions.REPORT_ALL)
            }

            secondLevelStepFailure.execute(StepReportOptions.REPORT_ALL)
        }

        topLevelStep.execute(StepReportOptions.REPORT_ALL)
    }


    private void expectReport(int verbosityLevel, String expectedReport, Closure code) {
        lines = new ArrayList<>()
        def stepReporter = new ConsoleStepReporter(IntegrationTestsMessageBuilder.getConverter(), () -> verbosityLevel)

        try {
            StepReporters.add(stepReporter)
            code.run()
        } catch(Throwable ignored) {
        }

        StepReporters.remove(stepReporter)

        assertEquals(expectedReport, lines.join('\n'))
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
        lines.add(new IgnoreAnsiString(styleOrValues).toString())
    }
}
