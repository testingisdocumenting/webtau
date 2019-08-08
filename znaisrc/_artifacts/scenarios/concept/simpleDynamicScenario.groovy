package scenarios.concept

import static com.twosigma.webtau.WebTauGroovyDsl.*

[1, 2, 3].each { number ->
    scenario("number $number") {
        println number
    }
}
