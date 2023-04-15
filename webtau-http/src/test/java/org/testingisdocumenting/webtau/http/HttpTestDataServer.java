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

package org.testingisdocumenting.webtau.http;

import org.testingisdocumenting.webtau.http.testserver.*;
import org.testingisdocumenting.webtau.utils.CollectionUtils;
import org.testingisdocumenting.webtau.utils.ResourceUtils;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class HttpTestDataServer {
    private final FixedResponsesHandler handler = new FixedResponsesHandler();
    private final TestServer testServer = new TestServer(handler);

    public static TestServerJsonLiveResponse IBM_PRICES = new TestServerJsonLiveResponse(Arrays.asList(
            "{\"price\": 100}",
            "{\"price\": 110}",
            "{\"price\": 120}"));

    public HttpTestDataServer() {
        TestServerJsonResponse queryTestResponse = jsonResponse("queryTestResponse.json");
        TestServerJsonResponse objectTestResponse = jsonResponse("objectTestResponse.json");

        handler.registerGet("/end-point", objectTestResponse);
        handler.registerGet("/end-point/id1", jsonResponse("objectTestResponseA.json"));
        handler.registerGet("/end-point/id2", jsonResponse("objectTestResponseB.json"));
        handler.registerGet("/end-point/10/20", jsonResponse("objectTestResponseA.json"));
        handler.registerGet("/end-point?a=1&b=text", objectTestResponse);
        handler.registerGet("/end-point?queryParam1=queryParamValue1", objectTestResponse);

        handler.registerGet("/prices/IBM", IBM_PRICES);

        handler.registerPost("/end-point", jsonResponse("objectTestResponse.json", 201,
                CollectionUtils.map(
                        "Content-Location", "/url/23",
                        "Location", "http://www.example.org/url/23")));

        handler.registerGet("/query", queryTestResponse);
        handler.registerGet("/query?q1=v1", queryTestResponse);

        handler.registerGet("/example", jsonResponse("matcherExampleResponse.json"));
        handler.registerGet("/customer/123", objectTestResponse);

        handler.registerPut("/end-point", objectTestResponse);
        handler.registerPatch("/end-point", objectTestResponse);
        handler.registerDelete("/end-point", objectTestResponse);
        handler.registerGet("/text-end-point", new TestServerTextResponse("hello world"));
        handler.registerGet("/end-point-simple-object", jsonResponse("simpleObjectTestResponse.json"));
        handler.registerGet("/end-point-simple-list", jsonResponse("simpleListTestResponse.json"));
        handler.registerGet("/end-point-large-list", jsonResponse("largeListTestResponse.json"));
        handler.registerGet("/end-point-mixed", jsonResponse("mixedTestResponse.json"));
        handler.registerGet("/end-point-numbers", jsonResponse("numbersTestResponse.json"));
        handler.registerGet("/end-point-list", jsonResponse("listTestResponse.json"));
        handler.registerGet("/end-point-dates", jsonResponse("datesTestResponse.json"));
        handler.registerGet("/large-numbers", jsonResponse("largeNumbersTestResponse.json"));
        handler.registerGet("/prices", jsonResponse("prices.json"));
        handler.registerGet("/binary", new TestServerBinaryResponse(ResourceUtils.binaryContent("image.png")));
        handler.registerPost("/echo", new TestServerResponseEcho(201));
        handler.registerPut("/echo", new TestServerResponseEcho(200));
        handler.registerPatch("/echo", new TestServerResponseEcho(200));
        handler.registerGet("/full-echo", new TestServerResponseFullEcho(200));
        handler.registerGet("/full-echo?a=1&b=text", new TestServerResponseFullEcho(200));
        handler.registerPut("/full-echo", new TestServerResponseFullEcho(200));
        handler.registerPut("/full-echo?a=1&b=text", new TestServerResponseFullEcho(200));
        handler.registerPost("/full-echo", new TestServerResponseFullEcho(201));
        handler.registerPost("/full-echo?a=1&b=text", new TestServerResponseFullEcho(201));
        handler.registerPatch("/full-echo", new TestServerResponseFullEcho(200));
        handler.registerPatch("/full-echo?a=1&b=text", new TestServerResponseFullEcho(200));
        handler.registerDelete("/full-echo", new TestServerResponseFullEcho(200));
        handler.registerDelete("/full-echo?a=1&b=text", new TestServerResponseFullEcho(200));
        handler.registerGet("/echo-header", new TestServerResponseHeaderEcho(200));
        handler.registerGet("/echo-header?qp1=v1", new TestServerResponseHeaderEcho(200));
        handler.registerPatch("/echo-header", new TestServerResponseHeaderEcho(200));
        handler.registerPost("/echo-header", new TestServerResponseHeaderEcho(201));
        handler.registerPut("/echo-header", new TestServerResponseHeaderEcho(200));
        handler.registerPatch("/echo-header", new TestServerResponseHeaderEcho(200));
        handler.registerDelete("/echo-header", new TestServerResponseHeaderEcho(200));
        handler.registerPost("/echo-body-and-header", new TestServerResponseHeaderAndBodyEcho(201));
        handler.registerPost("/echo-multipart-content-part-one", new TestServerMultiPartContentEcho(201, 0));
        handler.registerPost("/echo-multipart-content-part-two", new TestServerMultiPartContentEcho(201, 1));
        handler.registerPost("/echo-multipart-meta", new TestServerMultiPartMetaEcho(201));
        handler.registerPost("/submit", new TestServerResponseFullEcho(201));
        handler.registerPost("/empty", new TestServerJsonResponse(null, 201));
        handler.registerPatch("/empty", new TestServerJsonResponse(null, 204));
        handler.registerPost("/file-upload", new TestServerFakeFileUpload());
        handler.registerDelete("/resource", new TestServerTextResponse("abc"));
        handler.registerGet("/path?a=1&b=text", new TestServerJsonResponse("{\"a\": 1, \"b\": \"text\"}"));
        handler.registerPost("/path?a=1&b=text", new TestServerJsonResponse("{\"a\": 1, \"b\": \"text\"}", 201));
        handler.registerGet("/path?message=hello+world+%21", new TestServerJsonResponse("{}", 200));
        handler.registerGet("/integer", new TestServerJsonResponse("123"));
        handler.registerPost("/json-derivative", new TestServerJsonDerivativeResponse());

        handler.registerPost("/resource", jsonResponse("chatPostResponse.json", 200));
        handler.registerPost("/chat", jsonResponse("chatPostResponse.json", 201));
        handler.registerPost("/chat?a=1&b=text", jsonResponse("chatPostResponse.json", 201));
        handler.registerPost("/chat?q1=v1", jsonResponse("chatPostResponse.json", 201));
        handler.registerPut("/chat/id1", jsonResponse("chatPostResponse.json", 200));
        handler.registerPut("/chat/id1?q1=v1", jsonResponse("chatPostResponse.json", 200));
        handler.registerPatch("/chat/id1", jsonResponse("chatPostResponse.json", 200));
        handler.registerPatch("/chat/id1?q1=v1", jsonResponse("chatPostResponse.json", 200));
        handler.registerDelete("/chat/id1", jsonResponse("chatPostResponse.json", 200));
        handler.registerDelete("/chat/id1?q1=v1", jsonResponse("chatPostResponse.json", 200));

        handler.registerGet("/address", jsonResponse("addressResponse.json"));

        handler.registerGet("/report",
                new TestServerBinaryResponse(ResourceUtils.binaryContent("report.pdf")));

        registerRedirects();
    }

    private void registerRedirects() {
        registerRedirectOnAllMethods(HttpURLConnection.HTTP_MOVED_TEMP, "/redirect", "/redirect2");
        registerRedirectOnAllMethods(HttpURLConnection.HTTP_MOVED_PERM, "/redirect2", "/redirect3");
        registerRedirectOnAllMethods(307, "/redirect3", "/redirect4");

        handler.registerGet("/redirect4", new TestServerRedirectResponse(HttpURLConnection.HTTP_SEE_OTHER, testServer, "/end-point"));
        handler.registerPost("/redirect4", new TestServerRedirectResponse(HttpURLConnection.HTTP_SEE_OTHER, testServer, "/echo"));
        handler.registerPut("/redirect4", new TestServerRedirectResponse(HttpURLConnection.HTTP_SEE_OTHER, testServer, "/echo"));
        handler.registerDelete("/redirect4", new TestServerRedirectResponse(HttpURLConnection.HTTP_SEE_OTHER, testServer, "/end-point"));

        handler.registerGet("/recursive", new TestServerRedirectResponse(HttpURLConnection.HTTP_MOVED_TEMP, testServer, "/recursive"));
    }

    private void registerRedirectOnAllMethods(int statusCode, String fromPath, String toPath) {
        TestServerRedirectResponse response = new TestServerRedirectResponse(statusCode, testServer, toPath);
        handler.registerGet(fromPath, response);
        handler.registerPatch(fromPath, response);
        handler.registerPost(fromPath, response);
        handler.registerPut(fromPath, response);
        handler.registerDelete(fromPath, response);
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
        handler.registerGet(relativeUrl, response);
    }

    public void registerPatch(String relativeUrl, TestServerResponse response) {
        handler.registerPatch(relativeUrl, response);
    }

    public void registerPost(String relativeUrl, TestServerResponse response) {
        handler.registerPost(relativeUrl, response);
    }

    public void registerPut(String relativeUrl, TestServerResponse response) {
        handler.registerPut(relativeUrl, response);
    }

    public void registerDelete(String relativeUrl, TestServerResponse response) {
        handler.registerDelete(relativeUrl, response);
    }

    private static String json(String resourceName) {
        return ResourceUtils.textContent(resourceName);
    }

    private static TestServerJsonResponse jsonResponse(String resourceName) {
        return new TestServerJsonResponse(json(resourceName), 200, Collections.emptyMap());
    }

    private static TestServerJsonResponse jsonResponse(String resourceName, int statusCode) {
        return new TestServerJsonResponse(json(resourceName), statusCode, Collections.emptyMap());
    }

    private static TestServerJsonResponse jsonResponse(String resourceName, int statusCode, Map<String, Object> headerResponse) {
        return new TestServerJsonResponse(json(resourceName), statusCode, headerResponse);
    }
}
