package com.example.tests.junit5;

import com.twosigma.webtau.junit5.WebTau;
import org.junit.jupiter.api.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.twosigma.webtau.WebTauDsl.*;

@WebTau
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // forcing methods execution order
@DisplayName("customer CRUD")
public class CustomerCrudSeparatedJavaTest {
    private static final Map<String, Object> customerPayload = createCustomerPayload();
    private static final Map<String, Object> changedCustomerPayload = createChangedCustomerPayload();

    private static int id;

    @BeforeAll
    @DisplayName("create customer") // optional friendly name for reporting purposes
    static void createCustomer() {
        id = http.post("/customers", customerPayload, ((header, body) -> {
            return body.get("id");
        }));

        actual(id).shouldNot(equal(0));
    }

    @Test
    @Order(1)
    @DisplayName("read customer")
    void read() {
        http.get("/customers/" + id, ((header, body) -> {
            body.should(equal(customerPayload));
        }));
    }

    @Test
    @Order(2) // order dependence saves from creating customer on every test
    @DisplayName("update customer")
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
    @DisplayName("delete customer")
    void delete() {
        http.delete("/customers/" + id, ((header, body) -> {
            header.statusCode().should(equal(204));
        }));

        http.get("/customers/" + id, ((header, body) -> {
            header.statusCode().should(equal(404));
        }));

        id = -1; // marking as deleted to let cleanup step know that no delete is required
    }

    @AfterAll
    static void cleanup() { // optional (since we create new ids all the time) step to keep your environment clean
        if (id == -1) {
            return;
        }

        http.delete("/customers/" + id);
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
