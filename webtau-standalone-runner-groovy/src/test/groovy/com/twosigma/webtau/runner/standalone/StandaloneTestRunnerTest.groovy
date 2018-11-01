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

package com.twosigma.webtau.runner.standalone

import org.junit.Test

import java.nio.file.Paths

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
        runner.tests[1].assertionMessage.should == "\ndoesn't equal 11\nmismatches:\n" +
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
        def failedSnippets = runner.tests[0].reportTestEntry.toMap().failedCodeSnippets
        def firstSnippet = failedSnippets[0]

        firstSnippet.filePath.should == 'StandaloneTest.groovy'
        firstSnippet.lineNumbers.should == [20]
        firstSnippet.snippet.contains('throw new RuntimeException("error on purpose")').should == true // TODO contain matcher
    }

    @Test
    void "should skip the rest of scenarios if terminate scenario is thrown"() {
        def runner = createRunner("withTermination.groovy")
        runner.runTests()

        runner.tests[0].hasError().should == true
        runner.tests[0].isFailed().should == false

        runner.tests.skipped.should == [false, true, true]
    }

    private static StandaloneTestRunner createRunner(String scenarioFile) {
        def workingDir = Paths.get("test-scripts")
        def runner = new StandaloneTestRunner(GroovyStandaloneEngine.createWithDelegatingEnabled(workingDir, []), workingDir)
        runner.process(Paths.get(scenarioFile), this)

        return runner
    }
}
