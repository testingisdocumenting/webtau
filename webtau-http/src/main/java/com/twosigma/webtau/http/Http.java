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

import com.twosigma.webtau.console.ConsoleOutputs;
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
import com.twosigma.webtau.http.multipart.MultiPartFile;
import com.twosigma.webtau.http.multipart.MultiPartFormData;
import com.twosigma.webtau.http.multipart.MultiPartFormField;
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
import com.twosigma.webtau.utils.CollectionUtils;
import com.twosigma.webtau.utils.JsonParseException;
import com.twosigma.webtau.utils.JsonUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
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
    private static final HttpResponseValidatorWithReturn EMPTY_RESPONSE_VALIDATOR = (header, body) -> null;

    public static final Http http = new Http();

    public final HttpDocumentation doc = new HttpDocumentation();

    private final ThreadLocal<HttpValidationResult> lastValidationResult = new ThreadLocal<>();

    public <E> E get(String url, HttpQueryParams queryParams, HttpRequestHeader header, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("GET", queryParams.attachToUrl(url),
                this::getToFullUrl,
                header, null, validator);
    }

    public void get(String url, HttpQueryParams queryParams, HttpRequestHeader header, HttpResponseValidator validator) {
        get(url, queryParams, header, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E get(String url, HttpQueryParams queryParams, HttpResponseValidatorWithReturn validator) {
        return get(url, queryParams, HttpRequestHeader.EMPTY, validator);
    }

    public void get(String url, HttpQueryParams queryParams, HttpResponseValidator validator) {
        get(url, queryParams, HttpRequestHeader.EMPTY, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E get(String url, HttpRequestHeader header, HttpResponseValidatorWithReturn validator) {
        return get(url, HttpQueryParams.EMPTY, header, validator);
    }

    public void get(String url, HttpRequestHeader header, HttpResponseValidator validator) {
        get(url, HttpQueryParams.EMPTY, header, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E get(String url, HttpResponseValidatorWithReturn validator) {
        return get(url, HttpQueryParams.EMPTY, HttpRequestHeader.EMPTY, validator);
    }

    public void get(String url, HttpResponseValidator validator) {
        get(url, HttpQueryParams.EMPTY, HttpRequestHeader.EMPTY, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E post(String url, HttpRequestHeader header, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("POST", url,
                (fullUrl, fullHeader) -> postToFullUrl(fullUrl, fullHeader, requestBody),
                header,
                requestBody,
                validator);
    }

    public void post(String url, HttpRequestHeader header, HttpRequestBody requestBody, HttpResponseValidator validator) {
        post(url, header, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, HttpRequestHeader header, HttpResponseValidator validator) {
        post(url, header, EmptyRequestBody.INSTANCE, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E post(String url, HttpRequestHeader header, HttpResponseValidatorWithReturn validator) {
        return post(url, header, EmptyRequestBody.INSTANCE, validator);
    }

    public <E> E post(String url, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, HttpRequestHeader.EMPTY, requestBody, validator);
    }

    public void post(String url, HttpRequestBody requestBody, HttpResponseValidator validator) {
        post(url, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, Map<String, Object> requestBody, HttpResponseValidator validator) {
        post(url, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, HttpResponseValidator validator) {
        post(url, EmptyRequestBody.INSTANCE, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E post(String url, HttpResponseValidatorWithReturn validator) {
        return post(url, EmptyRequestBody.INSTANCE, validator);
    }

    public void post(String url, HttpRequestHeader header) {
        post(url, header, EMPTY_RESPONSE_VALIDATOR);
    }

    public void post(String url) {
        post(url, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E post(String url, Map<String, Object> requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, new JsonRequestBody(requestBody), validator);
    }

    public void post(String url, HttpRequestBody requestBody) {
        post(url, requestBody, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E put(String url, HttpRequestHeader header, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("PUT", url,
                (fullUrl, fullHeader) -> putToFullUrl(fullUrl, fullHeader, requestBody),
                header,
                requestBody,
                validator);
    }

    public void put(String url, HttpRequestHeader header, HttpRequestBody requestBody, HttpResponseValidator validator) {
        put(url, header, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E put(String url, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return put(url, HttpRequestHeader.EMPTY, requestBody, validator);
    }

    public void put(String url, HttpRequestBody requestBody, HttpResponseValidator validator) {
        put(url, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E put(String url, HttpResponseValidatorWithReturn validator) {
        return put(url, HttpRequestHeader.EMPTY, EmptyRequestBody.INSTANCE, validator);
    }

    public void put(String url, HttpResponseValidator validator) {
        put(url, HttpRequestHeader.EMPTY, EmptyRequestBody.INSTANCE, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, HttpRequestHeader header, Map<String, Object> requestBody, HttpResponseValidator validator) {
        put(url, header, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E put(String url, Map<String, Object> requestBody, HttpResponseValidatorWithReturn validator) {
        return put(url, HttpRequestHeader.EMPTY, new JsonRequestBody(requestBody), validator);
    }

    public void put(String url, Map<String, Object> requestBody, HttpResponseValidator validator) {
        put(url, HttpRequestHeader.EMPTY, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E put(String url, HttpRequestHeader header, HttpResponseValidatorWithReturn validator) {
        return put(url, header, EmptyRequestBody.INSTANCE, validator);
    }

    public void put(String url, HttpRequestHeader header, HttpResponseValidator validator) {
        put(url, header, EmptyRequestBody.INSTANCE, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, HttpRequestHeader header) {
        put(url, header, EmptyRequestBody.INSTANCE, EMPTY_RESPONSE_VALIDATOR);
    }

    public void put(String url) {
        put(url, HttpRequestHeader.EMPTY, EmptyRequestBody.INSTANCE, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E delete(String url, HttpRequestHeader header, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("DELETE", url,
                this::deleteToFullUrl,
                header,
                null,
                validator);
    }

    public void delete(String url, HttpRequestHeader header, HttpResponseValidator validator) {
        delete(url, header, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E delete(String url, HttpResponseValidatorWithReturn validator) {
        return delete(url, HttpRequestHeader.EMPTY, validator);
    }

    public void delete(String url, HttpResponseValidator validator) {
        delete(url, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void delete(String url, HttpRequestHeader header) {
        delete(url, header, EMPTY_RESPONSE_VALIDATOR);
    }

    public void delete(String url) {
        delete(url, EMPTY_RESPONSE_VALIDATOR);
    }

    public HttpRequestHeader header(String... properties) {
        return new HttpRequestHeader(CollectionUtils.createMap((Object[]) properties));
    }

    public HttpRequestHeader header(Map<String, String> properties) {
        return new HttpRequestHeader(properties);
    }

    public MultiPartFormData formData(MultiPartFormField... fields) {
        MultiPartFormData formData = new MultiPartFormData();
        Arrays.stream(fields).forEach(formData::addField);

        return formData;
    }

    public MultiPartFormData formData(Map<String, Object> fields) {
        return new MultiPartFormData(fields);
    }

    public MultiPartFormField formField(byte[] fileContent) {
        return formField("file", fileContent, null);
    }

    public MultiPartFormField formField(String fieldName, byte[] fileContent) {
        return formField(fieldName, fileContent, null);
    }

    public MultiPartFormField formField(String fieldName, Path file) {
        return formField(fieldName, file, file.getFileName().toString());
    }

    public MultiPartFormField formField(String fieldName, Path file, String fileName) {
        return MultiPartFormField.fileFormField(fieldName, file, fileName);
    }

    public MultiPartFormField formField(String fieldName, byte[] fileContent, String fileName) {
        return MultiPartFormField.binaryFormField(fieldName, fileContent, fileName);
    }

    public MultiPartFormField formField(String fieldName, String textContent, String fileName) {
        return MultiPartFormField.textFormField(fieldName, textContent, fileName);
    }

    public MultiPartFormField formField(String fieldName, String textContent) {
        return formField(fieldName, textContent, null);
    }

    public MultiPartFile formFile(String fileName, byte[] fileContent) {
        return new MultiPartFile(fileName, fileContent);
    }

    public MultiPartFile formFile(String fileName, Path file) {
        return new MultiPartFile(fileName, file);
    }

    public HttpValidationResult getLastValidationResult() {
        return lastValidationResult.get();
    }

    public HttpResponse getToFullUrl(String fullUrl, HttpRequestHeader requestHeader) {
        return request("GET", fullUrl, requestHeader, EmptyRequestBody.INSTANCE);
    }

    public HttpResponse deleteToFullUrl(String fullUrl, HttpRequestHeader requestHeader) {
        return request("DELETE", fullUrl, requestHeader, EmptyRequestBody.INSTANCE);
    }

    public HttpResponse postToFullUrl(String fullUrl, HttpRequestHeader requestHeader, HttpRequestBody requestBody) {
        return request("POST", fullUrl, requestHeader, requestBody);
    }

    public HttpResponse putToFullUrl(String fullUrl, HttpRequestHeader requestHeader, HttpRequestBody requestBody) {
        return request("PUT", fullUrl, requestHeader, requestBody);
    }

    private <R> R executeAndValidateHttpCall(String requestMethod, String url, HttpCall httpCall,
                                             HttpRequestHeader requestHeader,
                                             HttpRequestBody requestBody,
                                             HttpResponseValidatorWithReturn validator) {
        String fullUrl = HttpConfigurations.fullUrl(url);
        HttpRequestHeader fullHeader = HttpConfigurations.fullHeader(fullUrl, url, requestHeader);

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

        ExpectationHandler expectationHandler = (valueMatcher, actualPath, actualValue, message) -> {
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
        switch (validationResult.getRequestMethod()) {
            case "GET":
                return 200;
            case "POST":
                return 201;
            case "PUT":
            case "DELETE":
                return validationResult.hasContent() ? 200 : 204;
            default:
                return 200;
        }
    }

    private void render(HttpValidationResult result) {
        if (result.getResponse().isBinary()) {
            ConsoleOutputs.out("binary content, size: " + (result.getResponse().getBinaryContent()).length);
        } else {
            new DataNodeAnsiPrinter().print(result.getBodyNode());
        }
    }

    private HttpResponse request(String method, String fullUrl,
                                 HttpRequestHeader requestHeader,
                                 HttpRequestBody requestBody) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(fullUrl).openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", requestBody.type());
            connection.setRequestProperty("Accept", requestBody.type());
            requestHeader.forEachProperty(connection::setRequestProperty);

            if (! (requestBody instanceof EmptyRequestBody)) {
                connection.setDoOutput(true);

                if (requestBody.isBinary()) {
                    connection.getOutputStream().write(requestBody.asBytes());
                } else {
                    IOUtils.write(requestBody.asString(), connection.getOutputStream(), UTF_8);
                }
            }

            return extractHttpResponse(connection);
        } catch (IOException e) {
            throw new RuntimeException("couldn't " + method + ": " + fullUrl, e);
        }
    }

    private HttpResponse extractHttpResponse(HttpURLConnection connection) throws IOException {
        HttpResponse httpResponse = new HttpResponse();

        InputStream inputStream = connection.getResponseCode() < 400 ? connection.getInputStream() : connection.getErrorStream();
        httpResponse.setStatusCode(connection.getResponseCode());
        httpResponse.setContentType(connection.getContentType() != null ? connection.getContentType() : "");

        if (!httpResponse.isBinary()) {
            httpResponse.setTextContent(inputStream != null ? IOUtils.toString(inputStream, StandardCharsets.UTF_8) : "");
        } else {
            httpResponse.setBinaryContent(inputStream != null ? IOUtils.toByteArray(inputStream) : new byte[0]);
        }

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

            if (!response.isBinary() && response.nullOrEmptyTextContent()) {
                return new StructuredDataNode(id, new TraceableValue(null));
            }

            if (response.isText()) {
                return new StructuredDataNode(id, new TraceableValue(response.getTextContent()));
            }

            if (response.isJson()) {
                Object mapOrList = JsonUtils.deserialize(response.getTextContent());

                return mapOrList instanceof List ?
                        DataNodeBuilder.fromList(id, (List<Object>) mapOrList) :
                        DataNodeBuilder.fromMap(id, (Map<String, Object>) mapOrList);
            }

            return new StructuredDataNode(id, new TraceableValue(response.getBinaryContent()));
        } catch (JsonParseException e) {
            throw new RuntimeException("error parsing body: " + response.getTextContent(), e);
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
