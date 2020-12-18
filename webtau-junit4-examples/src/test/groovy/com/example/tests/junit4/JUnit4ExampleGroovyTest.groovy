package com.example.tests.junit4

import org.junit.Test
import org.junit.runner.RunWith
import org.testingisdocumenting.webtau.junit4.WebTauRunner
import static org.testingisdocumenting.webtau.WebTauGroovyDsl.* // convenient single import for all things webtau

@RunWith(WebTauRunner.class) // webtau runner to generate reports
class JUnit4ExampleGroovyTest {
    @Test
    void "my test"() {
    }
}
