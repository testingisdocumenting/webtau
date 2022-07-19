package com.example.tests.junit5;

import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.junit5.WebTau;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.testingisdocumenting.webtau.WebTauDsl.*;

@WebTau
public class CustomerCrudJavaTest {
    @Test
    public void crud() {
        HttpRequestBody customerPayload = http.json( // new customer data
                "firstName", "FN",
                "lastName", "LN" );

        int id = http.post("/customers", customerPayload, ((header, body) -> {
            return body.get("id"); // return id value from response body
        }));

        http.get("/customers/" + id, ((header, body) -> {
            body.should(equal(customerPayload)); // only specified properties will be asserted against
        }));

        String changedLastName = "NLN";
        Map<String, ?> changedCustomerPayload = aMapOf(
                "firstName", "FN",
                "lastName", changedLastName);

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
