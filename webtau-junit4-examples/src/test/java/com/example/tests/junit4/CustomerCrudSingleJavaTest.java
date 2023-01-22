package com.example.tests.junit4;

import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.junit4.WebTauRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.testingisdocumenting.webtau.WebTauDsl.*; // convenient single import for DSL methods and props like http and equal, mapOf, etc

@RunWith(WebTauRunner.class) // runner is required to have this test to be a part of generated html report
public class CustomerCrudSingleJavaTest {
    private final HttpRequestBody customerPayload = http.json(
            "firstName", "FN",
            "lastName", "LN");

    private final HttpRequestBody changedCustomerPayload = http.json(
            "firstName", "FN",
            "lastName", "NLN");

    @Test
    public void crud() {
        int id = http.post("/customers", customerPayload, ((header, body) -> {
            return body.get("id");
        }));

        http.get("/customers/" + id, ((header, body) -> {
            body.should(equal(customerPayload));
        }));

        http.put("/customers/" + id, changedCustomerPayload, ((header, body) -> {
            body.should(equal(changedCustomerPayload));
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
