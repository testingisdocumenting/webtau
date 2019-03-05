package com.example.tests.junit5

import com.twosigma.webtau.junit5.WebTau
import org.junit.jupiter.api.TestFactory

@WebTau
class DynamicTestsGroovyTest {
    @TestFactory
    def read() {
        ["price", "quantity", "outcome"] {
        _________________________________
          10     |  30      |  300
          -10    |  30      | -300
        }.useCases {
            PriceCalculator.calculate(price, quantity).should == outcome
        }
    }
}