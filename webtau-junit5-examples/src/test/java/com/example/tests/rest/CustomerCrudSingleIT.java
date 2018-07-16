package com.example.tests.rest;

import com.twosigma.webtau.junit5.WebTau;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.twosigma.webtau.WebTauDsl.equal;
import static com.twosigma.webtau.WebTauDsl.http;

@WebTau
public class CustomerCrudSingleIT {
    private Map<String, Object> customerPayload = createCustomerPayload();
    private Map<String, Object> changedCustomerPayload = createChangedCustomerPayload();

    @Test
    public void crud() {
        int id = http.post("/customers", customerPayload, ((header, body) -> {
            return body.get("id").get();
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
            header.statusCode().should(equal(204));
        }));

        http.get("/customers/" + id, ((header, body) -> {
            header.statusCode().should(equal(404));
        }));
    }

    private Map<String, Object> createCustomerPayload() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("firstName", "FN");
        payload.put("lastName", "LN");

        return payload;
    }

    private Map<String, Object> createChangedCustomerPayload() {
        Map<String, Object> payload = createCustomerPayload();
        payload.put("lastName", "NLN");

        return payload;
    }
}
