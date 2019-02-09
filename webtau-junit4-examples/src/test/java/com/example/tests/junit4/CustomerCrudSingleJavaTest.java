package com.example.tests.junit4;

import com.twosigma.webtau.junit4.WebTauRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static com.twosigma.webtau.Ddjt.aMapOf;
import static com.twosigma.webtau.WebTauDsl.equal;
import static com.twosigma.webtau.WebTauDsl.http;

@RunWith(WebTauRunner.class)
public class CustomerCrudSingleJavaTest {
    private Map<String, Object> customerPayload = createCustomerPayload();
    private Map<String, Object> changedCustomerPayload = createChangedCustomerPayload();

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
            header.statusCode().should(equal(204));
        }));

        http.get("/customers/" + id, ((header, body) -> {
            header.statusCode().should(equal(404));
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
