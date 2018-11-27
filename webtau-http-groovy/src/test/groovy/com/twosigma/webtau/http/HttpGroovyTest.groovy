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

import com.twosigma.documentation.DocumentationArtifactsLocation
import com.twosigma.webtau.http.config.HttpConfiguration
import com.twosigma.webtau.http.config.HttpConfigurations
import com.twosigma.webtau.http.datanode.DataNode
import com.twosigma.webtau.http.datanode.GroovyDataNode
import com.twosigma.webtau.http.testserver.TestServerResponse
import com.twosigma.webtau.http.validation.HttpResponseValidator
import com.twosigma.webtau.utils.ResourceUtils
import com.twosigma.webtau.utils.UrlUtils
import org.junit.*

import javax.servlet.http.HttpServletRequest
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

import static com.twosigma.webtau.Ddjt.*
import static com.twosigma.webtau.http.Http.*
import static org.junit.Assert.*

class HttpGroovyTest implements HttpConfiguration {
    private static final HttpTestDataServer testServer = new HttpTestDataServer()

    private static final byte[] sampleFile = [1, 2, 3]

    @BeforeClass
    static void startServer() {
        testServer.start()
    }

    @AfterClass
    static void stopServer() {
        testServer.stop()
    }

    @Before
    void initCfg() {
        HttpConfigurations.add(this)
    }

    @After
    void cleanCfg() {
        HttpConfigurations.remove(this)
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
            '        expected: not null')
    }

    @Test
    void "use table data as expected"() {
        http.get("/end-point") {
            complexList.should == ["k1"   | "k2"] {
                                  __________________
                                    "v1"  | 30
                                    "v11" | 40 }
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

        assert complexList == [[id: 'id1', k1: 'v1', k2: 30], [id: 'id2', k1: 'v11', k2: 40]]
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
    void "matchers basic example"() {
        http.get("/example") {
            year.shouldNot == 2000
            year.should != 2000  // alternative shortcut
            genres.should contain('RPG')
            rating.shouldBe > 7
        }
    }

    @Test
    void "explicit header passing"() {
        def headerPayload = [
                'Custom-Header-One': 'custom-value-one',
                'Custom-Header-Two': 'custom-value-two']

        def requestHeader = http.header(headerPayload)
        def expectations = {
            header.should == headerPayload
            body.should == headerPayload
        }

        http.get("/echo-header", requestHeader, expectations)
        http.get("/echo-header", [qp1: 'v1'], requestHeader, expectations)
        http.post("/echo-header", requestHeader, [:], expectations)
        http.put("/echo-header", requestHeader, [:], expectations)
        http.delete("/echo-header", requestHeader, expectations)
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
    void "explicit binary mime types combined with request body"() {
        def content = binaryFile('path')
        http.post("/end-point", http.body("application/octet-stream", content)) {
            // assertions go here
        }
    }

    @Test
    void "explicit text mime types combined with request body"() {
        def content = binaryFile('path')
        http.post("/end-point", http.body("application/octet-stream", content)) {
            // assertions go here
        }
    }

    @Test
    void "post implicit binary mime types combined with request body"() {
        def content = binaryFile('path')
        http.post("/end-point", http.application.octetStream(content)) {
            // assertions go here
        }
    }

    @Test
    void "put implicit binary mime types combined with request body"() {
        def content = binaryFile('path')
        http.put("/end-point", http.application.octetStream(content)) {
            // assertions go here
        }
    }

    @Test
    void "implicit text mime types combined with request body"() {
        def content = 'text content'
        http.post("/end-point", http.text.plain(content)) {
            // assertions go here
        }
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
    void "header assertion with shortcut"() {
        http.post("/end-point") {
            header.location.should == 'http://www.example.org/url/23'
            header['Location'].should == 'http://www.example.org/url/23'

            header.contentLocation.should == '/url/23'
            header['Content-Location'].should == '/url/23'

            header.contentLength.should == 303
            header['Content-Length'].should == 303
        }
    }

    @Test
    void "no body request"() {
        def noBodyExpectation = {
            body.should == null
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
    void "no files generated for empty request and response"() {
        http.post("/empty") {
            body.should == null
        }

        def artifactName = 'empty'
        http.doc.capture(artifactName)

        Path docRoot = DocumentationArtifactsLocation.resolve(artifactName)
        Path requestFile = docRoot.resolve("request.json")
        assertFalse(Files.exists(requestFile))

        Path responseFile = docRoot.resolve("response.json")
        assertFalse(Files.exists(responseFile))
    }

    @Test
    void "no files generated for binary request"() {
        def content = [1, 2, 3] as byte[]
        http.post("/empty", http.application.octetStream(content)) {
            body.should == null
        }

        def artifactName = 'empty-binary'
        http.doc.capture(artifactName)

        Path docRoot = DocumentationArtifactsLocation.resolve(artifactName)
        Path requestFile = docRoot.resolve("request.data")
        assertFalse(Files.exists(requestFile))
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
    void "groovy transform list by referencing node"() {
        def ids = http.get("/end-point") {
            return complexList.collect { it.id }
        }

        assert ids == ['id1', 'id2']
    }

    @Test
    void "groovy transform on body that is not a list"() {
        def transformed = http.get("/end-point") {
            return body.collect { "world#${it}" }
        }

        assert transformed == []
    }

    @Test
    void "groovy findAll, collect, and sum"() {
        def sum = http.get("/end-point") {
            return complexList
                    .findAll { k1.startsWith('v1') }
                    .collect { k2 }
                    .sum()
        }

        assert sum == 70
    }

    @Test
    void "groovy children key shortcut"() {
        http.get("/end-point") {
            complexList.k2.should == [30, 40]
        }
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
        def imagePath = testResourcePath("src/test/resources/image.png")

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
        def imagePath = testResourcePath("src/test/resources/image.png")

        http.post("/file-upload", http.formData(file: imagePath)) {
            fileName.should == 'image.png'
        }
    }

    @Test
    void "file upload example with file name override"() {
        def imagePath = testResourcePath("src/test/resources/image.png")

        http.post("/file-upload", http.formData(file: http.formFile('myFileName.png', imagePath))) {
            fileName.should == 'myFileName.png'
        }
    }

    @Test
    void "file upload example multiple fields"() {
        def imagePath = testResourcePath("src/test/resources/image.png")

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
        def imagePath = testResourcePath("src/test/resources/image.png")

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
    void "send binary content"() {
        byte[] expectedImage = ResourceUtils.binaryContent("image.png")

        http.post("/echo", http.application.octetStream(expectedImage)) {
            header.contentType.should == 'application/octet-stream'
            body.should == expectedImage
        }

        http.post("/echo", http.application.pdf(expectedImage)) {
            header.contentType.should == 'application/pdf'
            body.should == expectedImage
        }
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
                                    "v1"  | 30
                                    "v11" | 40 }
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

        assertStatusCodeMismatchRegistered()

        http.get("/no-resource") {
            statusCode.should == 404
        }
    }

    @Test
    void "implicit status code should happen even if there is another assertion mismatch"() {
        code {
            http.get("/no-resource") {
                id.should == 0
                message.shouldBe == 10
            }
        } should throwException('\ndoesn\'t equal 200\n' +
                    'mismatches:\n' +
                    '\n' +
                    'header.statusCode:   actual: 404 <java.lang.Integer>\n' +
                    '                   expected: 200 <java.lang.Integer>')

        assertStatusCodeMismatchRegistered()
    }

    @Test
    void "implicit status code should happen even if there is runtime exception"() {
        code {
            http.get("/no-resource") {
                throw new RuntimeException('error')
            }
        } should throwException('\ndoesn\'t equal 200\n' +
                'mismatches:\n' +
                '\n' +
                'header.statusCode:   actual: 404 <java.lang.Integer>\n' +
                '                   expected: 200 <java.lang.Integer>\n' +
                '\n' +
                'additional exception message:\n' +
                'error')

        assertStatusCodeMismatchRegistered()
    }

    @Test
    void "implicit status code should not happen if explicit status code failed"() {
        code {
            http.get("/no-resource") {
                statusCode.should == 401
            }
        } should throwException('\ndoesn\'t equal 401\n' +
                'mismatches:\n' +
                '\n' +
                'header.statusCode:   actual: 404 <java.lang.Integer>\n' +
                '                   expected: 401 <java.lang.Integer>')

        assertStatusCodeMismatchRegistered()
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

        http.lastValidationResult.errorMessage.should == ~/java.lang.ClassCastException: .*cannot be cast to .*HttpURLConnection/
    }

    @Test
    void "provides appropriate error when headers are null"() {
        code {
            http.get("/end-point", (HttpHeader) null, (HttpResponseValidator) {})
        } should throwException(HttpException, ~/error during http\.get/)

        http.lastValidationResult.errorMessage.should == ~/java.lang.IllegalArgumentException: Request header is null/
    }

    @Override
    String fullUrl(String url) {
        if (UrlUtils.isFull(url)) {
            return url
        }

        return UrlUtils.concat(testServer.uri, url)
    }

    @Override
    HttpHeader fullHeader(String fullUrl, String passedUrl, HttpHeader given) {
        return given
    }

    private static void assertStatusCodeMismatchRegistered() {
        http.lastValidationResult.mismatches.should contain(~/statusCode/)
    }

    private static byte[] binaryFile(String path) {
        return [1, 2, 3] as byte[]
    }

    private static Path testResourcePath(String relativePath) {
        return Paths.get("../webtau-http/${relativePath}")
    }
}
