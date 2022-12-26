package scenarios.ui

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("expect visible but hidden") {
    def feedback = $("#feedback")
    feedback.shouldBe visible
}
