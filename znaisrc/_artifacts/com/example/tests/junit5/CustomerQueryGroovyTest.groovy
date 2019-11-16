package com.example.tests.junit5

import com.twosigma.webtau.junit5.WebTau
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

import static com.twosigma.webtau.WebTauDsl.http

@WebTau
@DisplayName("customer query")
class CustomerQueryGroovyTest {
    private static def id1 // keep track of created ids to assert and cleanup later
    private static def id2
    private static def id3

    @BeforeAll
    @DisplayName("create customers")
    static void createCustomers() {
        id1 = createCustomer("CQ_FN1", "CQ_LN1")
        id2 = createCustomer("CQ_FN1", "CQ_LN2")
        id3 = createCustomer("CQ_FN2", "CQ_LN2")
    }

    @Test
    @DisplayName("query by first name")
    void queryByFirstName() {
        http.get("/customers/search/first-name", [name: "CQ_FN1"]) {
            _embedded.customers.should == ["*id" | "firstName" | "lastName"] { // star(*) marks key column so assertion is order agnostic
                                          __________________________________
                                            id1  | "CQ_FN1"    | "CQ_LN1"
                                            id2  | "CQ_FN1"    | "CQ_LN2" }
        }
    }

    @Test
    @DisplayName("query by last name")
    void queryByLastName() {
        http.get("/customers/search/last-name", [name: "CQ_LN2"]) {
            _embedded.customers.should == ["*id" | "firstName" | "lastName"] {
                                          __________________________________
                                            id2  | "CQ_FN1"    | "CQ_LN2"
                                            id3  | "CQ_FN2"    | "CQ_LN2" }
        }
    }

    @AfterAll
    @DisplayName("clean up")
    static void cleanup() {
        [id1, id2, id3].each { http.delete("/customers/$it") }
    }

    private static def createCustomer(String firstName, String lastName) {
        return http.post("/customers", [firstName: firstName, lastName: lastName]) { id }
    }
}
