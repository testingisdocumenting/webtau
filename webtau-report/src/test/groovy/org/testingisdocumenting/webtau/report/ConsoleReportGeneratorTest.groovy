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

package org.testingisdocumenting.webtau.report

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.AutoResetAnsiString
import org.testingisdocumenting.webtau.console.ansi.IgnoreAnsiString
import org.testingisdocumenting.webtau.reporter.*
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.webtau.WebTauCore.*

class ConsoleReportGeneratorTest implements ConsoleOutput {
    def lines = []
    def output = ""

    @Before
    void setup() {
        lines.clear()
        output = ""
        ConsoleOutputs.add(this)
    }

    @After
    void cleanup() {
        ConsoleOutputs.remove(this)
    }

    @Test
    void "print only first three failed tests"() {
        def tests = new WebTauTestList()
        tests.add(createFailedTest("test one"))
        tests.add(createFailedTest("test two"))
        tests.add(createFailedTest("test three"))
        tests.add(createFailedTest("test four"))
        tests.add(createFailedTest("test five"))
        tests.add(createFailedTest("test six"))

        def report = createReport(tests)

        new ConsoleReportGenerator().generate(report)

        lines.should contain("you have 6 failed test(s):")
        lines.should contain("...(3 more failed tests)")
    }

    @Test
    void "print all tests if within threshold"() {
        def tests = new WebTauTestList()
        tests.add(createFailedTest("test one"))
        tests.add(createFailedTest("test two"))
        tests.add(createFailedTest("test three"))
        tests.add(createFailedTest("test four"))

        def report = createReport(tests)

        new ConsoleReportGenerator().generate(report)

        lines.should contain("you have 4 failed test(s):")
        lines.shouldNot contain("...(1 more failed tests)")

        lines.should contain("[x] test four (dummy)")
    }

    @Test
    void "print only first three errored tests"() {
        def tests = new WebTauTestList()
        tests.add(createErroredTest("test one"))
        tests.add(createErroredTest("test two"))
        tests.add(createErroredTest("test three"))
        tests.add(createErroredTest("test four"))
        tests.add(createErroredTest("test five"))
        tests.add(createErroredTest("test six"))

        def report = createReport(tests)

        new ConsoleReportGenerator().generate(report)

        lines.should contain("you have 6 errored test(s):")
        lines.should contain("...(3 more errored tests)")
    }

    @Test
    void "print failed test steps"() {
        def tests = new WebTauTestList()
        tests.add(createTestWithNestedFailedStepsAndOutput("with nested"))

        def report = createReport(tests)
        new ConsoleReportGenerator().generate(report)

        output.should == '> do x\n' +
                '  > nested do x\n' +
                '    nestedKey: "value1"\n' +
                '  X failed nested do x: nested failed step (Xms)\n' +
                '  outerKey: "value1"\n' +
                'X failed do x (Xms)\n' +
                '\n' +
                'you have 1 errored test(s):\n' +
                '[x] with nested (dummy)\n' +
                'X failed do x (Xms)\n' +
                '  X failed nested do x: nested failed step (Xms)\n' +
                '    nestedKey: "value1"\n' +
                '  outerKey: "value1"\n' +
                'java.lang.RuntimeException: nested failed step\n' +
                '\n' +
                'Total time: Xms\n' +
                'Total: 1,  Passed: 0,  Skipped: 0,  Failed: 0,  Errored: 1\n'
    }

    static def createFailedTest(String scenario) {
        def exception = new AssertionError("mismatch" as Object)
        return createTestWithException(scenario, exception)
    }

    static def createErroredTest(String scenario) {
        def exception = new RuntimeException("error")
        return createTestWithException(scenario, exception)
    }

    static def createTestWithException(String scenario, exception) {
        def test = new WebTauTest(Paths.get(""))
        test.id = scenario.toLowerCase()
        test.scenario = scenario
        test.shortContainerId = "dummy"

        def step = WebTauStep.createStep(
                tokenizedMessage().action("do x"),
                () -> tokenizedMessage().action("done x"),
                () -> {
                    throw exception
                })

        try {
            step.execute(StepReportOptions.REPORT_ALL)
        } catch (Throwable ignored) {
        }

        test.addStep(step)
        test.exception = exception

        return test
    }

    static def createTestWithNestedFailedStepsAndOutput(String scenario) {
        def test = new WebTauTest(Paths.get(""))
        test.id = scenario.toLowerCase()
        test.scenario = scenario
        test.shortContainerId = "dummy"

        def exception = new RuntimeException("nested failed step")
        def stepOuter = WebTauStep.createStep(
                tokenizedMessage().action("do x"),
                () -> tokenizedMessage().action("done x"),
                () -> {
                    def nestedStep = WebTauStep.createStep(
                            tokenizedMessage().action("nested do x"),
                                    () -> tokenizedMessage().action("nested done x"),
                                            () -> {
                                                throw exception
                                            })

                    nestedStep.setOutput(WebTauStepOutputKeyValue.stepOutput(["nestedKey": "value1"]))
                    nestedStep.execute(StepReportOptions.REPORT_ALL)
                })

        stepOuter.setOutput(WebTauStepOutputKeyValue.stepOutput("outerKey", "value1"))

        try {
            stepOuter.execute(StepReportOptions.REPORT_ALL)
        } catch (Throwable ignored) {
        }

        test.addStep(stepOuter)
        test.exception = exception

        return test
    }

    static WebTauReport createReport(WebTauTestList tests) {
        def report = new WebTauReport(new WebTauReportName("test", ""), tests, 0, 0)
        report.addCustomData(new WarningsReportDataProvider().provide(tests, new WebTauReportLog()).findFirst().orElse(null))

        return report
    }

    @Override
    void out(Object... styleOrValues) {
        println new AutoResetAnsiString(styleOrValues)

        def noAnsiText = TestConsoleOutput.replaceTimeAndPort(new IgnoreAnsiString(styleOrValues).toString())
        lines.add(noAnsiText)

        output += noAnsiText + "\n"
    }

    @Override
    void err(Object... styleOrValues) {
    }
}
