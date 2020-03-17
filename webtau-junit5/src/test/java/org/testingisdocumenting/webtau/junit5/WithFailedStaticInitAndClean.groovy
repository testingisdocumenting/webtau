package org.testingisdocumenting.webtau.junit5

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

@WebTau
class WithFailedStaticInitAndClean {
    @BeforeAll
    static void failingInit() {
        throw new RuntimeException("error on purpose")
    }

    @AfterAll
    static void failingCleanup() {
        throw new RuntimeException("error on purpose")
    }

    @Test
    void noOpOne() {
    }

    @Test
    void noOpTwo() {
    }
}
