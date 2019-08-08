http.get("/city/London") {
    weather.should complyWithSchema('valid-schema.json')
}