package com.example.tests.junit5;

import org.junit.jupiter.api.Test;
import org.testingisdocumenting.webtau.junit5.WebTau;
import static org.testingisdocumenting.webtau.WebTauDsl.*; // convenient single import for all things webtau

@WebTau // annotation required for reports generation
public class JUnit5ExampleJavaTest {
    @Test
    public void myTest() {
    }
}
