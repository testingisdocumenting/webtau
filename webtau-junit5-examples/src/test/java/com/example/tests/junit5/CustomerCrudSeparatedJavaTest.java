package com.example.tests.junit5;

import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.junit5.WebTau;
import org.junit.jupiter.api.*;

import static org.testingisdocumenting.webtau.WebTauDsl.*;

@WebTau
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // forcing methods execution order
@DisplayName("customer CRUD")
public class CustomerCrudSeparatedJavaTest {
    private static final HttpRequestBody customerPayload = http.json(
            "firstName", "FN",
            "lastName", "LN");

    private static final HttpRequestBody changedCustomerPayload = http.json(
            "firstName", "FN",
            "lastName", "NLN");

    private static int id;

    @BeforeAll
    @DisplayName("create customer") // optional friendly name for reporting purposes
    public static void createCustomer() {
        id = http.post("/customers", customerPayload, ((header, body) -> {
            return body.get("id");
        }));

        actual(id).shouldNot(equal(0));
    }

    @Test
    @Order(1)
    @DisplayName("read customer")
    public void read() {
        http.get("/customers/" + id, ((header, body) -> {
            body.should(equal(customerPayload));
        }));
    }

    @Test
    @Order(2) // order dependence saves from creating customer on every test
    @DisplayName("update customer")
    public void update() {
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
    public void delete() {
        http.delete("/customers/" + id, ((header, body) -> {
            header.statusCode.should(equal(204));
        }));

        http.get("/customers/" + id, ((header, body) -> {
            header.statusCode.should(equal(404));
        }));

        id = -1; // marking as deleted to let cleanup step know that no delete is required
    }

    @AfterAll
    public static void cleanup() { // optional (since we create new ids all the time) step to keep your environment clean
        if (id == -1) {
            return;
        }

        http.delete("/customers/" + id);
    }
}
