package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("wait for output") {
    def command = cli.runInBackground("scripts/sleeps")
    command.output.waitTo contain("line two")

    command.output.should contain("line one")

    command.stop()
}