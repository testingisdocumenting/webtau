/*
 * Copyright 2020 webtau maintainers
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
import org.testingisdocumenting.webtau.utils.FileUtils
import org.testingisdocumenting.webtau.utils.JsonUtils

import java.nio.file.Files
import java.nio.file.Path
import java.util.function.Consumer
import java.util.stream.Collectors

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue
import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.contain
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.webtau.cfg.WebTauConfig.cfg
import static org.testingisdocumenting.webtau.http.Http.http

class HttpDocCaptureTest extends HttpTestBase {
    @Test
    void "no files generated for empty request and response"() {
        http.post("/empty") {
            body.should == null
        }

        def artifactName = 'empty'
        http.doc.capture(artifactName)

        Path docRoot = getCfg().docArtifactsPath.resolve(artifactName)
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

        Path docRoot = getCfg().docArtifactsPath.resolve(artifactName)
        Path requestFile = docRoot.resolve("request.data")
        assertFalse(Files.exists(requestFile))
    }

    @Test
    void "appropriate doc capture files are generated"() {
        def requestBody = [a: 'b', c: 1]
        def requestHeaders = [testheader: 'testValue', another: 'value']
        http.post("echo-body-and-header", http.header(requestHeaders), requestBody) {
            body.a.should == 'b'
            header.testheader.should == 'testValue'
            header.another.should == 'value'
        }

        String artifactName = 'echo-body-and-header'
        http.doc.capture(artifactName)

        readAndAssertCapturedFile(artifactName, 'request.json') { requestBodyFile ->
            def capturedRequestBody = JsonUtils.deserializeAsMap(requestBodyFile)
            capturedRequestBody.should == requestBody
        }

        readAndAssertCapturedFile(artifactName, 'response.json') { responseBodyFile ->
            def capturedResponseBody = JsonUtils.deserializeAsMap(responseBodyFile)
            capturedResponseBody.should == requestBody
        }

        readAndAssertCapturedFile(artifactName, 'request.header.txt') { requestHeaderFile ->
            def capturedRequestHeaders = setOfLines(requestHeaderFile)
            capturedRequestHeaders.should == requestHeaders.collect { header -> "${header.key}: ${header.value}" }.toSet()
        }

        readAndAssertCapturedFile(artifactName, 'response.header.txt') { responseHeaderFile ->
            def capturedResponseHeaders = setOfLines(responseHeaderFile)
            capturedResponseHeaders.should contain('testheader: testValue')
            capturedResponseHeaders.should contain('another: value')
        }

        readAndAssertCapturedFile(artifactName, 'paths.json') { pathsFile ->
            def capturedPaths = JsonUtils.deserializeAsList(pathsFile)
            capturedPaths.should == ['root.a']
        }
    }

    @Test
    void "captured doc headers are redacted"() {
        def requestHeaders = [testheader: 'testValue', authorization: 'topSecret']
        http.post("echo-body-and-header", http.header(requestHeaders)) {
            header.testheader.should == 'testValue'
            header.authorization.should == 'topSecret'
        }

        String artifactName = 'echo-body-and-header-redacted'
        http.doc.capture(artifactName)

        String redactedAuth = 'authorization: ................'

        readAndAssertCapturedFile(artifactName, 'request.header.txt') { requestHeaderFile ->
            authHeader(requestHeaderFile).should == redactedAuth
        }

        readAndAssertCapturedFile(artifactName, 'response.header.txt') { responseHeaderFile ->
            authHeader(responseHeaderFile).should == redactedAuth
        }
    }

    @Test
    void "url doc capture includes query params when part of url"() {
        http.get('/path?a=1&b=text') {}

        String artifactName = 'url-capture'
        http.doc.capture(artifactName)

        readAndAssertCapturedFileTextContents(artifactName, 'request.url.txt', '/path?a=1&b=text')
        readAndAssertCapturedFileTextContents(artifactName, 'request.fullurl.txt', "${testServer.uri}/path?a=1&b=text")
    }

    @Test
    void "url doc capture includes query params specified as HttpQueryParams"() {
        http.get('/path', http.query([a: 1, b: 'text'])) {}

        String artifactName = 'url-capture-with-query-params'
        http.doc.capture(artifactName)

        readAndAssertCapturedFileTextContents(artifactName, 'request.url.txt', '/path?a=1&b=text')
        readAndAssertCapturedFileTextContents(artifactName, 'request.fullurl.txt', "${testServer.uri}/path?a=1&b=text")
    }

    @Test
    void "http method and operation are captured for docs"() {
        http.get('/path', http.query([a: 1, b: 'text'])) {}

        String artifactName = 'operation-capture'
        http.doc.capture(artifactName)

        readAndAssertCapturedFileTextContents(artifactName, 'request.method.txt', 'GET')
        readAndAssertCapturedFileTextContents(artifactName, "request.operation.txt", 'GET /path?a=1&b=text')
    }

    @Test
    void "doc capture checks that artifact name is not being re-used"() {
        http.get('/path', http.query([a: 1, b: 'text'])) {}

        String artifactName = 'name-reuse-check'
        http.doc.capture(artifactName)

        code {
            http.doc.capture(artifactName)
        } should throwException("doc artifact name <${artifactName}> was already used")
    }

    static def readAndAssertCapturedFileTextContents(String artifactName, String name, String expectedContents) {
        readAndAssertCapturedFile(artifactName, name) { pathFile ->
            FileUtils.fileTextContent(pathFile).should == expectedContents
        }
    }

    static def setOfLines(Path file) {
        return Files.lines(file).collect(Collectors.toSet())
    }

    static def authHeader(Path file) {
        def authorizationHeader = Files.lines(file).filter { line -> line.toLowerCase().startsWith('authorization') }.findFirst().get()
        return authorizationHeader.toLowerCase()
    }

    static def readAndAssertCapturedFile(String artifactName, String name, Consumer<Path> assertions) {
        Path docRoot = getCfg().docArtifactsPath.resolve(artifactName)
        Path captureFile = docRoot.resolve(name)
        assertTrue("${name} for ${artifactName} expected in ${captureFile} but does not exist", Files.exists(captureFile))
        assertions.accept(captureFile)
    }
}
