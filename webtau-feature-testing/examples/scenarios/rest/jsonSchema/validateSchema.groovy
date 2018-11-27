package scenarios.rest.jsonSchema

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario("valid schema") {
    http.get("/weather") {
        body.should complyWithSchema('valid-schema.json')
    }
}

scenario("invalid schema") {
    http.get("/weather") {
        body.should complyWithSchema('invalid-schema.json')
    }
}
