package scenarios.rest.headers

import static personas.Personas.*
import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("my bank balance") {
    Alice {
        http.get("/statement") {
            balance.shouldBe > 100
        }
    }

    Bob {
        http.get("/statement") {
            balance.shouldBe < 50
        }
    }
}