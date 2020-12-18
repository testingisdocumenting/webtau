package com.example.tests.junit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.testingisdocumenting.webtau.junit4.WebTauRunner;
import static org.testingisdocumenting.webtau.WebTauDsl.*; // convenient single import for all things webtau

@RunWith(WebTauRunner.class) // webtau runner to generate reports
public class JUnit4ExampleJavaTest {
    @Test
    public void myTest() {
    }
}
