package scenarios.rest

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("check text message") {
    http.get("/text-message") {
        body.shouldBe == 'hello world'
    }
}