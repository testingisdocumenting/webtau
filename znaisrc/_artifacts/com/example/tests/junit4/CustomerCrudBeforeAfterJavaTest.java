package com.example.tests.junit4;

import com.twosigma.webtau.junit4.WebTauRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static com.twosigma.webtau.WebTauDsl.*;

@RunWith(WebTauRunner.class)
public class CustomerCrudBeforeAfterJavaTest {
    private Map<String, Object> customerPayload = createCustomerPayload();
    private Map<String, Object> changedCustomerPayload = createChangedCustomerPayload();
    private int customerId;

    @Before
    public void createCustomer() {
        customerId = http.post("/customers", customerPayload, ((header, body) -> {
            return body.get("id");
        }));
    }

    @Test
    public void queryCustomer() {
        http.get("/customers/" + customerId, ((header, body) -> {
            body.should(equal(customerPayload));
        }));
    }

    @Test
    public void updateCustomer() {
        http.put("/customers/" + customerId, changedCustomerPayload, ((header, body) -> {
            body.should(equal(changedCustomerPayload));
        }));

        http.get("/customers/" + customerId, ((header, body) -> {
            body.should(equal(changedCustomerPayload));
        }));
    }

    @After
    public void deleteCustomer() {
        http.delete("/customers/" + customerId, (header, body) -> {
            header.statusCode().should(equal(204));
        });

        http.get("/customers/" + customerId, ((header, body) -> {
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
