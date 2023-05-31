package com.example.tests.junit5;

import org.junit.jupiter.api.Test;
import org.testingisdocumenting.webtau.junit5.WebTau;

import static org.testingisdocumenting.webtau.WebTauDsl.*;

@WebTau
public class CustomerCrudJavaTest {
    @Test
    public void crud() {
        var customerPayload = http.json( // new customer data
                "firstName", "FN",
                "lastName", "LN");

        int id = http.post("/customers", customerPayload, ((header, body) -> {
            return body.get("id"); // return id value from response body
        }));

        http.get("/customers/" + id, ((header, body) -> {
            body.should(equal(customerPayload)); // only specified properties will be asserted against
        }));

        String changedLastName = "NLN";
        var changedCustomerPayload = http.json(
                "firstName", "FN",
                "lastName", "NLN");

        http.put("/customers/" + id, changedCustomerPayload, ((header, body) -> {
            body.get("firstName").should(equal("FN"));
            body.get("lastName").should(equal(changedLastName));
        }));

        http.get("/customers/" + id, ((header, body) -> {
            body.should(equal(changedCustomerPayload));
        }));

        http.delete("/customers/" + id, ((header, body) -> {
            header.statusCode.should(equal(204));
        }));

        http.get("/customers/" + id, ((header, body) -> {
            header.statusCode.should(equal(404));
        }));
    }
}
