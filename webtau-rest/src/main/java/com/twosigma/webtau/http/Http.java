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

package com.twosigma.webtau.http;

import com.twosigma.webtau.data.traceable.CheckLevel;
import com.twosigma.webtau.data.traceable.TraceableValue;
import com.twosigma.webtau.expectation.ExpectationHandler;
import com.twosigma.webtau.expectation.ExpectationHandlers;
import com.twosigma.webtau.http.config.HttpConfigurations;
import com.twosigma.webtau.http.datacoverage.DataNodeToMapOfValuesConverter;
import com.twosigma.webtau.http.datanode.DataNode;
import com.twosigma.webtau.http.datanode.DataNodeBuilder;
import com.twosigma.webtau.http.datanode.DataNodeId;
import com.twosigma.webtau.http.datanode.StructuredDataNode;
import com.twosigma.webtau.http.json.JsonRequestBody;
import com.twosigma.webtau.http.render.DataNodeAnsiPrinter;
import com.twosigma.webtau.http.validation.HeaderDataNode;
import com.twosigma.webtau.http.validation.HttpResponseValidator;
import com.twosigma.webtau.http.validation.HttpResponseValidatorIgnoringReturn;
import com.twosigma.webtau.http.validation.HttpResponseValidatorWithReturn;
import com.twosigma.webtau.http.validation.HttpValidationHandlers;
import com.twosigma.webtau.http.validation.HttpValidationResult;
import com.twosigma.webtau.reporter.StepReportOptions;
import com.twosigma.webtau.reporter.TestStep;
import com.twosigma.webtau.reporter.stacktrace.StackTraceUtils;
import com.twosigma.webtau.utils.JsonParseException;
import com.twosigma.webtau.utils.JsonUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.twosigma.webtau.Ddjt.equal;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.action;
import static com.twosigma.webtau.reporter.IntegrationTestsMessageBuilder.urlValue;
import static com.twosigma.webtau.reporter.TokenizedMessage.tokenizedMessage;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

public class Http {
    private static final HttpResponseValidator EMPTY = (header, body) -> {};

    public static final Http http = new Http();

    public final HttpDocumentation doc = new HttpDocumentation();

    private final ThreadLocal<HttpValidationResult> lastValidationResult = new ThreadLocal<>();

    public <E> E get(String url, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("GET", url,
                this::getToFullUrl,
                HttpRequestHeader.EMPTY, null, validator);
    }

    public void get(String url, HttpResponseValidator validator) {
        get(url, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E get(String url, HttpQueryParams queryParams, HttpResponseValidatorWithReturn validator) {
        return get(url + "?" + queryParams.toString(), validator);
    }

    public void get(String url, HttpQueryParams queryParams, HttpResponseValidator validator) {
        get(url, queryParams, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E post(String url, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("POST", url,
                (fullUrl, fullHeader) -> postToFullUrl(fullUrl, fullHeader, requestBody),
                HttpRequestHeader.EMPTY,
                requestBody,
                validator);
    }

    public void post(String url, HttpRequestBody requestBody, HttpResponseValidator validator) {
        post(url, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, Map<String, Object> requestBody, HttpResponseValidator validator) {
        post(url, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E post(String url, Map<String, Object> requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, new JsonRequestBody(requestBody), validator);
    }

    public void post(String url, HttpRequestBody requestBody) {
        post(url, requestBody, EMPTY);
    }

    public <E> E put(String url, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("PUT", url,
                (fullUrl, fullHeader) -> putToFullUrl(fullUrl, fullHeader, requestBody),
                HttpRequestHeader.EMPTY,
                requestBody,
                validator);
    }

    public void put(String url, HttpRequestBody requestBody, HttpResponseValidator validator) {
        put(url, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, Map<String, Object> requestBody, HttpResponseValidator validator) {
        put(url, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E delete(String url, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("DELETE", url,
                this::deleteToFullUrl,
                HttpRequestHeader.EMPTY,
                null,
                validator);
    }

    public void delete(String url, HttpResponseValidator validator) {
        delete(url, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void delete(String url) {
        delete(url, EMPTY);
    }

    public HttpValidationResult getLastValidationResult() {
        return lastValidationResult.get();
    }

    public HttpResponse getToFullUrl(String fullUrl, HttpRequestHeader requestHeader) {
        return requestWithoutBody("GET", fullUrl, requestHeader);
    }

    public HttpResponse deleteToFullUrl(String fullUrl, HttpRequestHeader requestHeader) {
        return requestWithoutBody("DELETE", fullUrl, requestHeader);
    }

    public HttpResponse postToFullUrl(String fullUrl, HttpRequestHeader requestHeader, HttpRequestBody requestBody) {
        return requestWithBody("POST", fullUrl, requestHeader, requestBody);
    }

    public HttpResponse putToFullUrl(String fullUrl, HttpRequestHeader requestHeader, HttpRequestBody requestBody) {
        return requestWithBody("PUT", fullUrl, requestHeader, requestBody);
    }

    private <R> R executeAndValidateHttpCall(String requestMethod, String url, HttpCall httpCall,
                                             HttpRequestHeader requestHeader,
                                             HttpRequestBody requestBody,
                                             HttpResponseValidatorWithReturn validator) {
        String fullUrl = HttpConfigurations.fullUrl(url);
        HttpRequestHeader fullHeader = HttpConfigurations.fullHeader(requestHeader);

        HttpValidationResult validationResult = new HttpValidationResult(requestMethod, fullUrl, requestHeader, requestBody);

        TestStep<Void, R> step = createHttpStep(validationResult, requestMethod, fullUrl, httpCall, fullHeader, validator);
        try {
            return step.execute(StepReportOptions.REPORT_ALL);
        } finally {
            lastValidationResult.set(validationResult);

            HttpValidationResult payload = lastValidationResult.get();
            if (payload != null) {
                step.addPayload(payload);
            }
        }
    }

    private <R> TestStep<Void, R> createHttpStep(HttpValidationResult validationResult,
                                                 String requestMethod, String fullUrl, HttpCall httpCall,
                                                 HttpRequestHeader fullRequestHeader,
                                                 HttpResponseValidatorWithReturn validator) {
        Supplier<R> httpCallSupplier = () -> {
            try {
                long startTime = System.currentTimeMillis();
                HttpResponse response = httpCall.execute(fullUrl, fullRequestHeader);
                long endTime = System.currentTimeMillis();

                validationResult.setElapsedTime(endTime - startTime);
                validationResult.setResponse(response);

                R validationBlockReturnedValue = validateAndRecord(validationResult, validator);

                if (validationResult.hasMismatches()) {
                    throw new AssertionError("\n" + validationResult.renderMismatches());
                }

                return validationBlockReturnedValue;
            } catch (AssertionError e) {
                throw e;
            } catch (Throwable e) {
                validationResult.setErrorMessage(StackTraceUtils.fullCauseMessage(e));
                throw new HttpException("error during http." + requestMethod.toLowerCase() + "(" + fullUrl + ")", e);
            }
        };

        return TestStep.create(null, tokenizedMessage(action("executing HTTP " + requestMethod), urlValue(fullUrl)),
                () -> tokenizedMessage(action("executed HTTP " + requestMethod), urlValue(fullUrl)),
                httpCallSupplier);
    }

    @SuppressWarnings("unchecked")
    private <R> R validateAndRecord(HttpValidationResult validationResult,
                                    HttpResponseValidatorWithReturn validator) {
        HeaderDataNode header = createHeaderDataNode(validationResult.getResponse());
        DataNode body = createBodyDataNode(validationResult.getResponse());

        validationResult.setResponseHeaderNode(header);
        validationResult.setResponseBodyNode(body);

        HttpValidationHandlers.validate(validationResult);

        ExpectationHandler expectationHandler = (actualPath, actualValue, message) -> {
            validationResult.addMismatch(message);
            return ExpectationHandler.Flow.Terminate;
        };

        try {
            return ExpectationHandlers.withAdditionalHandler(expectationHandler, () -> {
                Object returnedValue = validator.validate(header, body);
                validateStatusCode(validationResult);
                return (R) extractOriginalValue(returnedValue);
            });
        } finally {
            render(validationResult);
        }
    }

    private void validateStatusCode(HttpValidationResult validationResult) {
        DataNode statusCode = validationResult.getHeaderNode().statusCode();
        if (statusCode.get().getCheckLevel() != CheckLevel.None) {
            return;
        }

        statusCode.should(equal(defaultExpectedStatusCodeByRequest(validationResult)));
    }

    private Integer defaultExpectedStatusCodeByRequest(HttpValidationResult validationResult) {
        boolean noContent = validationResult.getResponseContent().isEmpty();

        switch (validationResult.getRequestMethod()) {
            case "GET":
                return 200;
            case "POST":
                return 201;
            case "PUT":
            case "DELETE":
                return noContent ? 204 : 200;
            default:
                return 200;
        }
    }

    private void render(HttpValidationResult result) {
        new DataNodeAnsiPrinter().print(result.getBodyNode());
    }

    private HttpResponse requestWithoutBody(String method, String fullUrl, HttpRequestHeader requestHeader) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(fullUrl).openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            requestHeader.forEachProperty(connection::setRequestProperty);

            connection.connect();

            return extractHttpResponse(connection);
        } catch (IOException e) {
            throw new RuntimeException("couldn't " + method + ": " + fullUrl, e);
        }
    }

    private HttpResponse requestWithBody(String method, String fullUrl,
                                         HttpRequestHeader requestHeader,
                                         HttpRequestBody requestBody) {
        if (requestBody.isBinary()) {
            throw new UnsupportedOperationException("binary is not supported yet");
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(fullUrl).openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", requestBody.type());
            connection.setRequestProperty("Accept", requestBody.type());
            requestHeader.forEachProperty(connection::setRequestProperty);
            connection.setDoOutput(true);

            IOUtils.write(requestBody.asString(), connection.getOutputStream(), UTF_8);

            return extractHttpResponse(connection);
        } catch (IOException e) {
            throw new RuntimeException("couldn't " + method + ": " + fullUrl, e);
        }
    }

    private HttpResponse extractHttpResponse(HttpURLConnection connection) throws IOException {
        HttpResponse httpResponse = new HttpResponse();

        InputStream inputStream = connection.getResponseCode() < 400 ? connection.getInputStream() : connection.getErrorStream();
        httpResponse.setStatusCode(connection.getResponseCode());
        httpResponse.setContent(inputStream != null ? IOUtils.toString(inputStream, StandardCharsets.UTF_8) : "");
        httpResponse.setContentType(connection.getContentType() != null ? connection.getContentType() : "");

        return httpResponse;
    }

    private static HeaderDataNode createHeaderDataNode(HttpResponse response) {
        Map<String, Object> headerData = new LinkedHashMap<>();
        headerData.put("statusCode", response.getStatusCode());
        headerData.put("contentType", response.getContentType());

        return new HeaderDataNode(DataNodeBuilder.fromMap(new DataNodeId("header"), headerData));
    }

    @SuppressWarnings("unchecked")
    private static DataNode createBodyDataNode(HttpResponse response) {
        try {
            DataNodeId id = new DataNodeId("body");
            if (response.getContent().isEmpty()) {
                return new StructuredDataNode(id, new TraceableValue(""));
            }

            if (!response.getContentType().contains("/json")) {
                return new StructuredDataNode(id, new TraceableValue(response.getContent()));
            }

            Object mapOrList = JsonUtils.deserialize(response.getContent());

            return mapOrList instanceof List ?
                    DataNodeBuilder.fromList(id, (List<Object>) mapOrList) :
                    DataNodeBuilder.fromMap(id, (Map<String, Object>) mapOrList);
        } catch (JsonParseException e) {
            throw new RuntimeException("error parsing body: " + response.getContent(), e);
        }
    }

    /**
     * Response consist of DataNode and Traceable values but we need to return back a simple value that can be used for
     * regular calculations and to drive test flow
     *
     * @param v value returned from a validation callback
     * @return extracted regular value
     */
    @SuppressWarnings("unchecked")
    private Object extractOriginalValue(Object v) {
        if (v instanceof DataNode) {
            return new DataNodeToMapOfValuesConverter((id, traceableValue) -> traceableValue.getValue())
                    .convert((DataNode) v);
        }

        if (v instanceof TraceableValue) {
            return ((TraceableValue) v).getValue();
        }

        if (v instanceof List) {
            return ((List) v).stream().map(this::extractOriginalValue).collect(toList());
        }

        return v;
    }

    private interface HttpCall {
        HttpResponse execute(String fullUrl, HttpRequestHeader fullHeader);
    }
}
