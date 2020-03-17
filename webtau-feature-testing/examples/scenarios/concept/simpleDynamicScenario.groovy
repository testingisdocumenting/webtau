package scenarios.concept

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

[1, 2, 3].each { number ->
    scenario("number $number") {
        println number
    }
}
