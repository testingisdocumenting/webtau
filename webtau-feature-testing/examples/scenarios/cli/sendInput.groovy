package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("send input") {
    def helloWorld = cli.runInBackground("scripts/hello-world")
    helloWorld.output.waitTo contain("enter your name")

    helloWorld.send("webtau\n")
    helloWorld.output.waitTo contain("hello webtau")

    helloWorld.stop()
}

scenario("send input with shift left") {
    def helloWorld = cli.runInBackground("scripts/hello-world")
    helloWorld.output.waitTo contain("enter your name")

    helloWorld << "webtau\n"
    helloWorld.output.waitTo contain("hello webtau")

    helloWorld.stop()
}