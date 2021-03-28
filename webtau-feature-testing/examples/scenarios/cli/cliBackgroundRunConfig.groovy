package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("working dir") {
    def command = cli.runInBackground('./listing', cli.workingDir('../scripts'))
    command.output.waitTo contain('listing files')
    command.output.waitTo contain('sleeps')
    command.stop()
}

scenario("environment var") {
    def command = cli.runInBackground('scripts/hello-env-var', cli.env([my_var: 'webtau']))
    command.output.waitTo contain('hello webtau')
    command.stop()
}

scenario("env var and working dir") {
    def command = cli.runInBackground('./hello-env-var',
            cli.workingDir('../scripts').env([my_var: 'webtau']))
    command.output.waitTo contain('hello webtau')
    command.stop()
}
