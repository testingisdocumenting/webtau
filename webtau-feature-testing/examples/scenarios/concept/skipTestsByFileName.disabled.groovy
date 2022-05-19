package scenarios.concept

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario('do not execute this scenario') {
    http.get('/non-existing-endpoint') {
        price.shouldBe > 0
    }
}

scenario('do not execute this scenario either') {
    http.get('/non-existing-endpoint') {
        price.shouldBe > 0
    }
}
