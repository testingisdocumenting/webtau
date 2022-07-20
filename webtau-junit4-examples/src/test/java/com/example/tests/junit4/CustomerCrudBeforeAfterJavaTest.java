package com.example.tests.junit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.junit4.WebTauRunner;

import static org.testingisdocumenting.webtau.WebTauDsl.*;

@RunWith(WebTauRunner.class)
public class CustomerCrudBeforeAfterJavaTest {
    private final HttpRequestBody customerPayload = http.json(
            "firstName", "FN",
            "lastName", "LN");

    private final HttpRequestBody changedCustomerPayload = http.json(
            "firstName", "FN",
            "lastName", "NLN");

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
            header.statusCode.should(equal(204));
        });

        http.get("/customers/" + customerId, ((header, body) -> {
            header.statusCode.should(equal(404));
        }));
    }
}
