/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

import com.twosigma.webtau.http.testserver.TestServer
import com.twosigma.webtau.http.testserver.TestServerJsonResponse
import com.twosigma.webtau.http.testserver.TestServerResponseEcho
import com.twosigma.webtau.http.testserver.TestServerTextResponse
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.throwException
import static com.twosigma.webtau.http.Http.http

class HttpExtensionsTest {
    static TestServer testServer = new TestServer()

    @BeforeClass
    static void startServer() {
        testServer.start(7823)
        testServer.registerGet("/object", new TestServerJsonResponse(/{"id": 10, "price": 100, "amount": 30, "list": [1, 2, 3], "complexList": [{"k1": "v1", "k2": "v2"}, {"k1": "v11", "k2": "v22"}]}/))
        testServer.registerPost("/echo", new TestServerResponseEcho())
        testServer.registerPut("/echo", new TestServerResponseEcho())
        testServer.registerDelete("/resource", new TestServerTextResponse(''))
        testServer.registerGet("/params?a=1&b=text", new TestServerJsonResponse(/{"a": 1, "b": "text"}/))
    }

    @AfterClass
    static void stopServer() {
        testServer.stop()
    }

    @Test
    void "use groovy closure as validation"() {
        http.get("/object") {
            price.should == 100
        }
    }

    @Test
    void "use table data as expected"() {
        http.get("/object") {
            complexList.should == ["k1"   | "k2"] {
                                  __________________
                                    "v1"  | "v2"
                                    "v11" | "v22" }
        }
    }

    @Test
    void "can return simple value from get"() {
        def id = http.get("/object") {
            return id
        }

        assert id == 10
        assert id.getClass() == Integer
    }

    @Test
    void "can return simple value from post"() {
        def id = http.post("/echo", [hello: "world", id: "generated-id"]) {
            hello.should == "world"
            return id
        }

        assert id == "generated-id"
        assert id.getClass() == String
    }

    @Test
    void "can return simple value from put"() {
        def id = http.put("/echo", [hello: "world", id: "generated-id"]) {
            return id
        }

        assert id == "generated-id"
    }

    @Test
    void "can send delete and return status code"() {
        int statusCode = http.delete("/resource") {
            return statusCode
        }

        assert statusCode == 200
    }

    @Test
    void "build query params from the map"() {
        http.get("params", [a: 1, b: 'text']) {
            a.should == 1
            b.should == 'text'
        }
    }

    @Test
    void "build query params from the map and return a single value from closure"() {
        def a = http.get("params", [a: 1, b: 'text']) {
            return a
        }

        assert a == 1
    }

    @Test
    void "explicitly access header and body "() {
        def a = http.get("params", [a: 1, b: 'text']) { header, body ->
            return body.a
        }

        assert a == 1
    }

    @Test
    void "captures failed assertions"() {
        code {
            http.get("params", [a: 1, b: 'text']) {
                a.should == 2
            }
        } should throwException(~/body\.a:/)

        http.lastValidationResult.mismatches.should == [~/body\.a:/]
    }
}
