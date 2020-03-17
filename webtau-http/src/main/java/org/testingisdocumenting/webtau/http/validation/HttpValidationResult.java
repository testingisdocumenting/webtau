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

package org.testingisdocumenting.webtau.http.validation;

import org.testingisdocumenting.webtau.data.traceable.CheckLevel;
import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.http.HttpResponse;
import org.testingisdocumenting.webtau.http.datacoverage.DataNodeToMapOfValuesConverter;
import org.testingisdocumenting.webtau.http.datacoverage.TraceableValueConverter;
import org.testingisdocumenting.webtau.http.datanode.DataNode;
import org.testingisdocumenting.webtau.reporter.TestStepPayload;
import org.testingisdocumenting.webtau.utils.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HttpValidationResult implements TestStepPayload {
    private static final AtomicInteger idCounter = new AtomicInteger();
    private static final String BINARY_CONTENT_PLACEHOLDER = "[binary content]";

    private final String id;
    private final String url;
    private final String fullUrl;
    private final String requestMethod;
    private HttpHeader requestHeader;
    private final HttpRequestBody requestBody;

    private final List<String> mismatches;

    private HttpResponse response;
    private HeaderDataNode responseHeaderNode;
    private DataNode responseBodyNode;
    private long startTime;
    private long elapsedTime;
    private String errorMessage;

    public HttpValidationResult(String requestMethod,
                                String url,
                                String fullUrl,
                                HttpHeader requestHeader,
                                HttpRequestBody requestBody) {
        this.id = generateId();
        this.requestMethod = requestMethod;
        this.url = url;
        this.fullUrl = fullUrl;
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
        this.mismatches = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public HttpHeader getRequestHeader() {
        return requestHeader;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public void setResponseHeaderNode(HeaderDataNode responseHeader) {
        this.responseHeaderNode = responseHeader;
    }

    public void setResponseBodyNode(DataNode responseBody) {
        this.responseBodyNode = responseBody;
    }

    public List<String> getFailedPaths() {
        return extractPaths(responseBodyNode, CheckLevel::isFailed);
    }

    public List<String> getPassedPaths() {
        return extractPaths(responseBodyNode, CheckLevel::isPassed);
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getRequestType() {
        return requestBody != null ? requestBody.type() : null;
    }

    public boolean isRequestBinary() {
        return requestBody != null && requestBody.isBinary();
    }

    public String getResponseType() {
        return response.getContentType();
    }

    public String getRequestContent() {
        return requestBody != null ? requestBody.asString() : null;
    }

    public HttpRequestBody getRequestBody() {
        return requestBody;
    }

    public boolean nullOrEmptyRequestContent() {
        return StringUtils.nullOrEmpty(getRequestContent());
    }

    public String getResponseTextContent() {
        return response.getTextContent();
    }

    public boolean hasResponseContent() {
        return response.hasContent();
    }

    public int getResponseStatusCode() {
        return response.getStatusCode();
    }

    public void addMismatch(String message) {
        mismatches.add(message);
    }

    public List<String> getMismatches() {
        return mismatches;
    }

    public boolean hasMismatches() {
        return !mismatches.isEmpty();
    }

    public String renderMismatches() {
        return String.join("\n", mismatches);
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getUrl() {
        return url;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public HeaderDataNode getHeaderNode() {
        return responseHeaderNode;
    }

    public DataNode getBodyNode() {
        return responseBodyNode;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    @Override
    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("id", id);
        result.put("method", requestMethod);
        result.put("url", fullUrl);

        result.put("startTime", startTime);
        result.put("elapsedTime", elapsedTime);
        result.put("errorMessage", errorMessage);
        result.put("mismatches", mismatches);

        result.put("requestHeader", requestHeader.redactSecrets().toListOfMaps());

        if (requestBody != null) {
            result.put("requestType", requestBody.type());
            result.put("requestBody", requestBody.isBinary() ? BINARY_CONTENT_PLACEHOLDER : requestBody.asString());
        }

        if (response != null) {
            result.put("responseType", response.getContentType());
            result.put("responseStatusCode", response.getStatusCode());
            result.put("responseHeader", response.getHeader().redactSecrets().toListOfMaps());
            result.put("responseBody", response.isBinary() ? BINARY_CONTENT_PLACEHOLDER : response.getTextContent());
        }

        if (responseBodyNode != null) {
            Map<String, Object> responseBodyChecks = new LinkedHashMap<>();
            result.put("responseBodyChecks", responseBodyChecks);
            responseBodyChecks.put("failedPaths", getFailedPaths());
            responseBodyChecks.put("passedPaths", getPassedPaths());
        }

        return result;
    }

    private List<String> extractPaths(DataNode dataNode, Function<CheckLevel, Boolean> includePath) {
        List<String> paths = new ArrayList<>();

        TraceableValueConverter traceableValueConverter = (id, traceableValue) -> {
            if (includePath.apply(traceableValue.getCheckLevel())) {
                paths.add(replaceStartOfThePath(id.getPath()));
            }

            return traceableValue.getValue();
        };

        DataNodeToMapOfValuesConverter dataNodeConverter = new DataNodeToMapOfValuesConverter(traceableValueConverter);
        dataNodeConverter.convert(dataNode);

        return paths;
    }

    private static String replaceStartOfThePath(String path) {
        if (path.startsWith("body")) {
            return path.replace("body", "root");
        }

        if (path.startsWith("header")) {
            return path.replace("header", "root");
        }

        throw new RuntimeException("path should start with either header or body");
    }

    private String generateId() {
        return "httpCall-" + idCounter.incrementAndGet();
    }
}
