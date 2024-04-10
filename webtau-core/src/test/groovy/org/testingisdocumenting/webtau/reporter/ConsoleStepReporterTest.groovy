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

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.AnsiConsoleOutput
import org.testingisdocumenting.webtau.console.ansi.IgnoreAnsiString
import org.testingisdocumenting.webtau.data.render.PrettyPrinter
import org.testingisdocumenting.webtau.expectation.AssertionTokenizedError
import org.testingisdocumenting.webtau.time.ControlledTimeProvider
import org.testingisdocumenting.webtau.time.Time
import static org.testingisdocumenting.webtau.WebTauCore.*

import static org.junit.Assert.*

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
    void "should indent multiline assert message at the end of a step message"() {
        def topLevelStep = createStepWithNestedException(new AssertionError('line1\nline2' as Object))

        expectReport(Integer.MAX_VALUE, '> top level action\n' +
                '  X failed validation:\n' +
                '      <AssertionError> line1\n' +
                '      line2 (0ms)\n' +
                'X failed top level action (0ms)') {
            topLevelStep.execute(StepReportOptions.REPORT_ALL)
        }
    }

    @Test
    void "should indent multiline runtime exception at the end of a step message"() {
        def topLevelStep = createStepWithNestedException(new RuntimeException('line1\nline2'))

        expectReport(Integer.MAX_VALUE, '> top level action\n' +
                '  X failed validation:\n' +
                '      <RuntimeException> line1\n' +
                '      line2 (0ms)\n' +
                'X failed top level action (0ms)') {
            topLevelStep.execute(StepReportOptions.REPORT_ALL)
        }
    }

    @Test
    void "should maintain single line exception message"() {
        def topLevelStep = createStepWithNestedException(new RuntimeException('single line error'))

        expectReport(Integer.MAX_VALUE, '> top level action\n' +
                '  X failed validation: <RuntimeException> single line error (0ms)\n' +
                'X failed top level action (0ms)') {
            topLevelStep.execute(StepReportOptions.REPORT_ALL)
        }
    }

    @Test
    void "should render time step took in milliseconds"() {
        Time.setTimeProvider(new ControlledTimeProvider([100, 350]))
        def action = WebTauStep.createStep(tokenizedMessage().action("action"),
                { -> tokenizedMessage().action("action completed") }) {
        }

        expectReport(Integer.MAX_VALUE, '> action\n' +
                '. action completed (250ms)') {
            action.execute(StepReportOptions.REPORT_ALL)
        }
    }

    @Test
    void "should render failed step time took in milliseconds"() {
        Time.setTimeProvider(new ControlledTimeProvider([100, 350]))
        def action = WebTauStep.createStep(tokenizedMessage().action("action"),
                { -> tokenizedMessage().action("action completed") }) {
            throw new RuntimeException("error")
        }

        expectReport(Integer.MAX_VALUE, '> action\n' +
                'X failed action: <RuntimeException> error (250ms)') {
            action.execute(StepReportOptions.REPORT_ALL)
        }
    }

    @Test
    void "should render long running step time in seconds"() {
        Time.setTimeProvider(new ControlledTimeProvider([100, 5350]))
        def action = WebTauStep.createStep(tokenizedMessage().action("action"),
                { -> tokenizedMessage().action("action completed") }) {
        }

        expectReport(Integer.MAX_VALUE, '> action\n' +
                '. action completed (5s 250ms)') {
            action.execute(StepReportOptions.REPORT_ALL)
        }
    }

    @Test
    void "should render failed step on a new line if error comes with additional values"() {
        Time.setTimeProvider(new ControlledTimeProvider([100, 350]))
        def action = WebTauStep.createStep(tokenizedMessage().action("action"),
                { -> tokenizedMessage().action("action completed") }) {
            throw new AssertionTokenizedError(tokenizedMessage().error("error summary").colon().value(map("key", "value")))
        }

        expectReport(Integer.MAX_VALUE, '> action\n' +
                'X failed action:\n' +
                '    error summary: {"key": "value"} (250ms)') {
            action.execute(StepReportOptions.REPORT_ALL)
        }
    }

    @Test
    void "should indent step input pretty print"() {
        def topLevelStep = WebTauStep.createStep(tokenizedMessage().action("top level action"),
                { -> tokenizedMessage().action("top level action completed") }) {

            def secondLevelStepSuccess = WebTauStep.createStep(tokenizedMessage().action("second level action"),
                    { -> tokenizedMessage().action("second level action completed") }) {
            }

            secondLevelStepSuccess.setInput(new TestStepInput())
            secondLevelStepSuccess.setStepOutputFunc((result) -> new TestStepOutput())
            secondLevelStepSuccess.execute(StepReportOptions.REPORT_ALL)
        }

        expectReport(Integer.MAX_VALUE, '> top level action\n' +
                '  > second level action\n' +
                '    hello input\n' +
                '    world\n' +
                '    hello output\n' +
                '    world\n' +
                '  . second level action completed (0ms)\n' +
                '. top level action completed (0ms)') {
            topLevelStep.execute(StepReportOptions.REPORT_ALL)
        }
    }

    @Test
    void "should render repeated step progress"() {
        def repeatStep = WebTauStep.createRepeatStep("repeat", 5) { ctx ->
            def step = WebTauStep.createStep(tokenizedMessage().action("repeat"),
                    { -> tokenizedMessage().action("completed repeat") }) {
            }
            step.execute(StepReportOptions.REPORT_ALL)
        }

        expectReport(Integer.MAX_VALUE, '> repeat repeat 5 times\n' +
                '  > repeat #1\n' +
                '    > repeat\n' +
                '    . completed repeat (0ms)\n' +
                '  . completed repeat #1 (0ms)\n' +
                '  > 2/5\n' +
                '  . 2/5 (0ms)\n' +
                '  > 3/5\n' +
                '  . 3/5 (0ms)\n' +
                '  > 4/5\n' +
                '  . 4/5 (0ms)\n' +
                '  > repeat #5\n' +
                '    > repeat\n' +
                '    . completed repeat (0ms)\n' +
                '  . completed repeat #5 (0ms)\n' +
                '. repeated repeat 5 times (0ms)') {
            repeatStep.execute(StepReportOptions.REPORT_ALL)
        }
    }

    @Test
    void "should render failed step within repeated step progress"() {
        def repeatStep = WebTauStep.createRepeatStep("repeat", 5) { ctx ->
            def step = WebTauStep.createStep(tokenizedMessage().action("repeat"),
                    { -> tokenizedMessage().action("completed repeat") }) {
                if (ctx.attemptNumber == 2) {
                    throw new RuntimeException("no file found")
                }
            }
            step.execute(StepReportOptions.REPORT_ALL)
        }

        expectReport(Integer.MAX_VALUE, '> repeat repeat 5 times\n' +
                '  > repeat #1\n' +
                '    > repeat\n' +
                '    . completed repeat (0ms)\n' +
                '  . completed repeat #1 (0ms)\n' +
                '  > 2/5\n' +
                '  X failed repeat #2: <RuntimeException> no file found (0ms)\n' +
                '  > 3/5\n' +
                '  . 3/5 (0ms)\n' +
                '  > 4/5\n' +
                '  . 4/5 (0ms)\n' +
                '  > repeat #5\n' +
                '    > repeat\n' +
                '    . completed repeat (0ms)\n' +
                '  . completed repeat #5 (0ms)\n' +
                '. repeated repeat 5 times (0ms)') {
            repeatStep.execute(StepReportOptions.REPORT_ALL)
        }
    }

    @Test
    void "should not render step if a nest level is greater than provided max"() {
        expectReport(0, '') {
            executeNestedSteps()
        }

        expectReport(1,
                '> top level action\n' +
                'X failed top level action: <RuntimeException> dummy out of memory (0ms)') {
            executeNestedSteps()
        }

        expectReport(2,
                '> top level action\n' +
                        '  > second level action\n' +
                        '  . second level action completed (0ms)\n' +
                        '  > second level action with error\n' +
                        '  X failed second level action with error: <RuntimeException> dummy out of memory (0ms)\n' +
                        'X failed top level action (0ms)') {
            executeNestedSteps()
        }

        expectReport(3, '> top level action\n' +
                '  > second level action\n' +
                '  . second level action completed (0ms)\n' +
                '  > second level action with error\n' +
                '    > third level action\n' +
                '    X failed third level action: <RuntimeException> dummy out of memory (0ms)\n' +
                '  X failed second level action with error (0ms)\n' +
                'X failed top level action (0ms)') {
            executeNestedSteps()
        }
    }

    @Test
    void "should render trace steps in a special manner"() {
        expectReport(100, "[tracing] trace label\n" +
                "    key: \"value\"") {
            trace("trace label", "key", "value")
        }
    }

    @Test
    void "should render warning steps in a special manner"() {
        expectReport(100, "[warning] warning label\n" +
                "    key: \"value\"") {
            warning("warning label", "key", "value")
        }
    }

    private static void executeNestedSteps() {
        def topLevelStep = WebTauStep.createStep(tokenizedMessage().action("top level action"),
                { -> tokenizedMessage().action("top level action completed") }) {

            def secondLevelStepSuccess = WebTauStep.createStep(tokenizedMessage().action("second level action"),
                    { -> tokenizedMessage().action("second level action completed") }) {
            }

            secondLevelStepSuccess.execute(StepReportOptions.REPORT_ALL)

            def secondLevelStepFailure = WebTauStep.createStep(tokenizedMessage().action("second level action with error"),
                    { -> tokenizedMessage().action("second level action with error completed") }) {

                def thirdLevelStep = WebTauStep.createStep(tokenizedMessage().action("third level action"),
                        { -> tokenizedMessage().action("third level action completed") }) {
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
        def stepReporter = new ConsoleStepReporter(ConsoleOutputs.asCombinedConsoleOutput(),
                TokenizedMessageToAnsiConverter.DEFAULT, () -> verbosityLevel)

        try {
            StepReporters.add(stepReporter)
            code.run()
        } catch (Throwable ignored) {
        }

        StepReporters.remove(stepReporter)

        assertEquals(expectedReport, lines.join('\n'))
    }

    @Override
    void out(Object... styleOrValues) {
        lines.add(new IgnoreAnsiString(styleOrValues).toString())
    }

    @Override
    void err(Object... styleOrValues) {
        lines.add(new IgnoreAnsiString(styleOrValues).toString())
    }

    private static WebTauStep createStepWithNestedException(exception) {
        def topLevelStep = WebTauStep.createStep(tokenizedMessage().action("top level action"),
                { -> tokenizedMessage().action("top level action completed") }) {

            def validationStep = WebTauStep.createStep(tokenizedMessage().action("validation"),
                    { -> tokenizedMessage().action("validation")  }) {

                throw exception
            }

            validationStep.execute(StepReportOptions.SKIP_START)
        }

        return topLevelStep
    }

    private static class TestStepInput implements WebTauStepInput {
        @Override
        void prettyPrint(PrettyPrinter printer) {
            printer.printLine("hello input")
            printer.printLine("world")
        }

        @Override
        Map<String, ?> toMap() {
            return Collections.emptyMap()
        }
    }

    private static class TestStepOutput implements WebTauStepOutput {
        @Override
        void prettyPrint(PrettyPrinter printer) {
            printer.printLine("hello output")
            printer.printLine("world")
        }

        @Override
        Map<String, ?> toMap() {
            return Collections.emptyMap()
        }
    }
}
