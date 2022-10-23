package scenarios.concept

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("code to execute after the test") {
    defer {
        trace "execute at the end of the test", [key: "value"]
    }

    step("important action") {
        throw new RuntimeException("error")
    }
}

scenario("deferred action error") {
    defer {
        throw new RuntimeException("error in defer")
    }

    step("important action") {
        throw new RuntimeException("error")
    }
}
