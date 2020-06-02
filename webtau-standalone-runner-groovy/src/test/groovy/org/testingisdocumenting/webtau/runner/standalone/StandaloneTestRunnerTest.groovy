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

package org.testingisdocumenting.webtau.runner.standalone

import org.testingisdocumenting.webtau.TestFile
import org.testingisdocumenting.webtau.reporter.TestListener
import org.testingisdocumenting.webtau.reporter.TestListeners
import org.junit.Test
import org.testingisdocumenting.webtau.reporter.TestStep
import org.testingisdocumenting.webtau.reporter.TokenizedMessage

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.WebTauCore.contain
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.none

class StandaloneTestRunnerTest {
    @Test
    void "should register tests with scenario keyword"() {
        def runner = createRunner("StandaloneTest.groovy")
        runner.tests.scenario.should == ['# first header\noptionally split into multiple lines and has a header\n',
                                         '# second header\noptionally split into multiple lines and has a header\n',
                                         '# third header\noptionally split into multiple lines and has a header\n']
    }

    @Test
    void "should mark test as failed, passed or errored"() {
        def runner = createRunner("StandaloneTest.groovy")
        runner.runTests()

        runner.tests[0].hasError().should == true
        runner.tests[0].isFailed().should == false
        runner.tests[0].exception.message.should == "error on purpose"

        runner.tests[1].hasError().should == false
        runner.tests[1].isFailed().should == true
        runner.tests[1].assertionMessage.should == "\nmismatches:\n" +
                "\n" +
                "[value]:   actual: 10 <java.lang.Integer>\n" +
                "         expected: 11 <java.lang.Integer>"

        runner.tests[2].hasError().should == false
        runner.tests[2].isFailed().should == false
    }

    @Test
    void "should extract failed code snippets"() {
        def runner = createRunner("StandaloneTest.groovy")
        runner.runTests()
        def failedSnippets = runner.tests[0].test.toMap().failedCodeSnippets
        def firstSnippet = failedSnippets[0]

        firstSnippet.filePath.should == 'StandaloneTest.groovy'
        firstSnippet.lineNumbers.should == [20]
        firstSnippet.snippet.should contain('throw new RuntimeException("error on purpose")')
    }

    @Test
    void "should skip the rest of scenarios if terminate scenario is thrown"() {
        def runner = createRunner("withTermination.groovy")
        runner.runTests()

        runner.tests[0].hasError().should == true
        runner.tests[0].isFailed().should == false

        runner.tests.skipped.should == [false, true, true]
    }

    @Test
    void "should skip disabled tests with dscenario"() {
        def runner = createRunner("withDisabled.groovy")
        runner.tests.scenario.should == ['disabled test one', 'enabled test', 'disabled test two']
        runner.runTests()

        runner.tests.skipped.should == [true, false, true]
        runner.tests.disableReason.should == ['disabled with dscenario', null, 'disabled with dscenario']
    }

    @Test
    void "should skip disabled tests with onlyWhen"() {
        def runner = createRunner("withDisabledOnCondition.groovy")
        runner.tests.scenario.should == ['conditioned scenario one', 'conditioned scenario two', 'scenario three']
        runner.runTests()

        runner.tests.skipped.should == [true, true, false]
        runner.tests.disableReason.should == ['will never happen', 'will never happen', null]
    }


    @Test
    void "should report test file run failure due to exceptions"() {
        def runner = createRunner("withExceptionOutside.groovy")
        runner.runTests()
        assertInitFailed(runner, 'init and in general code should not be called outside')
    }

    @Test
    void "should report test file run failure due to parsing error"() {
        def runner = createRunner("withParsingError.groovy")
        runner.runTests()
        assertInitFailed(runner, 'No such property: helloWorld for class: withParsingError')
    }

    @Test
    void "should forbid test steps outside of scenario"() {
        def runner = createRunner("withTestStepOutsideScenario.groovy")
        runner.runTests()
        assertInitFailed(runner, 'executing <running errand> outside of scenario is not supported')
    }

    @Test
    void "should not notify test listeners during parsing phase"() {
        def trackingListener = new TracingTestListener()

        withTestListener(trackingListener) {
            createRunner("StandaloneTest.groovy")
        }

        trackingListener.calls.should == []
    }

    @Test
    void "should use requested thread count if smaller than number of files"() {
        def runner = createRunner("StandaloneTest.groovy", "withDisabled.groovy")
        runner.numThreadsToUse(1).should == 1
    }

    @Test
    void "should use number of files if smaller than requested thread count"() {
        def runner = createRunner("StandaloneTest.groovy", "withDisabled.groovy")
        runner.numThreadsToUse(3).should == 2
    }

    @Test
    void "should use number of files if requested -1 threads"() {
        def runner = createRunner("StandaloneTest.groovy", "withDisabled.groovy")
        runner.numThreadsToUse(-1).should == 2
    }

    @Test
    void "should create beforeFirstTest handlers as a setup first test if there are actions"() {
        def listener = new TestListener() {
            @Override
            void beforeFirstTest() {
                TestStep.createAndExecuteStep(TokenizedMessage.tokenizedMessage(none("test step")),
                        { -> TokenizedMessage.tokenizedMessage(none("complete test step")) }) {
                    println "dummy step"
                }
            }
        }

        withTestListener(listener) {
            def runner = createRunner("StandaloneTest.groovy")
            runner.runTests()

            runner.webTauTestList.size().should == 4

            def beforeFirstTest = runner.webTauTestList.get(0)
            beforeFirstTest.scenario.should == 'before first test'
            beforeFirstTest.shortContainerId.should == 'Setup'
        }
    }

    @Test
    void "should not create beforeFirstTest handlers as a setup first test if there are no actions"() {
        def listener = new TestListener() {
            @Override
            void beforeFirstTest() {
                println "dummy step"
            }
        }

        withTestListener(listener) {
            def runner = createRunner("StandaloneTest.groovy")
            runner.runTests()

            runner.webTauTestList.size().should == 3
        }
    }

    @Test
    void "should create beforeFirstTest handler as a setup first test if there was an exception"() {
        def listener = new TestListener() {
            @Override
            void beforeFirstTest() {
                throw new RuntimeException("test error message")
            }
        }

        withTestListener(listener) {
            def runner = createRunner("StandaloneTest.groovy")
            runner.runTests()

            runner.webTauTestList.size().should == 4

            def beforeFirstTest = runner.webTauTestList.get(0)
            beforeFirstTest.scenario.should == 'before first test'
            beforeFirstTest.shortContainerId.should == 'Setup'
            beforeFirstTest.exception.message.should == 'test error message'
        }
    }

    @Test
    void "should create afterAll handlers as a tear down last test if there are actions"() {
        def listener = new TestListener() {
            @Override
            void afterAllTests() {
                TestStep.createAndExecuteStep(TokenizedMessage.tokenizedMessage(none("test step")),
                        { -> TokenizedMessage.tokenizedMessage(none("complete test step")) }) {
                    println "dummy step"
                }
            }
        }

        withTestListener(listener) {
            def runner = createRunner("StandaloneTest.groovy")
            runner.runTests()

            runner.webTauTestList.size().should == 4

            def beforeFirstTest = runner.webTauTestList.get(3)
            beforeFirstTest.scenario.should == 'after all tests'
            beforeFirstTest.shortContainerId.should == 'Teardown'
        }
    }

    @Test
    void "should not create afterAll handlers as a tear down last test if there are no actions"() {
        def listener = new TestListener() {
            @Override
            void afterAllTests() {
                println "dummy step"
            }
        }

        withTestListener(listener) {
            def runner = createRunner("StandaloneTest.groovy")
            runner.runTests()

            runner.webTauTestList.size().should == 3
        }
    }

    @Test
    void "should create afterAll handlers as a tear down last test if there was an exception"() {
        def listener = new TestListener() {
            @Override
            void afterAllTests() {
                throw new RuntimeException("test error message")
            }
        }

        withTestListener(listener) {
            def runner = createRunner("StandaloneTest.groovy")
            runner.runTests()

            runner.webTauTestList.size().should == 4

            def beforeFirstTest = runner.webTauTestList.get(3)
            beforeFirstTest.scenario.should == 'after all tests'
            beforeFirstTest.shortContainerId.should == 'Teardown'
            beforeFirstTest.exception.message.should == 'test error message'
        }
    }

    private static void withTestListener(TestListener listener, Closure code) {
        TestListeners.add(listener)
        try {
            code()
        } finally {
            TestListeners.remove(listener)
        }
    }

    private static void assertInitFailed(StandaloneTestRunner runner, String message) {
        runner.tests.size().should == 1

        def test = runner.tests[0]
        test.scenario.should == 'parse/init'
        test.test.isErrored().should == true
        test.test.exception.message.should == message
    }

    private static StandaloneTestRunner createRunner(String... scenarioFiles) {
        def workingDir = Paths.get("test-scripts")
        def runner = new StandaloneTestRunner(GroovyStandaloneEngine.createWithDelegatingEnabled(workingDir, []), workingDir)
        scenarioFiles.each { runner.process(new TestFile(Paths.get(it)), this) }

        return runner
    }
}
