OpenApi.requestOnlyValidation() {
    http.post("/employee", [firstName: 'First', lastName: 'Second']) {
        // ...
    }
}