http.get("/weather") {
    body.should complyWithSchema('valid-schema.json')
}