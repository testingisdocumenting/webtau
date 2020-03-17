package com.example.tests.junit5

import com.twosigma.webtau.junit5.WebTau
import org.junit.jupiter.api.*

import static com.twosigma.webtau.WebTauGroovyDsl.*

@WebTau
@TestMethodOrder(MethodOrderer.OrderAnnotation) // forcing methods execution order
@DisplayName("customer CRUD")
class CustomerCrudSeparatedGroovyTest {
    private static def customerPayload = [firstName: 'FN',
                                          lastName : 'LN']
    private static def changedCustomerPayload = [*: customerPayload, lastName: 'NLN']

    private static def id

    @BeforeAll
    @DisplayName("create customer") // optional friendly name for reporting purposes
    static void createCustomer() {
        id = http.post("/customers", customerPayload) {
            body.id // using body prefix is required in this case as id conflicts with class field name
        }

        id.shouldNot == 0
    }

    @Test
    @Order(1)
    @DisplayName("read customer")
    void read() {
        http.get("/customers/$id") {
            body.should == customerPayload
        }
    }

    @Test
    @Order(2) // order dependence saves from creating customer on every test
    @DisplayName("update customer")
    void update() {
        http.put("/customers/$id", changedCustomerPayload) {
            body.should == changedCustomerPayload
        }

        http.get("/customers/$id") {
            body.should == changedCustomerPayload
        }
    }

    @Test
    @Order(3) // but you can still run each method independently
    @DisplayName("delete customer")
    void delete() {
        http.delete("/customers/$id") {
            header.statusCode.should == 204
        }

        http.get("/customers/$id") {
            header.statusCode.should == 404
        }

        id = -1 // marking as deleted to let cleanup step know that no delete is required
    }

    @AfterAll
    static void cleanup() { // optional (since we create new ids all the time) step to keep your environment clean
        if (id == -1) {
            return
        }

        http.delete("/customers/$id")
    }
}
