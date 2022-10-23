package com.example.tests.junit5;

import org.junit.jupiter.api.Test;
import org.testingisdocumenting.webtau.junit5.WebTau;

import static org.testingisdocumenting.webtau.WebTauDsl.*;

@WebTau
public class DeferredBlockJavaTest {
    @Test
    public void deferredAction() {
        defer(() -> {
            trace("will always be executed at the end", "key", "value");
        });

        step("important action", () -> {
            throw new RuntimeException("error");
        });
    }

    @Test
    public void deferredActionError() {
        defer(() -> {
            throw new RuntimeException("error in defer");
        });

        step("important action", () -> {
            throw new RuntimeException("error");
        });
    }
}
