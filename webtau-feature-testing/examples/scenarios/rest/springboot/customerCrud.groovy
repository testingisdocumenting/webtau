package scenarios.rest.springboot

import static com.twosigma.webtau.WebTauGroovyDsl.*

scenario("CRUD operations for customer") {
    def customerPayload = [firstName: "FN", lastName: "LN"]

    def id = http.post("/customers", customerPayload) {
        return id // return id value from response body
    }

    http.get("/customers/${id}") {
        body.should == customerPayload // only specified properties will be asserted against
    }

    def changedLastName = "NLN"
    http.put("/customers/${id}", [*:customerPayload, lastName: changedLastName]) {
        lastName.should == changedLastName // specifying body is optional
    }

    http.get("/customers/${id}") {
        firstName.should == "FN"
        lastName.should == changedLastName
    }

    def changedFirstName = "NFN"
    http.patch("/customers/${id}", [firstName: changedFirstName])

    http.get("/customers/${id}") {
        firstName.should == changedFirstName
        lastName.should == changedLastName
    }

    http.delete("/customers/${id}") {
        statusCode.should == 204
    }

    http.get("/customers/${id}") {
        statusCode.should == 404
    }
}