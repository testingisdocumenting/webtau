package com.example.tests.junit5;

import org.junit.jupiter.api.Test;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.junit5.WebTau;

import static org.testingisdocumenting.webtau.WebTauDsl.*;

@WebTau
public class CustomerDocCaptureTest {
    @Test
    public void extractIdAfterPostToUseInsideGetRequest() {
        HttpRequestBody customerPayload = http.json(
                "firstName", "FN",
                "lastName", "LN");

        int id = http.post("/customers", customerPayload, ((header, body) -> {
            body.get("id").shouldNot(equal(""));
            return body.get("id");
        }));
        http.doc.capture("employee-post"); // capture previous HTTP call into <docDir>/employee-post

        http.get("/customers/" + id, ((header, body) -> {
            body.get("firstName").should(equal("FN"));
            body.get("lastName").should(equal("LN"));
        }));
        http.doc.capture("employee-get"); // capture previous HTTP call into <docDir>/employee-get
    }
}
