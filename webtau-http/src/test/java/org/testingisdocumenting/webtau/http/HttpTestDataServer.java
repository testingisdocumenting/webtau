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

package org.testingisdocumenting.webtau.http;

import org.testingisdocumenting.webtau.http.testserver.*;
import org.testingisdocumenting.webtau.utils.CollectionUtils;
import org.testingisdocumenting.webtau.utils.ResourceUtils;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

public class HttpTestDataServer {
    private final TestServer testServer;

    public HttpTestDataServer() {
        testServer = new TestServer();

        TestServerJsonResponse objectTestResponse = jsonResponse("objectTestResponse.json");

        testServer.registerGet("/end-point", objectTestResponse);
        testServer.registerGet("/end-point?queryParam1=queryParamValue1", objectTestResponse);

        testServer.registerPost("/end-point", jsonResponse("objectTestResponse.json", 201,
                CollectionUtils.aMapOf(
                        "Content-Location", "/url/23",
                        "Location", "http://www.example.org/url/23")));

        testServer.registerGet("/example", jsonResponse("matcherExampleResponse.json"));

        testServer.registerPut("/end-point", objectTestResponse);
        testServer.registerPatch("/end-point", objectTestResponse);
        testServer.registerDelete("/end-point", objectTestResponse);
        testServer.registerGet("/end-point-simple-object", jsonResponse("simpleObjectTestResponse.json"));
        testServer.registerGet("/end-point-simple-list", jsonResponse("simpleListTestResponse.json"));
        testServer.registerGet("/end-point-mixed", jsonResponse("mixedTestResponse.json"));
        testServer.registerGet("/end-point-numbers", jsonResponse("numbersTestResponse.json"));
        testServer.registerGet("/end-point-list", jsonResponse("listTestResponse.json"));
        testServer.registerGet("/end-point-dates", jsonResponse("datesTestResponse.json"));
        testServer.registerGet("/binary", new TestServerBinaryResponse(ResourceUtils.binaryContent("image.png")));
        testServer.registerPost("/echo", new TestServerResponseEcho(201));
        testServer.registerPut("/echo", new TestServerResponseEcho(200));
        testServer.registerPatch("/echo", new TestServerResponseEcho(200));
        testServer.registerPut("/full-echo", new TestServerRequestFullEcho(200));
        testServer.registerPut("/full-echo?a=1&b=text", new TestServerRequestFullEcho(200));
        testServer.registerPatch("/full-echo", new TestServerRequestFullEcho(200));
        testServer.registerPatch("/full-echo?a=1&b=text", new TestServerRequestFullEcho(200));
        testServer.registerDelete("/full-echo", new TestServerRequestFullEcho(200));
        testServer.registerDelete("/full-echo?a=1&b=text", new TestServerRequestFullEcho(200));
        testServer.registerGet("/echo-header", new TestServerRequestHeaderEcho(200));
        testServer.registerGet("/echo-header?qp1=v1", new TestServerRequestHeaderEcho(200));
        testServer.registerPatch("/echo-header", new TestServerRequestHeaderEcho(200));
        testServer.registerPost("/echo-header", new TestServerRequestHeaderEcho(201));
        testServer.registerPut("/echo-header", new TestServerRequestHeaderEcho(200));
        testServer.registerPatch("/echo-header", new TestServerRequestHeaderEcho(200));
        testServer.registerDelete("/echo-header", new TestServerRequestHeaderEcho(200));
        testServer.registerPost("/echo-body-and-header", new TestServerRequestHeaderAndBodyEcho(201));
        testServer.registerPost("/echo-multipart-content-part-one", new TestServerMultiPartContentEcho(201, 0));
        testServer.registerPost("/echo-multipart-content-part-two", new TestServerMultiPartContentEcho(201, 1));
        testServer.registerPost("/echo-multipart-meta", new TestServerMultiPartMetaEcho(201));
        testServer.registerPost("/empty", new TestServerJsonResponse(null, 201));
        testServer.registerPatch("/empty", new TestServerJsonResponse(null, 204));
        testServer.registerPost("/file-upload", new TestServerFakeFileUpload());
        testServer.registerDelete("/resource", new TestServerTextResponse("abc"));
        testServer.registerGet("/params?a=1&b=text", new TestServerJsonResponse("{\"a\": 1, \"b\": \"text\"}"));
        testServer.registerPost("/params?a=1&b=text", new TestServerJsonResponse("{\"a\": 1, \"b\": \"text\"}", 201));
        testServer.registerGet("/integer", new TestServerJsonResponse("123"));
        testServer.registerPost("/json-derivative", new TestServerJsonDerivativeResponse());

        testServer.registerGet("/address", jsonResponse("addressResponse.json"));
        registerRedirects();
    }

    private void registerRedirects() {
        registerRedirectOnAllMethods(HttpURLConnection.HTTP_MOVED_TEMP, "/redirect", "/redirect2");
        registerRedirectOnAllMethods(HttpURLConnection.HTTP_MOVED_PERM, "/redirect2", "/redirect3");
        registerRedirectOnAllMethods(307, "/redirect3", "/redirect4");

        testServer.registerGet("/redirect4", new TestServerRedirectResponse(HttpURLConnection.HTTP_SEE_OTHER, testServer, "/end-point"));
        testServer.registerPost("/redirect4", new TestServerRedirectResponse(HttpURLConnection.HTTP_SEE_OTHER, testServer, "/echo"));
        testServer.registerPut("/redirect4", new TestServerRedirectResponse(HttpURLConnection.HTTP_SEE_OTHER, testServer, "/echo"));
        testServer.registerDelete("/redirect4", new TestServerRedirectResponse(HttpURLConnection.HTTP_SEE_OTHER, testServer, "/end-point"));

        testServer.registerGet("/recursive", new TestServerRedirectResponse(HttpURLConnection.HTTP_MOVED_TEMP, testServer, "/recursive"));
    }

    private void registerRedirectOnAllMethods(int statusCode, String fromPath, String toPath) {
        TestServerRedirectResponse response = new TestServerRedirectResponse(statusCode, testServer, toPath);
        testServer.registerGet(fromPath, response);
        testServer.registerPatch(fromPath, response);
        testServer.registerPost(fromPath, response);
        testServer.registerPut(fromPath, response);
        testServer.registerDelete(fromPath, response);
    }

    public void start() {
        testServer.startRandomPort();
    }

    public void stop() {
        testServer.stop();
    }

    public URI getUri() {
        return testServer.getUri();
    }

    public void registerGet(String relativeUrl, TestServerResponse response) {
        testServer.registerGet(relativeUrl, response);
    }

    public void registerPatch(String relativeUrl, TestServerResponse response) {
        testServer.registerPatch(relativeUrl, response);
    }

    public void registerPost(String relativeUrl, TestServerResponse response) {
        testServer.registerPost(relativeUrl, response);
    }

    public void registerPut(String relativeUrl, TestServerResponse response) {
        testServer.registerPut(relativeUrl, response);
    }

    public void registerDelete(String relativeUrl, TestServerResponse response) {
        testServer.registerDelete(relativeUrl, response);
    }

    private static TestServerJsonResponse jsonResponse(String resourceName) {
        return new TestServerJsonResponse(ResourceUtils.textContent(resourceName), 200, Collections.emptyMap());
    }

    private static TestServerJsonResponse jsonResponse(String resourceName, int statusCode) {
        return new TestServerJsonResponse(ResourceUtils.textContent(resourceName), statusCode, Collections.emptyMap());
    }

    private static TestServerJsonResponse jsonResponse(String resourceName, int statusCode, Map<String, String> headerResponse) {
        return new TestServerJsonResponse(ResourceUtils.textContent(resourceName), statusCode, headerResponse);
    }
}
