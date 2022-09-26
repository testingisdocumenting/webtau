package com.example.tests.junit5;

import org.junit.jupiter.api.Test;
import org.testingisdocumenting.webtau.junit5.WebTau;

import static com.example.tests.junit5.Personas.*;
import static org.testingisdocumenting.webtau.WebTauDsl.*;

@WebTau
public class PersonaHttpJavaTest {
    @Test
    public void checkBalance() {
        Alice.execute(() -> http.get("/statement", (header, body) -> {
            body.get("balance").shouldBe(greaterThan(100));
        }));

        Bob.execute(() -> http.get("/statement", (header, body) -> {
            body.get("balance").shouldBe(lessThan(50));
        }));
    }
}
