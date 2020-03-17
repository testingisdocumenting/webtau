package scenarios.rest.jsonSchema

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

scenario("valid schema") {
    http.get("/weather") {
        body.should complyWithSchema('valid-schema.json')
    }
}

scenario("validate specific field") {
    http.get("/city/London") {
        weather.should complyWithSchema('valid-schema.json')
    }
}

scenario("invalid schema") {
    http.get("/weather") {
        body.should complyWithSchema('invalid-schema.json')
    }
}
