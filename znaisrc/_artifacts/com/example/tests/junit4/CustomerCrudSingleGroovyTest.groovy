package com.example.tests.junit4

import com.twosigma.webtau.junit4.WebTauRunner
import org.junit.Test
import org.junit.runner.RunWith

import static com.twosigma.webtau.WebTauDsl.*

@RunWith(WebTauRunner.class)
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
