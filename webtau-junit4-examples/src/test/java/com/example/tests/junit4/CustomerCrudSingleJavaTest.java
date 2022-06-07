package com.example.tests.junit4;

import org.testingisdocumenting.webtau.junit4.WebTauRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.testingisdocumenting.webtau.WebTauDsl.*; // convenient single import for DSL methods and props like http and equal, aMapOf, etc

@RunWith(WebTauRunner.class) // runner is required to have this test to be a part of generated html report
public class CustomerCrudSingleJavaTest {
    private final Map<String, Object> customerPayload = createCustomerPayload();
    private final Map<String, Object> changedCustomerPayload = createChangedCustomerPayload();

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

    private Map<String, Object> createCustomerPayload() {
        return aMapOf(
                "firstName", "FN",
                "lastName", "LN");
    }

    private Map<String, Object> createChangedCustomerPayload() {
        Map<String, Object> payload = createCustomerPayload();
        payload.put("lastName", "NLN");

        return payload;
    }
}
