package com.twosigma.webtau.expectation

import org.junit.Ignore
import org.junit.Test

class ExpectationExtensionTest {
    @Test
    void "provides should equal shortcut"() {
        def value = 12
        value.should == 12
    }
}
