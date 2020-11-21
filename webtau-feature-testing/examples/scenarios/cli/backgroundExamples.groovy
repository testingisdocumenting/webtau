package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("run in background") {
    cli.runInBackground('echo hello && echo world')
    println "process above may still be running"
}

scenario("run and stop") {
    def command = cli.runInBackground('echo hello && sleep 10 && echo world')
    println "some commands that assume running process"

    command.stop()
}
