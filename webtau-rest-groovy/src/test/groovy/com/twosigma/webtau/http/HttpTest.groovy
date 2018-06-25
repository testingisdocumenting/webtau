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
import com.twosigma.webtau.http.testserver.TestServerFakeFileUpload
import com.twosigma.webtau.http.testserver.TestServerJsonResponse
import com.twosigma.webtau.http.testserver.TestServerMultiPartContentEcho
import com.twosigma.webtau.http.testserver.TestServerMultiPartMetaEcho
import com.twosigma.webtau.http.testserver.TestServerRequestHeaderEcho
import com.twosigma.webtau.http.testserver.TestServerResponse
import com.twosigma.webtau.http.testserver.TestServerResponseEcho
import com.twosigma.webtau.http.testserver.TestServerTextResponse
import com.twosigma.webtau.utils.ResourceUtils
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import javax.servlet.http.HttpServletRequest
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

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

    private final static byte[] sampleFile = [1, 2, 3]

    @BeforeClass
    static void startServer() {
        testServer.start(7823)

        def objectTestResponse = jsonResponse("objectTestResponse.json")

        testServer.registerGet("/end-point", objectTestResponse)
        testServer.registerGet("/end-point?queryParam1=queryParamValue1", objectTestResponse)
        testServer.registerPost("/end-point", jsonResponse("objectTestResponse.json", 201))
        testServer.registerPut("/end-point", objectTestResponse)
        testServer.registerDelete("/end-point", objectTestResponse)
        testServer.registerGet("/end-point-simple-object", jsonResponse("simpleObjectTestResponse.json"))
        testServer.registerGet("/end-point-simple-list", jsonResponse("simpleListTestResponse.json"))
        testServer.registerGet("/end-point-mixed", jsonResponse("mixedTestResponse.json"))
        testServer.registerGet("/end-point-numbers", jsonResponse("numbersTestResponse.json"))
        testServer.registerGet("/end-point-list", jsonResponse("listTestResponse.json"))
        testServer.registerGet("/end-point-dates", jsonResponse("datesTestResponse.json"))
        testServer.registerGet("/binary", new TestServerBinaryResponse(ResourceUtils.binaryContent("image.png")))
        testServer.registerPost("/echo", new TestServerResponseEcho(201))
        testServer.registerPut("/echo", new TestServerResponseEcho(200))
        testServer.registerGet("/echo-header", new TestServerRequestHeaderEcho(200))
        testServer.registerGet("/echo-header?qp1=v1", new TestServerRequestHeaderEcho(200))
        testServer.registerPost("/echo-header", new TestServerRequestHeaderEcho(201))
        testServer.registerPut("/echo-header", new TestServerRequestHeaderEcho(200))
        testServer.registerDelete("/echo-header", new TestServerRequestHeaderEcho(200))
        testServer.registerPost("/echo-multipart-content-part-one", new TestServerMultiPartContentEcho(201, 0))
        testServer.registerPost("/echo-multipart-content-part-two", new TestServerMultiPartContentEcho(201, 1))
        testServer.registerPost("/echo-multipart-meta", new TestServerMultiPartMetaEcho(201))
        testServer.registerPost("/file-upload", new TestServerFakeFileUpload())
        testServer.registerDelete("/resource", new TestServerTextResponse('abc'))
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
    void "explicit header passing"() {
        def headerPayload = [
                'Custom-Header-One': 'custom-value-one',
                'Custom-Header-Two': 'custom-value-two']

        def header = http.header(headerPayload)
        def expectations = {
            body.should == headerPayload
        }

        http.get("/echo-header", header, expectations)
        http.get("/echo-header", [qp1: 'v1'], header, expectations)
        http.post("/echo-header", header, [:], expectations)
        http.put("/echo-header", header, [:], expectations)
        http.delete("/echo-header", header, expectations)
    }

    @Test
    void "explicit header passing example"() {
        http.get("/end-point", http.header('Accept', 'application/octet-stream')) {
            // assertions go here
        }

        http.get("/end-point", [queryParam1: 'queryParamValue1'],
                http.header('Accept', 'application/octet-stream')) {
            // assertions go here
        }

        http.post("/end-point", http.header('Accept', 'application/octet-stream'),
                [fileId: 'myFile']) {
            // assertions go here
        }

        http.put("/end-point", http.header('Accept', 'application/octet-stream'),
                [fileId: 'myFile', file: sampleFile]) {
            // assertions go here
        }

        http.delete("/end-point", http.header('Custom-Header', 'special-value'))
    }

    @Test
    void "header creation"() {
        def varArgHeader = http.header(
                'My-Header1', 'Value1',
                'My-Header2', 'Value2')

        def mapBasedHeader = http.header([
                'My-Header1': 'Value1',
                'My-Header2': 'Value2'])

        assert varArgHeader == mapBasedHeader
    }

    @Test
    void "no body request"() {
        def noBodyExpectation = {
            body.should == ""
        }

        http.post("/echo", noBodyExpectation)
        http.put("/echo", noBodyExpectation << { statusCode.should == 200 })

        def headerValues = [Custom: 'Value']
        def header = http.header(headerValues)

        http.post("/echo-header", header) {
            body.should == headerValues
        }

        http.put("/echo-header", header) {
            body.should == headerValues
            statusCode.should == 200
        }
    }

    @Test
    void "no validation request"() {
        def counter = [:].withDefault { 0 }
        def responseCounter = { statusCode -> new TestServerResponse() {
            @Override
            byte[] responseBody(HttpServletRequest request) {
                counter[request.method]++
                return "".bytes
            }

            @Override
            String responseType(HttpServletRequest request) {
                return "application/json"
            }

            @Override
            int responseStatusCode() {
                return statusCode
            }
        }}

        testServer.registerPost("/no-body", responseCounter(201))
        testServer.registerPut("/no-body", responseCounter(204))

        def header = http.header('Custom', 'Value')
        http.post("/no-body", header)
        http.post("/no-body")
        http.put("/no-body", header)
        http.put("/no-body")

        counter.should == [POST: 2, PUT:2]
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
    void "send form data"() {
        byte[] content = [0, 1, 2, 101, 102, 103, 0] as byte[]

        http.post("/echo-multipart-content-part-one", http.formData(http.formField('file', content))) {
            body.should == content
        }

        http.post("/echo-multipart-content-part-two", http.formData(
            http.formField('file', content),
            http.formField('saveAs', 'second-form-parameter'))) {

            body.should == 'second-form-parameter'.getBytes()
        }

        http.post("/echo-multipart-meta", http.formData(
            http.formField('file', content),
            http.formField('additionalField', 'hello'))) {

            body.should contain([
                    fieldName: 'file',
                    fileName: null])

            body.should contain([
                    fieldName: 'additionalField',
                    fileName: null])
        }

        http.post("/echo-multipart-meta", http.formData(
            http.formField('file', content, 'myFileName'))) {

            body.should contain([
                fieldName: 'file',
                fileName: 'myFileName'])
        }
    }

    @Test
    void "send form file data from specified path"() {
        def imagePath = Paths.get("src/test/resources/image.png")

        http.post("/echo-multipart-content-part-one", http.formData(http.formField('file', imagePath))) {
            body.should == imagePath.readBytes()
        }


        http.post("/echo-multipart-meta", http.formData(http.formField('file', imagePath))) {
            body.should contain([
                fieldName: 'file',
                fileName: 'image.png'])
        }

        http.post("/echo-multipart-meta", http.formData(http.formField('file', imagePath, 'nameOverride'))) {
            body.should contain([
                fieldName: 'file',
                fileName: 'nameOverride'])
        }
    }

    @Test
    void "send form map based syntax with default file name"() {
        byte[] fileContent = [1, 2, 3, 4] as byte[]

        http.post("/echo-multipart-meta", http.formData(file: fileContent, userName: 'userName')) {
            body.should contain([
                fieldName: 'file',
                fileName: null])
        }
    }

    @Test
    void "send form map based syntax with specified file name"() {
        byte[] content = [1] as byte[]

        http.post("/echo-multipart-meta", http.formData(
                file: http.formFile('myFileName', content),
                userName: 'userName')) {
            body.should contain([
                fieldName: 'file',
                fileName: 'myFileName'])

            body.should contain([
                fieldName: 'userName',
                fileName: null])
        }
    }

    @Test
    void "file upload example simple"() {
        def imagePath = Paths.get("src/test/resources/image.png")

        http.post("/file-upload", http.formData(file: imagePath)) {
            fileName.should == 'image.png'
        }
    }

    @Test
    void "file upload example with file name override"() {
        def imagePath = Paths.get("src/test/resources/image.png")

        http.post("/file-upload", http.formData(file: http.formFile('myFileName.png', imagePath))) {
            fileName.should == 'myFileName.png'
        }
    }

    @Test
    void "file upload example multiple fields"() {
        def imagePath = Paths.get("src/test/resources/image.png")

        http.post("/file-upload", http.formData(file: imagePath, fileDescription: 'new report')) {
            fileName.should == 'image.png'
            description.should == 'new report'
        }
    }

    @Test
    void "file upload example with in-memory content"() {
        byte[] fileContent = [1, 2, 3, 4] as byte[]

        http.post("/file-upload", http.formData(file: fileContent)) {
            fileName.should == 'backend-generated-name-as-no-name-provided'
        }
    }

    @Test
    void "file upload example with in-memory content and file name"() {
        byte[] fileContent = [1, 2, 3, 4] as byte[]

        http.post("/file-upload", http.formData(
                file: http.formFile('myFileName.dat', fileContent))) {
            fileName.should == 'myFileName.dat'
        }
    }

    @Test
    void "file upload example with file path and name override"() {
        def imagePath = Paths.get("src/test/resources/image.png")

        http.post("/file-upload", http.formData(
                file: http.formFile('myFileName.dat', imagePath),
                fileDescription: 'new report')) {
            fileName.should == 'myFileName.dat'
            description.should == 'new report'
        }
    }

    @Test
    void "receive binary content"() {
        byte[] expectedImage = ResourceUtils.binaryContent("image.png")

        byte[] content = http.get("/binary") {
            body.should == expectedImage
            return body
        }

        content.length.should == expectedImage.length
    }

    @Test
    void "simple object mapping example"() {
        http.get("/end-point-simple-object") {
            k1.should == 'v1'
        }
    }

    @Test
    void "simple list mapping example"() {
        http.get("/end-point-simple-list") {
            body[0].k1.should == 'v1'
        }
    }

    @Test
    void "equality matcher"() {
        http.get("/end-point") {
            id.should != 0
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
            id.shouldBe > 0
            price.shouldBe >= 100
            amount.shouldBe < 150
            list[1].shouldBe <= 2

            id.shouldNotBe <= 0
            price.shouldNotBe < 100
            amount.shouldNotBe >= 150
            list[1].shouldNotBe > 2
        }

        http.doc.capture("end-point-numbers-matchers")
    }

    @Test
    void "contain matcher"() {
        http.get("/end-point-list") {
            body.should contain([k1: 'v1', k2: 'v2'])
            body[1].k2.shouldNot contain(22)
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
            transactionTime.shouldBe >= expectedDate

            paymentSchedule.should contain(expectedDate)
        }

        http.doc.capture("end-point-dates-matchers")
    }

    @Test
    void "implicit status code check when no explicit check present"() {
        code {
            def id = http.get("/no-resource") { id }
        } should throwException('\ndoesn\'t equal 200\n' +
            'mismatches:\n' +
            '\n' +
            'header.statusCode:   actual: 404 <java.lang.Integer>\n' +
            '                   expected: 200 <java.lang.Integer>')

        http.get("/no-resource") {
            statusCode.should == 404
        }
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

    private static TestServerJsonResponse jsonResponse(String resourceName, int statusCode = 200) {
        return new TestServerJsonResponse(ResourceUtils.textContent(resourceName), statusCode)
    }
}
