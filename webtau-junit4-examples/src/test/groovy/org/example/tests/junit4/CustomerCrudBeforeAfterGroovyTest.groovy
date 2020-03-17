package com.example.tests.junit4

import org.testingisdocumenting.webtau.junit4.WebTauRunner
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import static org.testingisdocumenting.webtau.WebTauGroovyDsl.*

@RunWith(WebTauRunner.class)
class CustomerCrudBeforeAfterGroovyTest {
    private def customerPayload = [firstName: 'FN',
                                   lastName: 'LN']
    private def changedCustomerPayload = [*:customerPayload, lastName: 'NLN']

    private int customerId

    @Before
    void "create customer"() {
        customerId = http.post("/customers", customerPayload) {
            return id // We deliberately named field as "customerId" to avoid conflict with response field. Alternatively you can use body.id to avoid the conflict
        }
    }

    @Test
    void "query customer"() {
        http.get("/customers/$customerId") {
            body.should == customerPayload
        }
    }

    @Test
    void "update customer"() {
        http.put("/customers/$customerId", changedCustomerPayload) {
            body.should == changedCustomerPayload
        }

        http.get("/customers/$customerId") {
            body.should == changedCustomerPayload
        }
    }

    @After
    void "delete customer"() {
        http.delete("/customers/$customerId") {
            statusCode.should == 204
        }

        http.get("/customers/$customerId") {
            statusCode.should == 404
        }
    }
}
