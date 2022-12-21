package com.example.tests.junit5


import org.junit.jupiter.api.TestFactory
import org.testingisdocumenting.webtau.junit5.WebTau

@WebTau
class DynamicTestsGroovyTest {
    @TestFactory
    def "individual tests use generated display labels"() {
        ["price" | "quantity" | "outcome"] {
        _________________________________
            10.0 |  30        |  300.0
           -10.0 |  30        | -300.0
        }.test {
            PriceCalculator.calculate(price, quantity).should == outcome
        }
    }

    @TestFactory
    def "individual tests label to clarify the use case"() {
        ["label"           | "price" | "quantity" | "outcome"] {
        _____________________________________________________
          "positive price" |  10.0   |  30        |  300.0
          "negative price" | -10.0   |  30        | -300.0
        }.test {
            PriceCalculator.calculate(price, quantity).should == outcome
        }
    }

    @TestFactory
    def "a null display label is treated as the String 'null'"() {
        ["label"           | "price" | "quantity" | "outcome"] {
        ___________________________________________________
          null             | 0       |  30        | 0
        }.test {
            PriceCalculator.calculate(price, quantity).should == outcome
        }
    }
}