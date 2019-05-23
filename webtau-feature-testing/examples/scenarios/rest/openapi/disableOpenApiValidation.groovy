package scenarios.rest.openapi

import com.twosigma.webtau.openapi.OpenApi
import static com.twosigma.webtau.WebTauGroovyDsl.*

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
