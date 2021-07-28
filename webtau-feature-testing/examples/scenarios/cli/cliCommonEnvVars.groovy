package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("foreground process var from config") {
    cli.run('scripts/hello-env-var') {
        output.should == 'hello webtau'
    }
}

scenario("background process var from config") {
    def command = cli.runInBackground('scripts/hello-env-var')
    command.output.waitTo contain('hello webtau')
    command.stop()
}

