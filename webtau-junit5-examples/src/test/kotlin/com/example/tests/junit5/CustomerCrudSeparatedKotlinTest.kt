package com.example.tests.junit5

//import com.example.kotlin.http.WebTauKotlinDsl.*
import com.example.kotlin.http.test
//import org.testingisdocumenting.webtau.WebTauDsl.*
import org.testingisdocumenting.webtau.junit5.WebTau

import com.example.kotlin.http.*

import org.junit.jupiter.api.*

@WebTau
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@DisplayName("customer")
class CustomerCrudSeparatedKotlinTest {
    companion object {
        private val customerPayload = mapOf(
            "firstName" to "FN",
            "lastName" to "LN"
        )

        private val changedCustomerPayload = mapOf(
            "lastName" to "NLN"
        )

        private var id: Int = 0
    }

//    private fun <R> post(url: String, payload: Map<String, Any>, validator: (header: HeaderDataNode, body: DataNode) -> Any): R {
//        return http.post(url, payload, HttpResponseValidatorWithReturn(validator))
//    }
//
//    private fun post(url: String, payload: Map<String, Any>, validator: (header: HeaderDataNode, body: DataNode) -> Unit) {
//        http.post(url, payload, HttpResponseValidator(validator))
//    }

    @Test
    fun testValidators() {
        test()
    }

    @Test
    @Order(1)
    fun `create customer`() {
        id = http.post("/customers", customerPayload) { _, body ->
            body.get("id")
        }

        http.post("/customers", customerPayload) { _, body ->
            body.get("id")
        }
//
//        http.post("/customers", customerPayload) { _, body ->
//
//        }
    }

//    @Test
//    @Order(2)
//    fun `read customer`() {
//        http.get("/customers/$id") { _, body ->
//            body.should(equal(customerPayload))
//        }
//    }
//
//    @Test
//    @Order(3)
//    fun `update customer`() {
//        http.put("/customers/$id", changedCustomerPayload) { _, body ->
//            body.should(equal(changedCustomerPayload))
//        }
//
//        http.get("/customers/$id") { _, body ->
//            body.should(equal(changedCustomerPayload))
//        }
//    }
//
//    @Test
//    @Order(4)
//    fun `delete customer`() {
//       http.delete("/customers/$id") { header, _ ->
//           header.statusCode.should(equal(204))
//       }
//
//        http.get("/customers/$id") { header, _ ->
//            header.statusCode.should(equal(404))
//        }
//    }
}
