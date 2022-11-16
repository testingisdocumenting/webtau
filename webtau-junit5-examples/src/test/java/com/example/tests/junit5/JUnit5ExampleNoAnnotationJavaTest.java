package com.example.tests.junit5;

import org.junit.jupiter.api.Test;

import static org.testingisdocumenting.webtau.Matchers.*;

public class JUnit5ExampleNoAnnotationJavaTest {
    @Test
    public void myTest() {
        Integer number = 2;
        actual(number).should(equal(2));
    }
}
