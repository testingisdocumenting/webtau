package com.example.tests.junit4

import org.testingisdocumenting.webtau.junit4.WebTauRunner
import org.junit.Test
import org.junit.runner.RunWith

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.* // convenient single import for DSL methods and props like http

@RunWith(WebTauRunner)  // runner is required to have this test to be a part of generated html report
class CustomerCrudSingleGroovyTest {
    private def customerPayload = [firstName: 'FN',
                                   lastName: 'LN']
    private def changedCustomerPayload = [*:customerPayload, lastName: 'NLN']

    @Test
    void crud() {
        def id = http.post("/customers", customerPayload) {
            return id
        }

        http.get("/customers/$id") {
            body.should == customerPayload
        }

        http.put("/customers/$id", changedCustomerPayload) {
            body.should == changedCustomerPayload
        }

        http.get("/customers/$id") {
            body.should == changedCustomerPayload
        }

        http.delete("/customers/$id") {
            statusCode.should == 204
        }

        http.get("/customers/$id") {
            statusCode.should == 404
        }
    }
}
