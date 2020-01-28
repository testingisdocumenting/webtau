package com.example.tests.junit5;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Stream;

import static com.twosigma.webtau.WebTauDsl.*;

@DisplayName("customer query")
class CustomerQueryJavaTest {
    private static Integer id1; // keep track of created ids to assert and cleanup later
    private static Integer id2;
    private static Integer id3;

    @BeforeAll
    @DisplayName("create customers")
    static void createCustomers() {
        id1 = createCustomer("CQ_FN1", "CQ_LN1");
        id2 = createCustomer("CQ_FN1", "CQ_LN2");
        id3 = createCustomer("CQ_FN2", "CQ_LN2");
    }

    @Test
    @DisplayName("query by first name")
    void queryByFirstName() {
        http.get("/customers/search/first-name", http.query("name", "CQ_FN1"), (header, body) -> {
            body.should(equal(table("*id", "firstName", "lastName", // star(*) marks key column so assertion is order agnostic
                                   ________________________________,
                                      id1, "CQ_FN1"   , "CQ_LN1",
                                      id2, "CQ_FN1"   , "CQ_LN2")));
        });
    }

    @Test
    @DisplayName("query by last name")
    void queryByLastName() {
        http.get("/customers/search/last-name", http.query("name", "CQ_LN2"), (header, body) -> {
            body.should(equal(table("*id", "firstName", "lastName",
                                    ________________________________,
                                      id2, "CQ_FN1"   , "CQ_LN2",
                                      id3, "CQ_FN2"   , "CQ_LN2")));
        });
    }

    @AfterAll
    @DisplayName("clean up")
    static void cleanup() {
        Stream.of(id1, id2, id3).forEach(id -> http.delete("/customers/" + id));
    }

    private static int createCustomer(String firstName, String lastName) {
        Map<String, Object> payload = aMapOf(
                "firstName", firstName,
                "lastName", lastName);

        return http.post("/customers", payload, ((header, body) -> {
            return body.get("id");
        }));
    }
}
