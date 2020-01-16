package scenarios.concept

import static com.twosigma.webtau.WebTauGroovyDsl.*

metadata([owner: "team A"]) // setting owner for scenarios "one" and "two"

scenario("one") {
}

scenario("two") {
}

metadata([owner: "team B"]) // setting owner for scenarios "three" and "four"

scenario("three") {
}

scenario("four") {
}