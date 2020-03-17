package scenarios.concept

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

dscenario('do not execute this scenario') {
    http.get('/new-endpoint') {
        price.shouldBe > 0
    }
}

disabledScenario('do not execute this scenario either') {
    http.get('/new-endpoint') {
        price.shouldBe > 0
    }
}
