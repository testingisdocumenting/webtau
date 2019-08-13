package com.example.tests.junit5

import com.twosigma.webtau.WebTauDsl.*
import com.twosigma.webtau.http.validation.HttpResponseValidatorWithReturn
import com.twosigma.webtau.junit5.WebTau

import org.junit.jupiter.api.*

@WebTau
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@DisplayName("customer")
class CustomerCrudSeparatedKotlinTest {
    @Test
    @Order(1)
    fun `read customer record`() {
        http.get("/customers/$id") { _, body ->
            body.should(equal(customerPayload))
        }
    }

    @Test
    @Order(2)
    fun `update customer record`() {
        http.put("/customers/$id", changedCustomerPayload) { _, body ->
            body.should(equal(changedCustomerPayload))
        }

        http.get("/customers/$id") { _, body ->
            body.should(equal(changedCustomerPayload))
        }
    }

    @Test
    @Order(3)
    fun `delete customer`() {
       http.delete("/customers/$id") { header, _ ->
           header.statusCode().should(equal(204))
       }

        http.get("/customers/$id") { header, _ ->
            header.statusCode().should(equal(404))
        }
    }

    companion object {
        private val customerPayload = mapOf(
            "firstName" to "FN",
            "lastName" to "LN"
        )
        private val changedCustomerPayload = mapOf(
            "lastName" to "NLN"
        )
        private val id by lazy {
            val id: Int = http.post("/customers", customerPayload, HttpResponseValidatorWithReturn { _, body ->
                body.get("id")
            })

            actual(id).shouldNot(equal(0))

            id
        }
    }
}
