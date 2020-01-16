package scenarios.concept

import static com.twosigma.webtau.WebTauGroovyDsl.*

metadata([owner: "team A"]) // setting owner for all the scenarios below

scenario("one") {
}

scenario("two") {
}

scenario("three") {
    metadata([owner: "team B"]) // owner is overridden for scenario "three"
}

scenario("four") {
}