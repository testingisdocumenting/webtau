package com.example.tests.junit5

import org.junit.jupiter.api.TestFactory

class DynamicTestsGroovyTest {
    @TestFactory
    def priceCalculator() {
        ["price", "quantity", "outcome"] {
        _________________________________
          10     |  30      |  300
          -10    |  30      | -300
        }.test {
            PriceCalculator.calculate(price, quantity).should == outcome
        }
    }
}