package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*
import static personas.Personas.*

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


sscenario("persona foreground process var from config") {
    Alice {
        cli.run('scripts/hello-env-var') {
            output.should == 'hello Alice!'
        }
    }

    Bob {
        cli.run('scripts/hello-env-var') {
            output.should == 'hello Bob!'
        }
    }
}

scenario("persona background process var from config") {
    Bob {
        def command = cli.runInBackground('scripts/hello-env-var')
        command.output.waitTo contain('hello Bob!')
        command.stop()
    }

    Alice {
        def command = cli.runInBackground('scripts/hello-env-var')
        command.output.waitTo contain('hello Alice!')
        command.stop()
    }
}
