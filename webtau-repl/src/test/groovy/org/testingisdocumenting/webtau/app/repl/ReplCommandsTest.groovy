/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.app.repl

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.webtau.TestFile
import org.testingisdocumenting.webtau.WebTauGroovyDsl
import org.testingisdocumenting.webtau.GroovyRunner
import org.testingisdocumenting.webtau.console.ConsoleOutput
import org.testingisdocumenting.webtau.console.ConsoleOutputs
import org.testingisdocumenting.webtau.console.ansi.AnsiConsoleOutput
import org.testingisdocumenting.webtau.console.ansi.IgnoreAnsiString
import org.testingisdocumenting.webtau.repl.Repl
import org.testingisdocumenting.webtau.reporter.ConsoleStepReporter
import org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder
import org.testingisdocumenting.webtau.reporter.StepReporter
import org.testingisdocumenting.webtau.reporter.StepReporters
import org.testingisdocumenting.webtau.reporter.WebTauStep
import org.testingisdocumenting.webtau.runner.standalone.StandaloneTestRunner

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.contain
import static org.testingisdocumenting.webtau.repl.ReplCommands.*

class ReplCommandsTest implements StepReporter, ConsoleOutput {
    AnsiConsoleOutput consoleOutput = new AnsiConsoleOutput()
    ConsoleStepReporter consoleStepReporter = new ConsoleStepReporter(IntegrationTestsMessageBuilder.getConverter(),
            () -> Integer.MAX_VALUE)
    StringBuilder out
    StringBuilder err

    Repl repl

    @Before
    void init() {
        def workingDir = Paths.get("").toAbsolutePath()
        def runner = new StandaloneTestRunner(
                GroovyRunner.createWithoutDelegating(workingDir),
                workingDir)

        WebTauGroovyDsl.initWithTestRunner(runner)

        runner.process(new TestFile(Paths.get("src/test/resources/repl/doNothingScenariosOne.groovy")))
        runner.process(new TestFile(Paths.get("src/test/resources/repl/doNothingScenariosTwo.groovy")))

        repl = new Repl(runner)

        out = new StringBuilder()
        err = new StringBuilder()
        ConsoleOutputs.add(this)
        ConsoleOutputs.add(consoleOutput)
        StepReporters.add(consoleStepReporter)
    }

    @After
    void cleanup() {
        ConsoleOutputs.remove(this)
        ConsoleOutputs.remove(consoleOutput)
        StepReporters.remove(consoleStepReporter)

        // get back from the currently selected scenario and test file
        getBack()
        getBack()
    }

    @Test
    void "should allow to select negative index scenario"() {
        select -1
        testsSelection.testFilePath.should == 'src/test/resources/repl/doNothingScenariosTwo.groovy'
    }

    @Test
    void "should allow to select negative index test"() {
        select "doNothingScenariosOne"

        select -1
        testsSelection.scenarios.should == ['one scenario three']

        select 1
        testsSelection.scenarios.should == ['one scenario two']
    }

    @Test
    void "should allow to select range with negative index at the end and display selection later"() {
        select "doNothingScenariosOne"

        select 1:-1

        testsSelection.scenarios.should == ['one scenario two', 'one scenario three']

        clearOut()

        repl.resultRenderer.renderResult(s)
        out.should contain('one scenario two')
        out.should contain('one scenario three')
    }

    @Test
    void "should allow to run negative index test and re-run it"() {
        select "doNothingScenariosOne"

        r -1
        testsSelection.scenarios.should == ['one scenario three']

        out.should contain("step inside three")

        clearOut()
        repl.resultRenderer.renderResult(r)
        out.should contain("step inside three")
    }

    @Override
    void out(Object... styleOrValues) {
        out.append(new IgnoreAnsiString(styleOrValues).toString()).append('\n')
    }

    @Override
    void err(Object... styleOrValues) {
        err.append(new IgnoreAnsiString(styleOrValues).toString()).append('\n')
    }

    @Override
    void onStepStart(WebTauStep step) {

    }

    @Override
    void onStepSuccess(WebTauStep step) {
        out.append(step.completionMessage.toString())
    }

    @Override
    void onStepFailure(WebTauStep step) {
        err.append(step.completionMessage.toString())
    }

    private void clearOut() {
        out.setLength(0)
    }
}
