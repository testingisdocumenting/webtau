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

import com.twosigma.webtau.http.datanode.DataNode
import com.twosigma.webtau.http.datanode.GroovyDataNode
import com.twosigma.webtau.http.testserver.TestServer
import com.twosigma.webtau.http.testserver.TestServerJsonResponse
import com.twosigma.webtau.http.testserver.TestServerResponseEcho
import com.twosigma.webtau.http.testserver.TestServerTextResponse
import com.twosigma.webtau.utils.ResourceUtils
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.throwException
import static com.twosigma.webtau.http.Http.http

class HttpTest {
    static TestServer testServer = new TestServer()

    @BeforeClass
    static void startServer() {
        testServer.start(7823)
        testServer.registerGet("/end-point", new TestServerJsonResponse(ResourceUtils.textContent("objectTestResponse.json")))
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
        http.get("/end-point") {
            price.should == 100
            assert price instanceof DataNode
        }
    }

    @Test
    void "use table data as expected"() {
        http.get("/end-point") {
            complexList.should == ["k1"   | "k2"] {
                                  __________________
                                    "v1"  | "v2"
                                    "v11" | "v22" }
        }
    }

    @Test
    void "can return simple value from get"() {
        def id = http.get("/end-point") {
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
    void "can return status code from delete"() {
        def statusCode = http.delete("/resource") {
            return statusCode
        }

        assert statusCode == 200
    }

    @Test
    void "can return list from get"() {
        def list = http.get("/end-point") {
            return list
        }

        assert list == [1, 2, 3]
        assert list.getClass() == ArrayList
    }

    @Test
    void "can return object from get"() {
        def object = http.get("/end-point") {
            return object
        }

        assert object == [k1: 'v1', k2: 'v2']
        assert object.getClass() == LinkedHashMap
        assert object.k1.getClass() == String
    }

    @Test
    void "can return list of objects from get"() {
        def complexList = http.get("/end-point") {
            return complexList
        }

        assert complexList == [[id: 'id1', k1: 'v1', k2: 'v2'], [id: 'id2', k1: 'v11', k2: 'v22']]
        assert complexList.getClass() == ArrayList
        assert complexList[0].getClass() == LinkedHashMap
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
    void "groovy find on list"() {
        def found = http.get("/end-point") {
            return list.find { it > 1 }
        }

        assert found == 2
        assert found.getClass() == Integer
    }

    @Test
    void "groovy findAll on list"() {
        def found = http.get("/end-point") {
            return list.findAll { it > 1 }
        }

        assert found == [2, 3]
        assert found[0].getClass() == Integer
    }

    @Test
    void "groovy find on list of objects"() {
        def id = http.get("/end-point") {
            def found = complexList.find {
                assert k1.getClass() == String
                k1 == 'v1'
            }
            assert found.getClass() == GroovyDataNode

            return found.id
        }

        assert id.getClass() == String
    }

    @Test
    void "groovy transform list"() {
        def found = http.get("/end-point") {
            return list.collect { "world#${it}" }
        }

        assert found == ['world#1', 'world#2', 'world#3']
        assert found[0] instanceof GString
    }

    @Test
    void "captures failed assertions"() {
        code {
            http.get("params", [a: 1, b: 'text']) {
                a.should == 2
            }
        } should throwException(AssertionError, ~/body\.a:/)

        http.lastValidationResult.mismatches.should == [~/body\.a:/]
    }

    @Test
    void "captures failed http call"() {
        code {
            http.get('mailto://demo', [a: 1, b: 'text']) {
            }
        } should throwException(HttpException, ~/error during http\.get/)

        http.lastValidationResult.errorMessage.should == 'java.lang.ClassCastException: ' +
            'sun.net.www.protocol.mailto.MailToURLConnection cannot be cast to java.net.HttpURLConnection'
    }
}
