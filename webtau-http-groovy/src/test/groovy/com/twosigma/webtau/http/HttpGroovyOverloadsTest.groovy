/*
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

package com.twosigma.webtau.http

import org.junit.Test

import static com.twosigma.webtau.http.Http.http
import static com.twosigma.webtau.http.HttpOverloadsTestCommon.*

class HttpGroovyOverloadsTest extends HttpTestBase {
    @Test
    void "put without return overloads"() {
        http.put("/full-echo", query, requestHeader, requestBodyMap) {
            headerValidation.accept(body)
            bodyValidation.accept(body)
            urlValidation.accept(body)
        }

        http.put("/full-echo", query, requestHeader, requestBody) {
            headerValidation.accept(body)
            bodyValidation.accept(body)
            urlValidation.accept(body)
        }

        http.put("/full-echo", query, requestBody) {
            bodyValidation.accept(body)
            urlValidation.accept(body)
        }

        http.put("/full-echo", query, requestBodyMap) {
            bodyValidation.accept(body)
            urlValidation.accept(body)
        }

        http.put("/full-echo", requestHeader, requestBodyMap) {
            headerValidation.accept(body)
            bodyValidation.accept(body)
            pathValidation.accept(body)
        }

        http.put("/full-echo", requestHeader, requestBody) {
            headerValidation.accept(body)
            bodyValidation.accept(body)
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
            bodyValidation.accept(body)
            pathValidation.accept(body)
        }

        http.put("/full-echo") {
            pathValidation.accept(body)
        }
    }

    @Test
    void "put with return overloads"() {
        def v
        v = http.put("/full-echo", query, requestHeader, requestBodyMap) {
            headerValidation.accept(body)
            bodyValidation.accept(body)
            urlValidation.accept(body)

            return body[BODY_KEY]
        }
        v.should == BODY_EXPECTED_RETURN

        v = http.put("/full-echo", query, requestHeader, requestBody) {
            headerValidation.accept(body)
            bodyValidation.accept(body)
            urlValidation.accept(body)

            return body[BODY_KEY]
        }
        v.should == BODY_EXPECTED_RETURN

        http.put("/full-echo", query, requestBody) {
            bodyValidation.accept(body)
            urlValidation.accept(body)

            return body[BODY_KEY]
        }
        v.should == BODY_EXPECTED_RETURN

        v = http.put("/full-echo", query, requestBodyMap) {
            bodyValidation.accept(body)
            urlValidation.accept(body)

            return body[BODY_KEY]
        }
        v.should == BODY_EXPECTED_RETURN

        v = http.put("/full-echo", requestHeader, requestBodyMap) {
            headerValidation.accept(body)
            bodyValidation.accept(body)
            pathValidation.accept(body)

            return body[BODY_KEY]
        }
        v.should == BODY_EXPECTED_RETURN

        v = http.put("/full-echo", requestHeader, requestBody) {
            headerValidation.accept(body)
            bodyValidation.accept(body)
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
            bodyValidation.accept(body)
            pathValidation.accept(body)

            return body[BODY_KEY]
        }
        v.should == BODY_EXPECTED_RETURN

        v = http.put("/full-echo") {
            pathValidation.accept(body)
            return body[PATH_KEY]
        }
        v.should == PATH_EXPECTED_RETURN
    }
}
