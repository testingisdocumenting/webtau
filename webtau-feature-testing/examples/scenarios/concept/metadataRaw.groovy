package scenarios.concept

import static com.twosigma.webtau.WebTauGroovyDsl.*

metadata([owner: "team A"])

scenario("one") {
}

scenario("two") {
}

scenario("three") {
    metadata([owner: "team B"])
}