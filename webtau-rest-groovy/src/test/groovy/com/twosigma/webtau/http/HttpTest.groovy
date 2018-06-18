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
import com.twosigma.webtau.http.testserver.TestServerBinaryResponse
import com.twosigma.webtau.http.testserver.TestServerJsonResponse
import com.twosigma.webtau.http.testserver.TestServerResponseEcho
import com.twosigma.webtau.http.testserver.TestServerTextResponse
import com.twosigma.webtau.utils.ResourceUtils
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

import static com.twosigma.webtau.Ddjt.beGreaterThan
import static com.twosigma.webtau.Ddjt.beGreaterThanOrEqual
import static com.twosigma.webtau.Ddjt.beLessThan
import static com.twosigma.webtau.Ddjt.beLessThanOrEqual
import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.contain
import static com.twosigma.webtau.Ddjt.greaterThan
import static com.twosigma.webtau.Ddjt.greaterThanOrEqual
import static com.twosigma.webtau.Ddjt.lessThan
import static com.twosigma.webtau.Ddjt.lessThanOrEqual
import static com.twosigma.webtau.Ddjt.notEqual
import static com.twosigma.webtau.Ddjt.throwException
import static com.twosigma.webtau.http.Http.http

class HttpTest {
    static TestServer testServer = new TestServer()

    @BeforeClass
    static void startServer() {
        testServer.start(7823)
        testServer.registerGet("/end-point", jsonResponse("objectTestResponse.json"))
        testServer.registerGet("/end-point-mixed", jsonResponse("mixedTestResponse.json"))
        testServer.registerGet("/end-point-numbers", jsonResponse("numbersTestResponse.json"))
        testServer.registerGet("/end-point-list", jsonResponse("listTestResponse.json"))
        testServer.registerGet("/end-point-dates", jsonResponse("datesTestResponse.json"))
        testServer.registerGet("/binary", new TestServerBinaryResponse(ResourceUtils.binaryContent("dummy-image.png")))
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
    void "handles nulls"() {
        code {
            http.post("/echo", [a: null]) {
                a.shouldNot == null
            }
        } should throwException('\nequals [null], but shouldn\'t\n' +
            'mismatches:\n' +
            '\n' +
            'body.a:   actual: null\n' +
            '        expected: null')
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

        assert object == [k1: 'v1', k2: 'v2', k3: 'v3']
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
    void "groovy find on body that is not a list"() {
        def found = http.get("/end-point") {
            return body.find { it > 1 }
        }

        assert found == null
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
    void "groovy findAll on body that is not a list"() {
        def found = http.get("/end-point") {
            return body.findAll { it > 1 }
        }

        assert found == []
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
        def transformed = http.get("/end-point") {
            return list.collect { "world#${it}" }
        }

        assert transformed == ['world#1', 'world#2', 'world#3']
        assert transformed[0] instanceof GString
    }

    @Test
    void "groovy transform on body that is not a list"() {
        def transformed = http.get("/end-point") {
            return body.collect { "world#${it}" }
        }

        assert transformed == []
    }

    @Test
    void "binary content"() {
        byte[] expectedImage = ResourceUtils.binaryContent("dummy-image.png")

        byte[] content = http.get("/binary") {
            body.should == expectedImage
            return body
        }

        content.length.should == expectedImage.length
    }

    @Test
    void "equality matcher"() {
        http.get("/end-point") {
            id.shouldNot == 0
            amount.should == 30

            list.should == [1, 2, 3]

            object.k1.should == ~/v\d/ // regular expression matching

            object.should == [k1: 'v1', k3: 'v3'] // matching only specified fields and can be nested multiple times

            complexList.should == ["k1"   | "k2"] { // matching only specified fields, but number of entries must be exact
                                   ________________
                                    "v1"  | "v2"
                                    "v11" | "v22" }
        }

        http.doc.capture("end-point-object-equality-matchers")
    }

    @Test
    void "compare numbers with greater less matchers"() {
        http.get("/end-point-numbers") {
            id.should beGreaterThan(0)
            price.should beGreaterThanOrEqual(100)
            amount.should beLessThan(150)
            list[1].should beLessThanOrEqual(2)

            id.shouldNot beLessThanOrEqual(0)
            price.shouldNot beLessThan(100)
            amount.shouldNot beGreaterThanOrEqual(150)
            list[1].shouldNot beGreaterThan(2)
        }

        http.doc.capture("end-point-numbers-matchers")
    }

    @Test
    void "contain matcher"() {
        http.get("/end-point-list") {
            body.should contain([k1: 'v1', k2: 'v2'])
            body[1].k2.should contain(20)
        }

        http.doc.capture("end-point-list-contain-matchers")
    }

    @Test
    void "working with dates"() {
        http.get("/end-point-dates") {
            def expectedDate = LocalDate.of(2018, 6, 12)
            def expectedTime = ZonedDateTime.of(expectedDate,
                LocalTime.of(9, 0, 0),
                ZoneId.of("UTC"))

            tradeDate.should == expectedDate
            transactionTime.should == expectedTime
            transactionTime.should beGreaterThanOrEqual(expectedDate)

            paymentSchedule.should contain(expectedDate)
        }

        http.doc.capture("end-point-dates-matchers")
    }

    @Test
    void "matchers combo"() {
        http.get("/end-point-mixed") {
            list.should contain(lessThanOrEqual(2)) // lessThanOrEqual will be matched against each value

            object.should == [k1: 'v1', k3: ~/v\d/] // regular expression match against k3

            complexList[0].should == [k1: 'v1', k2: lessThan(120)] // lessThen match against k2

            complexList[1].should == [
                k1: notEqual('v1'), // any value but v1
                k2: greaterThanOrEqual(120)]

            complexList.should == ["k1"   | "k2"] {
                                  ___________________________
                                   ~/v\d/ | lessThan(120)
                                    "v11" | greaterThan(150) } // using matchers as cell values
        }

        http.doc.capture("end-point-mixing-matchers")
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

    private static TestServerJsonResponse jsonResponse(String resourceName) {
        return new TestServerJsonResponse(ResourceUtils.textContent(resourceName))
    }
}
