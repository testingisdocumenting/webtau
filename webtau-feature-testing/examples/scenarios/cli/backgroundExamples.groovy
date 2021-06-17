package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("run in background") {
    cli.runInBackground('scripts/sleeps')
    println "process above may still be running"
}

scenario("run and stop") {
    def command = cli.runInBackground('scripts/sleeps')
    println "some commands that assume running process"

    command.stop()
}
