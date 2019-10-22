package com.example.tests.junit5

import com.twosigma.webtau.junit5.WebTau
import org.junit.jupiter.api.*

import static com.twosigma.webtau.WebTauGroovyDsl.*

@WebTau
@TestMethodOrder(MethodOrderer.OrderAnnotation) // forcing methods execution order
@DisplayName("customer")
class CustomerCrudSeparatedGroovyTest {
    private static def customerPayload = [firstName: 'FN',
                                          lastName : 'LN']
    private static def changedCustomerPayload = [*: customerPayload, lastName: 'NLN']

    private static def id

    @BeforeAll
    static void createCustomer() {
        id = http.post("/customers", customerPayload) {
            body.id // using body prefix is required in this case as id conflicts with class field name
        }

        id.shouldNot == 0
    }

    @Test
    @Order(1)
    void read() {
        http.get("/customers/$id") {
            body.should == customerPayload
        }
    }

    @Test
    @Order(2) // order dependence saves from creating customer on every test
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
    void delete() {
        http.delete("/customers/$id") {
            header.statusCode.should == 204
        }
        id = -1 // marking as deleted to let cleanup step know that no delete is required

        http.get("/customers/$id") {
            header.statusCode.should == 404
        }
    }

    @AfterAll
    static void cleanup() { // optional (since we create new ids all the time) step to keep your environment clean
        if (id == -1) {
            return
        }

        http.delete("/customers/$id")
    }
}
