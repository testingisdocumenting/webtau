package scenarios.rest.openapi

import org.testingisdocumenting.webtau.openapi.OpenApi
import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("disable all validation") {
    OpenApi.withoutValidation {
        http.post("/employee", [firstName: 'First']) {
            // ...
        }
    }
}

scenario("disable request validation") {
    OpenApi.responseOnlyValidation() {
        http.post("/employee", [firstName: 'First']) {
            // ...
        }
    }
}

scenario("disable response validation") {
    OpenApi.requestOnlyValidation() {
        http.post("/employee", [firstName: 'First', lastName: 'Second']) {
            // ...
        }
    }
}
