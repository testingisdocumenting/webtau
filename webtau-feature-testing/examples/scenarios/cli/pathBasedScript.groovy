package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("cli run foreground using path") {
    cli.run('path-simple') {
        output.should contain('version:')
    }
}

scenario("cli run foreground with args") {
    cli.run('path-simple foo "hello world"') {
        output.should contain('version:')
        output.should contain('foo')
        output.should contain('hello world')
    }
}

scenario("cli run background using path") {
    def command = cli.runInBackground('path-simple')
    command.output.waitTo contain('version:')
}

scenario("cli run background with args") {
    def command = cli.runInBackground('path-simple foo "hello world"')
    command.output.waitTo contain('version:')
    command.output.waitTo contain('foo')
    command.output.waitTo contain('hello world')
}
