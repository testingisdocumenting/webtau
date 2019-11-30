package com.example.tests.junit4

import com.twosigma.webtau.junit4.WebTauRunner
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

import static com.twosigma.webtau.WebTauDsl.http

@RunWith(WebTauRunner.class)
class CustomerQueryGroovyTest {
    private static def id1 // keep track of created ids to assert and cleanup later
    private static def id2
    private static def id3

    @BeforeClass
    static void createCustomers() {
        id1 = createCustomer("CQ_FN1", "CQ_LN1")
        id2 = createCustomer("CQ_FN1", "CQ_LN2")
        id3 = createCustomer("CQ_FN2", "CQ_LN2")
    }

    @Test
    void queryByFirstName() {
        http.get("/customers/search/first-name", [name: "CQ_FN1"]) {
            content.should == ["*id" | "firstName" | "lastName"] { // star(*) marks key column so assertion is order agnostic
                              __________________________________
                                id1  | "CQ_FN1"    | "CQ_LN1"
                                id2  | "CQ_FN1"    | "CQ_LN2" }
        }
    }

    @Test
    void queryByLastName() {
        http.get("/customers/search/last-name", [name: "CQ_LN2"]) {
           content.should == ["*id" | "firstName" | "lastName"] {
                             __________________________________
                               id2  | "CQ_FN1"    | "CQ_LN2"
                               id3  | "CQ_FN2"    | "CQ_LN2" }
        }
    }

    @AfterClass
    static void cleanup() {
        [id1, id2, id3].each { http.delete("/customers/$it") }
    }

    private static def createCustomer(String firstName, String lastName) {
        return http.post("/customers", [firstName: firstName, lastName: lastName]) { id }
    }
}
