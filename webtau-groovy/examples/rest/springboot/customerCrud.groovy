package rest.springboot

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario("CRUD operations for customer") {
    def customerPayload = [firstName: "FN", lastName: "LN"]

    int id = http.post("/customers", customerPayload) {
        return id
    }

    http.get("/customers/${id}") {
        body.should == customerPayload // only specified properties will be asserted against
    }

    def changedLastName = "NLN"
    http.put("/customers/${id}", [*:customerPayload, lastName: changedLastName]) {
        lastName.should == changedLastName // specifying body is optional
    }

    http.get("/customers/${id}") {
        lastName.should == changedLastName
    }

    http.delete("/customers/${id}") {
        statusCode.should == 204
    }

    http.get("/customers/${id}") {
        statusCode.should == 404
    }
}