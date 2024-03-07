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

import org.junit.BeforeClass
import org.junit.Test
import org.testingisdocumenting.webtau.cfg.WebTauConfig
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.data.render.PrettyPrinter
import org.testingisdocumenting.webtau.expectation.AssertionTokenizedError
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput

import java.util.function.Supplier

import static java.util.stream.Collectors.*
import static org.testingisdocumenting.webtau.WebTauCore.*
import static org.testingisdocumenting.webtau.reporter.StepReportOptions.*
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runAndValidateOutput
import static org.testingisdocumenting.webtau.testutils.TestConsoleOutput.runExpectExceptionAndValidateOutput

class WebTauStepTest {
    static WebTauStep rootStep
    static WebTauStep childStep1
    static WebTauStep childStep2

    @BeforeClass
    static void init() {
        rootStep = createStep("step action") {
            childStep1 = createStep("c1 action")
            childStep1.setStepOutputFunc((result) -> new OutputA(id: 'id2'))
            childStep1.execute(REPORT_ALL)
            childStep1.setClassifier("typeA")

            childStep2 = createStep("c2 action")
            childStep2.setStepOutputFunc((result) -> new OutputB(name: 'name3'))
            childStep2.execute(REPORT_ALL)
            childStep2.setClassifier("typeB")
        }

        rootStep.setStepOutputFunc((result) -> new OutputA(id: 'id1'))
        rootStep.setClassifier("typeA")

        rootStep.execute(REPORT_ALL)
    }

    @Test
    void "create step automatically maintains hierarchy of steps"() {
        def children = rootStep.children().collect(toList())
        assert children.size() == 2

        assert rootStep.numberOfParents == 0
        assert childStep1.numberOfParents == 1
        assert childStep2.numberOfParents == 1
    }

    @Test
    void "step should be able to return a value"() {
        def step = createStep('supplier step', { return 2 + 2 })
        def stepResult = step.execute(REPORT_ALL)

        assert stepResult == 4
    }

    @Test
    void "should recursively return all the outputs from test step and nested test steps"() {
        def outputs = rootStep.collectOutputs()
        assert outputs*.toMap() == [[id: 'id1'], [id: 'id2'], [name: 'name3']]
    }

    @Test
    void "should recursively return outputs of a given type"() {
        def outputsA = rootStep.collectOutputsOfType(OutputA).collect(toList())
        assert outputsA*.toMap() == [[id: 'id1'], [id: 'id2']]

        def outputsB = rootStep.collectOutputsOfType(OutputB).collect(toList())
        assert outputsB*.toMap() == [[name: 'name3']]
    }

    @Test
    void "should know if an output of a certain type is present"() {
        assert rootStep.hasOutput(OutputA)
        assert rootStep.hasOutput(OutputB)
        assert ! rootStep.hasOutput(OutputC)
    }

    @Test
    void "should count number of failed and successful steps"() {
        def root = createStep("root step action") {
            def step1 = createStep("c1 action") {
                throw new RuntimeException("error")
            }

            try {
                step1.execute(REPORT_ALL)
            } catch (Exception ignored) {
            }

            def step2 = createStep("c2 action")
            step2.execute(REPORT_ALL)

            def step3 = createStep("c3 action") {
                def step31 = createStep("c31 action", { throw new RuntimeException("error2") })
                step31.execute(REPORT_ALL)

                def step32 = createStep("c32 action")
                step32.execute(REPORT_ALL)
            }

            try {
                step3.execute(REPORT_ALL)
            } catch (Exception ignored) {
            }
        }

        root.execute(REPORT_ALL)

        assert root.calcNumberOfFailedSteps() == 3
        assert root.calcNumberOfSuccessfulSteps() == 2
    }

    @Test
    void "should remove repeated children steps inside repeatable step"() {
        def repeatStep = WebTauStep.createRepeatStep("my task", 20) {
            createStep("inside step ${it.attemptNumber}").execute(REPORT_ALL)
        }
        repeatStep.execute(REPORT_ALL)

        def children = repeatStep.children().collect(toList())
        assert children.completionMessage*.toString() == ['completed repeat #1', 'completed repeat #20']
    }

    @Test
    void "should not remove repeated failed children steps inside repeatable step"() {
        def repeatStep = WebTauStep.createRepeatStep("my task", 20) {ctx ->
            createStep("inside step ${ctx.attemptNumber}") {
                if (ctx.attemptNumber == 8) {
                    throw new RuntimeException("unknown failure")
                }
            }.execute(REPORT_ALL)
        }

        runAndValidateOutput(contain("failed repeat #8: <RuntimeException> unknown failure")) {
            repeatStep.execute(REPORT_ALL)
        }

        def children = repeatStep.children().collect(toList())
        assert children.completionMessage*.toString() == ['completed repeat #1', 'failed repeat #8', 'completed repeat #20']
    }

    @Test
    void "should find steps by classifier"() {
        def steps = rootStep.stepsWithClassifier("typeA").collect(toList())
        assert steps.size() == 2

        assert steps[0].completionMessage.toString() == "done step action"
        assert steps[1].completionMessage.toString() == "done c1 action"
    }

    @Test
    void "should include exception name"() {
        def step = createStep("exception step action") {
            throw new NullPointerException()
        }

        runExpectExceptionAndValidateOutput(NullPointerException, "> exception step action\n" +
                "X failed exception step action: <NullPointerException> (Xms)") {
            step.execute(REPORT_ALL)
        }
    }

    @Test
    void "should include exception name and message when present"() {
        def step = createStep("exception step action") {
            throw new ArrayIndexOutOfBoundsException("range")
        }

        runExpectExceptionAndValidateOutput(ArrayIndexOutOfBoundsException, "> exception step action\n" +
                "X failed exception step action: <ArrayIndexOutOfBoundsException> range (Xms)") {
            step.execute(REPORT_ALL)
        }
    }

    @Test
    void "should throw reduced message when console reporters present"() {
        try {
            StepReporters.add(StepReporters.defaultStepReporter)

            def step1 = createStep("c1 action") {
                throw new AssertionTokenizedError(tokenizedMessage().error("error").newLine().action("details"))
            }

            code {
                step1.execute(SKIP_START)
            } should(throwException("see the failed assertion details above"))
        } finally {
            StepReporters.remove(StepReporters.defaultStepReporter)
        }
    }

    @Test
    void "should throw full exception details if verbosity level is zero"() {
        WebTauConfig.getCfg().setVerbosityLevel(0)

        try {
            def step1 = createStep("c1 action") {
                throw new AssertionTokenizedError(tokenizedMessage().error("error").newLine().action("details"))
            }

            code {
                step1.execute(SKIP_START)
            } should(throwException("error\ndetails"))
        } finally {
            WebTauConfig.getCfg().setVerbosityLevel(Integer.MAX_VALUE)
        }
    }

    @Test
    void "should throw full exception details if console step reporter is not present"() {
        def output = new TestConsoleOutput()
        try {
            ConsoleOutputs.add(output)
            def step1 = createStep("c1 action") {
                throw new AssertionTokenizedError(tokenizedMessage().error("error").newLine().action("details"))
            }

            code {
                step1.execute(SKIP_START)
            } should(throwException("error\ndetails"))
        } finally {
            ConsoleOutputs.remove(output)
        }
    }

    private static WebTauStep createStep(String title, Supplier stepCode = { return null }) {
        return WebTauStep.createStep(tokenizedMessage().action(title), {
            tokenizedMessage().action('done ' + title)
        } as Supplier, stepCode)
    }

    private static class OutputA implements WebTauStepOutput {
        String id

        @Override
        Map<String, ?> toMap() {
            return [id: id]
        }

        @Override
        void prettyPrint(PrettyPrinter printer) {
        }
    }

    private static class OutputB implements WebTauStepOutput {
        String name

        @Override
        Map<String, ?> toMap() {
            return [name: name]
        }

        @Override
        void prettyPrint(PrettyPrinter printer) {
        }
    }

    private static class OutputC implements WebTauStepOutput {
        @Override
        Map<String, ?> toMap() {
            return [:]
        }

        @Override
        void prettyPrint(PrettyPrinter printer) {
        }
    }
}
