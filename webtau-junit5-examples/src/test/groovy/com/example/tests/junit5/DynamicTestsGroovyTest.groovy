package com.example.tests.junit5

import org.junit.jupiter.api.TestFactory

class DynamicTestsGroovyTest {
    @TestFactory
    def "individual tests use generated display labels"() {
        ["price", "quantity", "outcome"] {
        _________________________________
          10     |  30      |  300
          -10    |  30      | -300
        }.test {
            PriceCalculator.calculate(price, quantity).should == outcome
        }
    }

    @TestFactory
    def "individual tests can use an optional display label to clarify the use case"() {
        ["label",           "price", "quantity", "outcome"] {
        ___________________________________________________
          "positive price" | 10     |  30      |  300
          "negative price" | -10    |  30      | -300
          null             | 0      |  30      | 0
        }.test {
            PriceCalculator.calculate(price, quantity).should == outcome
        }
    }
}