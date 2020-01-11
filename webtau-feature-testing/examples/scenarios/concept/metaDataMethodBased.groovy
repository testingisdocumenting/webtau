package scenarios.concept

import static com.twosigma.webtau.WebTauGroovyDsl.*

Support.teamA()

scenario("one") {
}

scenario("two") {
}

scenario("three") {
    Support.teamB()
}