package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("wait for output") {
    def command = cli.runInBackground("scripts/sleeps")
    command.output.waitTo contain("line two")

    command.output.should contain("line one")

    command.stop()
}

scenario("wait for output with local timeout") {
    def command = cli.runInBackground("scripts/sleeps")
    // local-timeout
    command.output.waitTo(contain("line two"), 20_000)
    // local-timeout

    command.stop()
}