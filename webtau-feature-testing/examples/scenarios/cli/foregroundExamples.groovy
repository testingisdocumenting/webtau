package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("command without validation") {
    cli.run('echo hello world')
}

scenario("command with output validation") {
    cli.run('echo hello world') {
        output.should contain('hello')
        output.should contain('world')
    }
}

scenario("command with error validation") {
    cli.run('>&2 echo error B892T') {
        error.should == 'error B892T'
    }
}

scenario("command with exit code validation") {
    cli.run('exit 8') {
        exitCode.should == 8
        exitCode.shouldNot == 0
    }
}

scenario("command implicit exit code check explicitly") {
    cli.run('echo hello world') {
        exitCode.should == 0
    }
}

scenario("command run result") {
    def result = cli.run('echo hello world')

    println result.output
    println result.error
    if (result.exitCode == 1) {
        // ...
    }
}