package scenarios.rest

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("check weather") {
    def query = "{ weather { temperature } }";
    graphql.execute(query) {
        weather.temperature.shouldBe < 100
    }
}
