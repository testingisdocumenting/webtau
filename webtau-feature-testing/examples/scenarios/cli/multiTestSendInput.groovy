package scenarios.cli

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

def helloWorld = createLazyResource { cli.runInBackground("scripts/multi-step-hello-world") }

scenario("send name") {
    helloWorld.output.waitTo contain("enter your name")

    helloWorld << "webtau\n"
    helloWorld.output.waitTo contain("hello webtau")
}

scenario("send lang") {
    helloWorld.output.waitTo contain("enter your language")

    helloWorld << "groovy\n"
    helloWorld.output.waitTo contain("nice choice of groovy")
}

scenario("send hobby") {
    helloWorld.output.waitTo contain("enter your hobby")

    helloWorld << "waiting\n"
    helloWorld.output.waitTo contain("perfect hobby: waiting")
}

scenario("stop") {
    helloWorld.stop()
}
