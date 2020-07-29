/*
 * Copyright 2020 webtau maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.webtau.http

import org.junit.Test

import static org.testingisdocumenting.webtau.http.Http.http
import static org.testingisdocumenting.webtau.http.HttpOverloadsTestCommon.*

class HttpGroovyOverloadsTest extends HttpTestBase {
    @Test
    void "get without validation syntax check"() {
        http.get("/end-point")
        http.get("/end-point", [queryParam1: "queryParamValue1"])
        http.get("/end-point", [queryParam1: "queryParamValue1"], http.header("h1", "v1"))
        http.get("/end-point", http.header("h1", "v1"))
    }

    @Test
    void "put without return overloads"() {
        http.put("/full-echo", query, requestHeader, requestBodyMap) {
            headerValidation.accept(body)
            bodyAsMapValidation.accept(body)
            urlValidation.accept(body)
        }

        http.put("/full-echo", query, requestHeader, requestBodyList) {
            headerValidation.accept(body)
            bodyAsListValidation.accept(body)
            urlValidation.accept(body)
        }

        http.put("/full-echo", query, requestHeader, requestBody) {
            headerValidation.accept(body)
            bodyAsMapValidation.accept(body)
            urlValidation.accept(body)
        }

        http.put("/full-echo", query, requestHeader, requestBodyList) {
            headerValidation.accept(body)
            bodyAsListValidation.accept(body)
            urlValidation.accept(body)
        }

        http.put("/full-echo", query, requestBody) {
            bodyAsMapValidation.accept(body)
            urlValidation.accept(body)
        }

        http.put("/full-echo", query, requestBodyList) {
            bodyAsListValidation.accept(body)
            urlValidation.accept(body)
        }

        http.put("/full-echo", query, requestBodyMap) {
            bodyAsMapValidation.accept(body)
            urlValidation.accept(body)
        }

        http.put("/full-echo", query, requestBodyList) {
            bodyAsListValidation.accept(body)
            urlValidation.accept(body)
        }

        http.put("/full-echo", requestHeader, requestBodyMap) {
            headerValidation.accept(body)
            bodyAsMapValidation.accept(body)
            pathValidation.accept(body)
        }

        http.put("/full-echo", requestHeader, requestBodyList) {
            headerValidation.accept(body)
            bodyAsListValidation.accept(body)
            pathValidation.accept(body)
        }

        http.put("/full-echo", requestHeader, requestBody) {
            headerValidation.accept(body)
            bodyAsMapValidation.accept(body)
            pathValidation.accept(body)
        }

        http.put("/full-echo", query, requestHeader) {
            headerValidation.accept(body)
            urlValidation.accept(body)
        }

        http.put("/full-echo", query) {
            urlValidation.accept(body)
        }

        http.put("/full-echo", requestHeader) {
            headerValidation.accept(body)
            pathValidation.accept(body)
        }

        http.put("/full-echo", requestBody) {
            bodyAsMapValidation.accept(body)
            pathValidation.accept(body)
        }

        http.put("/full-echo") {
            pathValidation.accept(body)
        }
    }

    @Test
    void "patch with return overloads"() {
        def v
        v = http.patch("/full-echo", query, requestHeader, requestBodyMap) {
            headerValidation.accept(body)
            bodyAsMapValidation.accept(body)
            urlValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyMap

        v = http.patch("/full-echo", query, requestHeader, requestBodyList) {
            headerValidation.accept(body)
            bodyAsListValidation.accept(body)
            urlValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyList

        v = http.patch("/full-echo", query, requestHeader, requestBody) {
            headerValidation.accept(body)
            bodyAsMapValidation.accept(body)
            urlValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyMap

        http.patch("/full-echo", query, requestBody) {
            bodyAsMapValidation.accept(body)
            urlValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyMap

        v = http.patch("/full-echo", query, requestBodyMap) {
            bodyAsMapValidation.accept(body)
            urlValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyMap

        v = http.patch("/full-echo", query, requestBodyList) {
            bodyAsListValidation.accept(body)
            urlValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyList

        v = http.patch("/full-echo", requestHeader, requestBodyMap) {
            headerValidation.accept(body)
            bodyAsMapValidation.accept(body)
            pathValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyMap

        v = http.patch("/full-echo", requestHeader, requestBodyList) {
            headerValidation.accept(body)
            bodyAsListValidation.accept(body)
            pathValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyList

        v = http.patch("/full-echo", requestHeader, requestBody) {
            headerValidation.accept(body)
            bodyAsMapValidation.accept(body)
            pathValidation.accept(body)

            return body[HEADER_KEY]
        }
        v.should == HEADER_EXPECTED_RETURN

        v = http.patch("/full-echo", query, requestHeader) {
            headerValidation.accept(body)
            urlValidation.accept(body)

            return body[HEADER_KEY]
        }
        v.should == HEADER_EXPECTED_RETURN

        v = http.patch("/full-echo", query) {
            urlValidation.accept(body)
            return body[PATH_KEY]
        }
        v.should == PATH_EXPECTED_RETURN

        v = http.patch("/full-echo", requestHeader) {
            headerValidation.accept(body)
            pathValidation.accept(body)
            return body[PATH_KEY]
        }
        v.should == PATH_EXPECTED_RETURN

        v = http.patch("/full-echo", requestBody) {
            bodyAsMapValidation.accept(body)
            pathValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyMap

        v = http.patch("/full-echo") {
            pathValidation.accept(body)
            return body[PATH_KEY]
        }
        v.should == PATH_EXPECTED_RETURN
    }

    @Test
    void "patch without return overloads"() {
        http.patch("/full-echo", query, requestHeader, requestBodyMap) {
            headerValidation.accept(body)
            bodyAsMapValidation.accept(body)
            urlValidation.accept(body)
        }

        http.patch("/full-echo", query, requestHeader, requestBodyList) {
            headerValidation.accept(body)
            bodyAsListValidation.accept(body)
            urlValidation.accept(body)
        }

        http.patch("/full-echo", query, requestHeader, requestBody) {
            headerValidation.accept(body)
            bodyAsMapValidation.accept(body)
            urlValidation.accept(body)
        }

        http.patch("/full-echo", query, requestHeader, requestBodyList) {
            headerValidation.accept(body)
            bodyAsListValidation.accept(body)
            urlValidation.accept(body)
        }

        http.patch("/full-echo", query, requestBody) {
            bodyAsMapValidation.accept(body)
            urlValidation.accept(body)
        }

        http.patch("/full-echo", query, requestBodyMap) {
            bodyAsMapValidation.accept(body)
            urlValidation.accept(body)
        }

        http.patch("/full-echo", query, requestBodyList) {
            bodyAsListValidation.accept(body)
            urlValidation.accept(body)
        }

        http.patch("/full-echo", requestHeader, requestBodyMap) {
            headerValidation.accept(body)
            bodyAsMapValidation.accept(body)
            pathValidation.accept(body)
        }

        http.patch("/full-echo", requestHeader, requestBodyList) {
            headerValidation.accept(body)
            bodyAsListValidation.accept(body)
            pathValidation.accept(body)
        }

        http.patch("/full-echo", requestHeader, requestBody) {
            headerValidation.accept(body)
            bodyAsMapValidation.accept(body)
            pathValidation.accept(body)
        }

        http.patch("/full-echo", query, requestHeader) {
            headerValidation.accept(body)
            urlValidation.accept(body)
        }

        http.patch("/full-echo", query) {
            urlValidation.accept(body)
        }

        http.patch("/full-echo", requestHeader) {
            headerValidation.accept(body)
            pathValidation.accept(body)
        }

        http.patch("/full-echo", requestBody) {
            bodyAsMapValidation.accept(body)
            pathValidation.accept(body)
        }

        http.patch("/full-echo") {
            pathValidation.accept(body)
        }
    }

    @Test
    void "put with return overloads"() {
        def v
        v = http.put("/full-echo", query, requestHeader, requestBodyMap) {
            headerValidation.accept(body)
            bodyAsMapValidation.accept(body)
            urlValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyMap

        v = http.put("/full-echo", query, requestHeader, requestBodyList) {
            headerValidation.accept(body)
            bodyAsListValidation.accept(body)
            urlValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyList

        v = http.put("/full-echo", query, requestHeader, requestBody) {
            headerValidation.accept(body)
            bodyAsMapValidation.accept(body)
            urlValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyMap

        http.put("/full-echo", query, requestBody) {
            bodyAsMapValidation.accept(body)
            urlValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyMap

        v = http.put("/full-echo", query, requestBodyMap) {
            bodyAsMapValidation.accept(body)
            urlValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyMap

        v = http.put("/full-echo", query, requestBodyList) {
            bodyAsListValidation.accept(body)
            urlValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyList

        v = http.put("/full-echo", requestHeader, requestBodyMap) {
            headerValidation.accept(body)
            bodyAsMapValidation.accept(body)
            pathValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyMap

        v = http.put("/full-echo", requestHeader, requestBodyList) {
            headerValidation.accept(body)
            bodyAsListValidation.accept(body)
            pathValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyList

        v = http.put("/full-echo", requestHeader, requestBody) {
            headerValidation.accept(body)
            bodyAsMapValidation.accept(body)
            pathValidation.accept(body)

            return body[HEADER_KEY]
        }
        v.should == HEADER_EXPECTED_RETURN

        v = http.put("/full-echo", query, requestHeader) {
            headerValidation.accept(body)
            urlValidation.accept(body)

            return body[HEADER_KEY]
        }
        v.should == HEADER_EXPECTED_RETURN

        v = http.put("/full-echo", query) {
            urlValidation.accept(body)
            return body[PATH_KEY]
        }
        v.should == PATH_EXPECTED_RETURN

        v = http.put("/full-echo", requestHeader) {
            headerValidation.accept(body)
            pathValidation.accept(body)
            return body[PATH_KEY]
        }
        v.should == PATH_EXPECTED_RETURN

        v = http.put("/full-echo", requestBody) {
            bodyAsMapValidation.accept(body)
            pathValidation.accept(body)

            return body[BODY_RESPONSE_KEY]
        }
        v.should == requestBodyMap

        v = http.put("/full-echo") {
            pathValidation.accept(body)
            return body[PATH_KEY]
        }
        v.should == PATH_EXPECTED_RETURN
    }

    @Test
    void "delete without return overloads"() {
        http.delete("/full-echo", query, requestHeader) {
            headerValidation.accept(body)
            urlValidation.accept(body)
        }

        http.delete("/full-echo", queryAsMap, requestHeader) {
            headerValidation.accept(body)
            urlValidation.accept(body)
        }

        http.delete("/full-echo", requestHeader) {
            headerValidation.accept(body)
            pathValidation.accept(body)
        }

        http.delete("/full-echo", query) {
            urlValidation.accept(body)
        }

        http.delete("/full-echo", queryAsMap) {
            urlValidation.accept(body)
        }

        http.delete("/full-echo") {
            pathValidation.accept(body)
        }
    }

    @Test
    void "delete with return overloads"() {
        def number
        def expected = 200

        number = http.delete("/full-echo", query, requestHeader) {
            headerValidation.accept(body)
            urlValidation.accept(body)

            return header.statusCode
        }
        number.should == expected

        number = http.delete("/full-echo", queryAsMap, requestHeader) {
            headerValidation.accept(body)
            urlValidation.accept(body)

            return header.statusCode
        }
        number.should == expected

        number = http.delete("/full-echo", requestHeader) {
            headerValidation.accept(body)
            pathValidation.accept(body)

            return header.statusCode
        }
        number.should == expected

        number = http.delete("/full-echo", query) {
            urlValidation.accept(body)

            return header.statusCode
        }
        number.should == expected

        number = http.delete("/full-echo", queryAsMap) {
            urlValidation.accept(body)

            return header.statusCode
        }
        number.should == expected

        number = http.delete("/full-echo", ) {
            pathValidation.accept(body)

            return header.statusCode
        }
        number.should == expected
    }

    @Test
    void "post without validation"() {
        http.post("/full-echo", query, requestHeader, requestBody)
        http.post("/full-echo", query, requestHeader)
        http.post("/full-echo", query, requestBody)
        http.post("/full-echo", query, requestHeader)
        http.post("/full-echo", requestHeader)
        http.post("/full-echo", query, requestBody)
        http.post("/full-echo", requestHeader, requestBody)
        http.post("/full-echo", query)
        http.post("/full-echo", requestBody)
        http.post("/full-echo")

        http.post("/full-echo", query, requestHeader, requestBodyMap)
        http.post("/full-echo", query, requestHeader, requestBodyList)
        http.post("/full-echo", query, requestBodyMap)
        http.post("/full-echo", query, requestBodyList)
        http.post("/full-echo", requestBodyMap)
        http.post("/full-echo", requestBodyList)
        http.post("/full-echo", requestHeader, requestBodyMap)
        http.post("/full-echo", requestHeader, requestBodyList)
    }

    @Test
    void "put without validation"() {
        http.put("/full-echo", query, requestHeader, requestBody)
        http.put("/full-echo", query, requestHeader)
        http.put("/full-echo", query, requestBody)
        http.put("/full-echo", query, requestHeader)
        http.put("/full-echo", requestHeader)
        http.put("/full-echo", query, requestBody)
        http.put("/full-echo", requestHeader, requestBody)
        http.put("/full-echo", query)
        http.put("/full-echo", requestBody)
        http.put("/full-echo")

        http.put("/full-echo", query, requestHeader, requestBodyMap)
        http.put("/full-echo", query, requestHeader, requestBodyList)
        http.put("/full-echo", query, requestBodyMap)
        http.put("/full-echo", query, requestBodyList)
        http.put("/full-echo", requestBodyMap)
        http.put("/full-echo", requestBodyList)
        http.put("/full-echo", requestHeader, requestBodyMap)
        http.put("/full-echo", requestHeader, requestBodyList)
    }
}
