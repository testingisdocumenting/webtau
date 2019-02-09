package com.example.tests.junit5;

import com.twosigma.webtau.junit5.WebTau;
import org.junit.jupiter.api.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.twosigma.webtau.WebTauDsl.*;

@WebTau
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // forcing methods execution order
@DisplayName("customer")
public class CustomerCrudSeparatedJavaTest {
    private static final Map<String, Object> customerPayload = createCustomerPayload();
    private static final Map<String, Object> changedCustomerPayload = createChangedCustomerPayload();

    private static int id;

    @BeforeAll
    static void createCustomer() {
        id = http.post("/customers", customerPayload, ((header, body) -> {
            return body.get("id");
        }));

        actual(id).shouldNot(equal(0));
    }

    @Test
    @Order(1)
    void read() {
        http.get("/customers/" + id, ((header, body) -> {
            body.should(equal(customerPayload));
        }));
    }

    @Test
    @Order(2) // order dependence saves from creating customer on every test
    void update() {
        http.put("/customers/" + id, changedCustomerPayload, ((header, body) -> {
            body.should(equal(changedCustomerPayload));
        }));

        http.get("/customers/" + id, ((header, body) -> {
            body.should(equal(changedCustomerPayload));
        }));
    }

    @Test
    @Order(3) // but you can still run each method independently
    void delete() {
        http.delete("/customers/" + id, ((header, body) -> {
            header.statusCode().should(equal(204));
        }));

        http.get("/customers/" + id, ((header, body) -> {
            header.statusCode().should(equal(404));
        }));
    }

    private static Map<String, Object> createCustomerPayload() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("firstName", "FN");
        payload.put("lastName", "LN");

        return payload;
    }

    private static Map<String, Object> createChangedCustomerPayload() {
        Map<String, Object> payload = createCustomerPayload();
        payload.put("lastName", "NLN");

        return payload;
    }
}
