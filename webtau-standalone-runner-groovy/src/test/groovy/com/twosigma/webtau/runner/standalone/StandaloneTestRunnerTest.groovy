package com.twosigma.webtau.runner.standalone

import org.junit.Test

import java.nio.file.Paths

class StandaloneTestRunnerTest {
    private static StandaloneTestRunner runner = createRunner()

    @Test
    void "should register tests with scenario keyword"() {
        runner.tests.description.should == ['# first header\noptionally split into multiple lines and has a header\n',
                                            '# second header\noptionally split into multiple lines and has a header\n',
                                            '# third header\noptionally split into multiple lines and has a header\n']
    }

    @Test
    void "should mark test as failed, passed or errored"() {
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
        runner.runTests()
        def failedSnippets = runner.tests[0].toMap().failedCodeSnippets
        def firstSnippet = failedSnippets[0]

        firstSnippet.filePath.should == 'StandaloneTest.groovy'
        firstSnippet.lineNumbers.should == [4]
        firstSnippet.snippet.contains('throw new RuntimeException("error on purpose")').should == true // TODO contain matcher
    }

    private static StandaloneTestRunner createRunner() {
        def workingDir = Paths.get("test-scripts")
        def runner = new StandaloneTestRunner(GroovyStandaloneEngine.createWithDelegatingEnabled(workingDir, []), workingDir)
        runner.process(Paths.get("StandaloneTest.groovy"), this)

        return runner
    }
}
