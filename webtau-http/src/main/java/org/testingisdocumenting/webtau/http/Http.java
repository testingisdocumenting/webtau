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

import static org.testingisdocumenting.webtau.WebTauCore.equal;
import static org.testingisdocumenting.webtau.cfg.WebTauConfig.getCfg;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.action;
import static org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder.urlValue;
import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

import org.testingisdocumenting.webtau.console.ConsoleOutputs;
import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.data.traceable.CheckLevel;
import org.testingisdocumenting.webtau.data.traceable.TraceableValue;
import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.ExpectationHandler;
import org.testingisdocumenting.webtau.expectation.ExpectationHandlers;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.http.binary.BinaryRequestBody;
import org.testingisdocumenting.webtau.http.config.HttpConfigurations;
import org.testingisdocumenting.webtau.http.datanode.DataNode;
import org.testingisdocumenting.webtau.http.datanode.DataNodeBuilder;
import org.testingisdocumenting.webtau.http.datanode.DataNodeId;
import org.testingisdocumenting.webtau.http.datanode.StructuredDataNode;
import org.testingisdocumenting.webtau.http.json.JsonRequestBody;
import org.testingisdocumenting.webtau.http.listener.HttpListeners;
import org.testingisdocumenting.webtau.http.multipart.MultiPartFile;
import org.testingisdocumenting.webtau.http.multipart.MultiPartFormData;
import org.testingisdocumenting.webtau.http.multipart.MultiPartFormField;
import org.testingisdocumenting.webtau.http.render.DataNodeAnsiPrinter;
import org.testingisdocumenting.webtau.http.request.EmptyRequestBody;
import org.testingisdocumenting.webtau.http.request.HttpApplicationMime;
import org.testingisdocumenting.webtau.http.request.HttpQueryParams;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.http.request.HttpTextMime;
import org.testingisdocumenting.webtau.http.text.TextRequestBody;
import org.testingisdocumenting.webtau.http.validation.HeaderDataNode;
import org.testingisdocumenting.webtau.http.validation.HttpResponseValidator;
import org.testingisdocumenting.webtau.http.validation.HttpResponseValidatorIgnoringReturn;
import org.testingisdocumenting.webtau.http.validation.HttpResponseValidatorWithReturn;
import org.testingisdocumenting.webtau.http.validation.HttpValidationHandlers;
import org.testingisdocumenting.webtau.http.validation.HttpValidationResult;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TestStep;
import org.testingisdocumenting.webtau.reporter.stacktrace.StackTraceUtils;
import org.testingisdocumenting.webtau.time.Time;
import org.testingisdocumenting.webtau.utils.CollectionUtils;
import org.testingisdocumenting.webtau.utils.JsonParseException;
import org.testingisdocumenting.webtau.utils.JsonUtils;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.io.IOUtils;
import sun.net.www.protocol.https.HttpsURLConnectionImpl;

public class Http {
    private static final HttpResponseValidatorWithReturn EMPTY_RESPONSE_VALIDATOR = (header, body) -> null;

    public static final Http http = new Http();

    public final HttpDocumentation doc = new HttpDocumentation();

    private final ThreadLocal<HttpValidationResult> lastValidationResult = new ThreadLocal<>();

    public HttpApplicationMime application = new HttpApplicationMime();
    public HttpTextMime text = new HttpTextMime();

    public boolean ping(String url) {
        return ping(url, HttpQueryParams.EMPTY, HttpHeader.EMPTY);
    }

    public boolean ping(String url, HttpQueryParams queryParams) {
        return ping(url, queryParams, HttpHeader.EMPTY);
    }

    public boolean ping(String url, HttpHeader header) {
        return ping(url, HttpQueryParams.EMPTY, header);
    }

    public boolean ping(String url, HttpQueryParams queryParams, HttpHeader header) {
        String fullUrl = HttpConfigurations.fullUrl(queryParams.attachToUrl(url));
        TestStep step = TestStep.createStep(
                tokenizedMessage(action("pinging"), urlValue(fullUrl)),
                () -> tokenizedMessage(action("pinged"), urlValue(fullUrl)),
                () -> http.get(url, header));

        try {
            step.execute(StepReportOptions.REPORT_ALL);
        } catch (Throwable e) {
            return false;
        }

        return true;
    }

    public <E> E get(String url, HttpQueryParams queryParams, HttpHeader header, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("GET", queryParams.attachToUrl(url),
                this::getToFullUrl,
                header, null, validator);
    }

    public void get(String url, HttpQueryParams queryParams, HttpHeader header, HttpResponseValidator validator) {
        get(url, queryParams, header, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void get(String url, HttpQueryParams queryParams, HttpHeader header) {
        get(url, queryParams, header, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E get(String url, HttpQueryParams queryParams, HttpResponseValidatorWithReturn validator) {
        return get(url, queryParams, HttpHeader.EMPTY, validator);
    }

    public void get(String url, HttpQueryParams queryParams, HttpResponseValidator validator) {
        get(url, queryParams, HttpHeader.EMPTY, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void get(String url, HttpQueryParams queryParams) {
        get(url, queryParams, HttpHeader.EMPTY, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E get(String url, HttpHeader header, HttpResponseValidatorWithReturn validator) {
        return get(url, HttpQueryParams.EMPTY, header, validator);
    }

    public void get(String url, HttpHeader header, HttpResponseValidator validator) {
        get(url, HttpQueryParams.EMPTY, header, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void get(String url, HttpHeader header) {
        get(url, HttpQueryParams.EMPTY, header, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E get(String url, HttpResponseValidatorWithReturn validator) {
        return get(url, HttpQueryParams.EMPTY, HttpHeader.EMPTY, validator);
    }

    public void get(String url, HttpResponseValidator validator) {
        get(url, HttpQueryParams.EMPTY, HttpHeader.EMPTY, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void get(String url) {
        get(url, HttpQueryParams.EMPTY, HttpHeader.EMPTY, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E patch(String url, HttpQueryParams queryParams, HttpHeader header, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall(
            "PATCH",
            queryParams.attachToUrl(url),
            (fullUrl, fullHeader) -> patchToFullUrl(fullUrl, fullHeader, requestBody),
            header,
            requestBody,
            validator);
    }

    public <E> E patch(String url, HttpHeader header, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return patch(url, HttpQueryParams.EMPTY, header, requestBody, validator);
    }

    public void patch(String url, HttpQueryParams queryParams, HttpHeader header, HttpRequestBody requestBody, HttpResponseValidator validator) {
        patch(url, queryParams, header, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void patch(String url, HttpHeader header, HttpRequestBody requestBody, HttpResponseValidator validator) {
        patch(url, header, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E patch(String url, HttpQueryParams queryParams, HttpHeader header, HttpResponseValidatorWithReturn validator) {
        return patch(url, queryParams, header, EmptyRequestBody.INSTANCE, validator);
    }

    public <E> E patch(String url, HttpHeader header, HttpResponseValidatorWithReturn validator) {
        return patch(url, header, EmptyRequestBody.INSTANCE, validator);
    }

    public void patch(String url, HttpQueryParams queryParams, HttpHeader header, HttpResponseValidator validator) {
        patch(url, queryParams, header, EmptyRequestBody.INSTANCE, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void patch(String url, HttpHeader header, HttpResponseValidator validator) {
        patch(url, header, EmptyRequestBody.INSTANCE, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E patch(String url, HttpQueryParams queryParams, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return patch(url, queryParams, HttpHeader.EMPTY, requestBody, validator);
    }

    public <E> E patch(String url, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return patch(url, HttpHeader.EMPTY, requestBody, validator);
    }

    public void patch(String url, HttpQueryParams queryParams, HttpRequestBody requestBody, HttpResponseValidator validator) {
        patch(url, queryParams, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void patch(String url, HttpRequestBody requestBody, HttpResponseValidator validator) {
        patch(url, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E patch(String url, HttpQueryParams queryParams, HttpHeader header, Map<String, Object> requestBody,
        HttpResponseValidatorWithReturn validator) {
        return patch(url, queryParams, header, new JsonRequestBody(requestBody), validator);
    }

    public <E> E patch(String url, HttpHeader header, Map<String, Object> requestBody, HttpResponseValidatorWithReturn validator) {
        return patch(url, header, new JsonRequestBody(requestBody), validator);
    }

    public void patch(String url, HttpQueryParams queryParams, HttpHeader header, Map<String, Object> requestBody,
        HttpResponseValidator validator) {
        patch(url, queryParams, header, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void patch(String url, HttpHeader header, Map<String, Object> requestBody, HttpResponseValidator validator) {
        patch(url, header, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E patch(String url, HttpQueryParams queryParams, Map<String, Object> requestBody, HttpResponseValidatorWithReturn validator) {
        return patch(url, queryParams, HttpHeader.EMPTY, new JsonRequestBody(requestBody), validator);
    }

    public <E> E patch(String url, Map<String, Object> requestBody, HttpResponseValidatorWithReturn validator) {
        return patch(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), validator);
    }

    public void patch(String url, HttpQueryParams queryParams, Map<String, Object> requestBody, HttpResponseValidator validator) {
        patch(url, queryParams, HttpHeader.EMPTY, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void patch(String url, Map<String, Object> requestBody, HttpResponseValidator validator) {
        patch(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void patch(String url, Map<String, Object> requestBody) {
        patch(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E patch(String url, HttpQueryParams queryParams, HttpResponseValidatorWithReturn validator) {
        return patch(url, queryParams, EmptyRequestBody.INSTANCE, validator);
    }

    public <E> E patch(String url, HttpResponseValidatorWithReturn validator) {
        return patch(url, EmptyRequestBody.INSTANCE, validator);
    }

    public void patch(String url, HttpQueryParams queryParams, HttpResponseValidator validator) {
        patch(url, queryParams, EmptyRequestBody.INSTANCE, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void patch(String url, HttpResponseValidator validator) {
        patch(url, EmptyRequestBody.INSTANCE, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void patch(String url, HttpQueryParams queryParams, HttpHeader header) {
        patch(url, queryParams, header, EMPTY_RESPONSE_VALIDATOR);
    }

    public void patch(String url, HttpHeader header) {
        patch(url, header, EMPTY_RESPONSE_VALIDATOR);
    }

    public void patch(String url, HttpQueryParams queryParams) {
        patch(url, queryParams, EMPTY_RESPONSE_VALIDATOR);
    }

    public void patch(String url) {
        patch(url, EMPTY_RESPONSE_VALIDATOR);
    }

    public void patch(String url, HttpQueryParams queryParams, HttpRequestBody requestBody) {
        patch(url, queryParams, requestBody, EMPTY_RESPONSE_VALIDATOR);
    }

    public void patch(String url, HttpRequestBody requestBody) {
        patch(url, requestBody, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E post(String url, HttpQueryParams queryParams, HttpHeader header, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("POST", queryParams.attachToUrl(url),
                (fullUrl, fullHeader) -> postToFullUrl(fullUrl, fullHeader, requestBody),
                header,
                requestBody,
                validator);
    }

    public <E> E post(String url, HttpHeader header, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, HttpQueryParams.EMPTY, header, requestBody, validator);
    }

    public void post(String url, HttpQueryParams queryParams, HttpHeader header, HttpRequestBody requestBody, HttpResponseValidator validator) {
        post(url, queryParams, header, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, HttpQueryParams queryParams, HttpHeader header, HttpRequestBody requestBody) {
        post(url, queryParams, header, requestBody, EMPTY_RESPONSE_VALIDATOR);
    }

    public void post(String url, HttpHeader header, HttpRequestBody requestBody, HttpResponseValidator validator) {
        post(url, header, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, HttpHeader header, HttpRequestBody requestBody) {
        post(url, header, requestBody, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E post(String url, HttpQueryParams queryParams, HttpHeader header, HttpResponseValidatorWithReturn validator) {
        return post(url, queryParams, header, EmptyRequestBody.INSTANCE, validator);
    }

    public <E> E post(String url, HttpHeader header, HttpResponseValidatorWithReturn validator) {
        return post(url, header, EmptyRequestBody.INSTANCE, validator);
    }

    public void post(String url, HttpQueryParams queryParams, HttpHeader header, HttpResponseValidator validator) {
        post(url, queryParams, header, EmptyRequestBody.INSTANCE, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, HttpHeader header, HttpResponseValidator validator) {
        post(url, header, EmptyRequestBody.INSTANCE, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E post(String url, HttpQueryParams queryParams, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, queryParams, HttpHeader.EMPTY, requestBody, validator);
    }

    public <E> E post(String url, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, HttpHeader.EMPTY, requestBody, validator);
    }

    public void post(String url, HttpQueryParams queryParams, HttpRequestBody requestBody, HttpResponseValidator validator) {
        post(url, queryParams, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, HttpRequestBody requestBody, HttpResponseValidator validator) {
        post(url, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E post(String url, HttpQueryParams queryParams, HttpHeader header, Map<String, Object> requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, queryParams, header, new JsonRequestBody(requestBody), validator);
    }

    public <E> E post(String url, HttpHeader header, Map<String, Object> requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, header, new JsonRequestBody(requestBody), validator);
    }

    public void post(String url, HttpQueryParams queryParams, HttpHeader header, Map<String, Object> requestBody, HttpResponseValidator validator) {
        post(url, queryParams, header, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, HttpQueryParams queryParams, HttpHeader header, Map<String, Object> requestBody) {
        post(url, queryParams, header, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public void post(String url, HttpHeader header, Map<String, Object> requestBody, HttpResponseValidator validator) {
        post(url, header, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, HttpHeader header, Map<String, Object> requestBody) {
        post(url, header, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E post(String url, HttpQueryParams queryParams, Map<String, Object> requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, queryParams, HttpHeader.EMPTY, new JsonRequestBody(requestBody), validator);
    }

    public <E> E post(String url, Map<String, Object> requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), validator);
    }

    public void post(String url, HttpQueryParams queryParams, Map<String, Object> requestBody, HttpResponseValidator validator) {
        post(url, queryParams, HttpHeader.EMPTY, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, HttpQueryParams queryParams, Map<String, Object> requestBody) {
        post(url, queryParams, HttpHeader.EMPTY, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public void post(String url, Map<String, Object> requestBody, HttpResponseValidator validator) {
        post(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, Map<String, Object> requestBody) {
        post(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E post(String url, HttpQueryParams queryParams, HttpResponseValidatorWithReturn validator) {
        return post(url, queryParams, EmptyRequestBody.INSTANCE, validator);
    }

    public <E> E post(String url, HttpResponseValidatorWithReturn validator) {
        return post(url, EmptyRequestBody.INSTANCE, validator);
    }

    public void post(String url, HttpQueryParams queryParams, HttpResponseValidator validator) {
        post(url, queryParams, EmptyRequestBody.INSTANCE, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, HttpResponseValidator validator) {
        post(url, EmptyRequestBody.INSTANCE, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, HttpQueryParams queryParams, HttpHeader header) {
        post(url, queryParams, header, EMPTY_RESPONSE_VALIDATOR);
    }

    public void post(String url, HttpHeader header) {
        post(url, header, EMPTY_RESPONSE_VALIDATOR);
    }

    public void post(String url, HttpQueryParams queryParams) {
        post(url, queryParams, EMPTY_RESPONSE_VALIDATOR);
    }

    public void post(String url) {
        post(url, EMPTY_RESPONSE_VALIDATOR);
    }

    public void post(String url, HttpQueryParams queryParams, HttpRequestBody requestBody) {
        post(url, queryParams, requestBody, EMPTY_RESPONSE_VALIDATOR);
    }

    public void post(String url, HttpRequestBody requestBody) {
        post(url, requestBody, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E put(String url, HttpQueryParams queryParams, HttpHeader header, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("PUT", queryParams.attachToUrl(url),
                (fullUrl, fullHeader) -> putToFullUrl(fullUrl, fullHeader, requestBody),
                header,
                requestBody,
                validator);
    }

    public void put(String url, HttpQueryParams queryParams, HttpHeader header, HttpRequestBody requestBody, HttpResponseValidator validator) {
        put(url, queryParams, header, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, HttpQueryParams queryParams, HttpHeader header, HttpRequestBody requestBody) {
        put(url, queryParams, header, requestBody, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E put(String url, HttpHeader header, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return put(url, HttpQueryParams.EMPTY, header, requestBody, validator);
    }

    public void put(String url, HttpHeader header, HttpRequestBody requestBody, HttpResponseValidator validator) {
        put(url, header, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, HttpHeader header, HttpRequestBody requestBody) {
        put(url, header, requestBody, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E put(String url, HttpQueryParams queryParams, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return put(url, queryParams, HttpHeader.EMPTY, requestBody, validator);
    }

    public void put(String url, HttpQueryParams queryParams, HttpRequestBody requestBody, HttpResponseValidator validator) {
        put(url, queryParams, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, HttpQueryParams queryParams, HttpRequestBody requestBody) {
        put(url, queryParams, requestBody, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E put(String url, HttpRequestBody requestBody, HttpResponseValidatorWithReturn validator) {
        return put(url, HttpHeader.EMPTY, requestBody, validator);
    }

    public void put(String url, HttpRequestBody requestBody, HttpResponseValidator validator) {
        put(url, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, HttpRequestBody requestBody) {
        put(url, requestBody, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E put(String url, HttpQueryParams queryParams, HttpResponseValidatorWithReturn validator) {
        return put(url, queryParams, HttpHeader.EMPTY, EmptyRequestBody.INSTANCE, validator);
    }

    public void put(String url, HttpQueryParams queryParams, HttpResponseValidator validator) {
        put(url, queryParams, HttpHeader.EMPTY, EmptyRequestBody.INSTANCE, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, HttpQueryParams queryParams) {
        put(url, queryParams, HttpHeader.EMPTY, EmptyRequestBody.INSTANCE, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E put(String url, HttpResponseValidatorWithReturn validator) {
        return put(url, HttpHeader.EMPTY, EmptyRequestBody.INSTANCE, validator);
    }

    public void put(String url, HttpResponseValidator validator) {
        put(url, HttpHeader.EMPTY, EmptyRequestBody.INSTANCE, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url) {
        put(url, HttpHeader.EMPTY, EmptyRequestBody.INSTANCE, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E put(String url, HttpHeader header, Map<String, Object> requestBody, HttpResponseValidatorWithReturn validator) {
        return put(url, header, new JsonRequestBody(requestBody), validator);
    }

    public void put(String url, HttpHeader header, Map<String, Object> requestBody, HttpResponseValidator validator) {
        put(url, header, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, HttpHeader header, Map<String, Object> requestBody) {
        put(url, header, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public void put(String url, HttpQueryParams queryParams, HttpHeader header, Map<String, Object> requestBody) {
        put(url, queryParams, header, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E put(String url, Map<String, Object> requestBody, HttpResponseValidatorWithReturn validator) {
        return put(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), validator);
    }

    public void put(String url, Map<String, Object> requestBody, HttpResponseValidator validator) {
        put(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, Map<String, Object> requestBody) {
        put(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public void put(String url, HttpQueryParams queryParams, Map<String, Object> requestBody) {
        put(url, queryParams, HttpHeader.EMPTY, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E put(String url, HttpQueryParams queryParams, HttpHeader header, HttpResponseValidatorWithReturn validator) {
        return put(url, queryParams, header, EmptyRequestBody.INSTANCE, validator);
    }

    public void put(String url, HttpQueryParams queryParams, HttpHeader header, HttpResponseValidator validator) {
        put(url, queryParams, header, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, HttpQueryParams queryParams, HttpHeader header) {
        put(url, queryParams, header, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E put(String url, HttpHeader header, HttpResponseValidatorWithReturn validator) {
        return put(url, header, EmptyRequestBody.INSTANCE, validator);
    }

    public void put(String url, HttpHeader header, HttpResponseValidator validator) {
        put(url, header, EmptyRequestBody.INSTANCE, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, HttpHeader header) {
        put(url, header, EmptyRequestBody.INSTANCE, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E delete(String url, HttpQueryParams queryParams, HttpHeader header, HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("DELETE", queryParams.attachToUrl(url),
                this::deleteToFullUrl,
                header,
                null,
                validator);
    }

    public void delete(String url, HttpQueryParams queryParams, HttpHeader header, HttpResponseValidator validator) {
        delete(url, queryParams, header, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E delete(String url, HttpHeader header, HttpResponseValidatorWithReturn validator) {
        return delete(url, HttpQueryParams.EMPTY, header, validator);
    }

    public void delete(String url, HttpHeader header, HttpResponseValidator validator) {
        delete(url, HttpQueryParams.EMPTY, header, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E delete(String url, HttpQueryParams queryParams, HttpResponseValidatorWithReturn validator) {
        return delete(url, queryParams, HttpHeader.EMPTY, validator);
    }

    public void delete(String url, HttpQueryParams queryParams, HttpResponseValidator validator) {
        delete(url, queryParams, HttpHeader.EMPTY, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E delete(String url, HttpResponseValidatorWithReturn validator) {
        return delete(url, HttpQueryParams.EMPTY, HttpHeader.EMPTY, validator);
    }

    public void delete(String url, HttpResponseValidator validator) {
        delete(url, HttpQueryParams.EMPTY, HttpHeader.EMPTY, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void delete(String url, HttpQueryParams queryParams, HttpHeader header) {
        delete(url, queryParams, header, EMPTY_RESPONSE_VALIDATOR);
    }

    public void delete(String url, HttpHeader header) {
        delete(url, HttpQueryParams.EMPTY, header, EMPTY_RESPONSE_VALIDATOR);
    }

    public void delete(String url, HttpQueryParams queryParams) {
        delete(url, queryParams, HttpHeader.EMPTY, EMPTY_RESPONSE_VALIDATOR);
    }

    public void delete(String url) {
        delete(url, HttpQueryParams.EMPTY, HttpHeader.EMPTY, EMPTY_RESPONSE_VALIDATOR);
    }

    public HttpHeader header(String... properties) {
        return new HttpHeader(CollectionUtils.aMapOf((Object[]) properties));
    }

    public HttpHeader header(Map<String, String> properties) {
        return new HttpHeader(properties);
    }

    public HttpQueryParams query(String... params) {
        return new HttpQueryParams(CollectionUtils.aMapOf((Object[]) params));
    }

    public HttpQueryParams query(Map<String, ?> params) {
        return new HttpQueryParams(params);
    }

    public HttpRequestBody body(String mimeType, String content) {
        return TextRequestBody.withType(mimeType, content);
    }

    public HttpRequestBody body(String mimeType, byte[] content) {
        return BinaryRequestBody.withType(mimeType, content);
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

    public HttpResponse getToFullUrl(String fullUrl, HttpHeader requestHeader) {
        return request("GET", fullUrl, requestHeader, EmptyRequestBody.INSTANCE);
    }

    public HttpResponse deleteToFullUrl(String fullUrl, HttpHeader requestHeader) {
        return request("DELETE", fullUrl, requestHeader, EmptyRequestBody.INSTANCE);
    }

    public HttpResponse patchToFullUrl(String fullUrl, HttpHeader requestHeader, HttpRequestBody requestBody) {
        return request("PATCH", fullUrl, requestHeader, requestBody);
    }

    public HttpResponse postToFullUrl(String fullUrl, HttpHeader requestHeader, HttpRequestBody requestBody) {
        return request("POST", fullUrl, requestHeader, requestBody);
    }

    public HttpResponse putToFullUrl(String fullUrl, HttpHeader requestHeader, HttpRequestBody requestBody) {
        return request("PUT", fullUrl, requestHeader, requestBody);
    }

    @SuppressWarnings("unchecked")
    private <R> R executeAndValidateHttpCall(String requestMethod, String url, HttpCall httpCall,
                                             HttpHeader requestHeader,
                                             HttpRequestBody requestBody,
                                             HttpResponseValidatorWithReturn validator) {
        String fullUrl = HttpConfigurations.fullUrl(url);
        HttpHeader fullHeader = HttpConfigurations.fullHeader(fullUrl, url, requestHeader);

        HttpValidationResult validationResult = new HttpValidationResult(requestMethod, url, fullUrl, fullHeader, requestBody);

        TestStep step = createHttpStep(validationResult, httpCall, validator);
        try {
            return (R) step.execute(StepReportOptions.REPORT_ALL);
        } finally {
            lastValidationResult.set(validationResult);
            step.addPayload(validationResult);
        }
    }

    private <R> TestStep createHttpStep(HttpValidationResult validationResult,
                                        HttpCall httpCall,
                                        HttpResponseValidatorWithReturn validator) {
        Supplier<Object> httpCallSupplier = () -> {
            HttpResponse response = null;
            try {
                long startTime = Time.currentTimeMillis();

                BeforeFirstHttpCallListenerTrigger.trigger();
                HttpListeners.beforeHttpCall(validationResult.getRequestMethod(),
                        validationResult.getUrl(), validationResult.getFullUrl(),
                        validationResult.getRequestHeader(), validationResult.getRequestBody());

                response = httpCall.execute(validationResult.getFullUrl(),
                        validationResult.getRequestHeader());

                response = followRedirects(validationResult.getRequestMethod(),
                        httpCall, validationResult.getRequestHeader(), response);

                long endTime = Time.currentTimeMillis();

                validationResult.setStartTime(startTime);
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
                throw new HttpException("error during http." + validationResult.getRequestMethod().toLowerCase() + "(" +
                        validationResult.getFullUrl() + "): " + StackTraceUtils.fullCauseMessage(e), e);
            } finally {
                HttpListeners.afterHttpCall(validationResult.getRequestMethod(),
                        validationResult.getUrl(), validationResult.getFullUrl(),
                        validationResult.getRequestHeader(), validationResult.getRequestBody(),
                        response);
            }
        };

        return TestStep.createStep(null, tokenizedMessage(
                action("executing HTTP " + validationResult.getRequestMethod()), urlValue(validationResult.getFullUrl())),
                () -> tokenizedMessage(action("executed HTTP " + validationResult.getRequestMethod()), urlValue(validationResult.getFullUrl())),
                httpCallSupplier);
    }

    private HttpResponse followRedirects(String requestMethod, HttpCall httpCall, HttpHeader fullRequestHeader, HttpResponse response) {
        int retryCount = 0;
        while (response.isRedirect() && getCfg().shouldFollowRedirects() && retryCount++ < getCfg().maxRedirects()) {
            TestStep httpStep = createRedirectStep(requestMethod, response.locationHeader(), httpCall, fullRequestHeader);
            response = (HttpResponse) httpStep.execute(StepReportOptions.REPORT_ALL);
        }
        return response;
    }

    private TestStep createRedirectStep(String requestMethod, String fullUrl, HttpCall httpCall,
                                        HttpHeader fullRequestHeader) {
        Supplier<Object> httpCallSupplier = () -> httpCall.execute(fullUrl, fullRequestHeader);

        return TestStep.createStep(null, tokenizedMessage(action("executing HTTP redirect to " + requestMethod), urlValue(fullUrl)),
                () -> tokenizedMessage(action("executed HTTP redirect to " + requestMethod), urlValue(fullUrl)),
                httpCallSupplier);
    }

    @SuppressWarnings("unchecked")
    private <R> R validateAndRecord(HttpValidationResult validationResult,
                                    HttpResponseValidatorWithReturn validator) {

        HeaderDataNode header = new HeaderDataNode(validationResult.getResponse());
        DataNode body = createBodyDataNode(validationResult.getResponse());

        validationResult.setResponseHeaderNode(header);
        validationResult.setResponseBodyNode(body);

        ExpectationHandler recordAndThrowHandler = new ExpectationHandler() {
            @Override
            public Flow onValueMismatch(ValueMatcher valueMatcher, ActualPath actualPath, Object actualValue, String message) {
                validationResult.addMismatch(message);
                return ExpectationHandler.Flow.PassToNext;
            }
        };

        // 1. validate using user provided validation block
        // 2. validate status code
        // 3. if validation block throws exception,
        //    we still validate status code to make sure user is aware of the status code problem
        try {
            R extracted = ExpectationHandlers.withAdditionalHandler(recordAndThrowHandler, () -> {
                Object returnedValue = validator.validate(header, body);
                return (R) extractOriginalValue(returnedValue);
            });

            ExpectationHandlers.withAdditionalHandler(recordAndThrowHandler, () -> {
                validateStatusCode(validationResult);
                return null;
            });

            HttpValidationHandlers.validate(validationResult);

            return extracted;
        } catch (Throwable e) {
            ExpectationHandlers.withAdditionalHandler(new ExpectationHandler() {
                @Override
                public Flow onValueMismatch(ValueMatcher valueMatcher, ActualPath actualPath, Object actualValue, String message) {
                    validationResult.addMismatch(message);

                    // another assertion happened before status code check
                    // we discard it and throw status code instead
                    if (e instanceof AssertionError) {
                        throw new AssertionError('\n' + message);
                    }

                    // originally an exception happened,
                    // so we combine it's message with status code failure
                    throw new AssertionError('\n' + message +
                            "\n\nadditional exception message:\n" + e.getMessage(), e);

                }
            }, () -> {
                validateErrorsOnlyStatusCode(validationResult);
                return null;
            });

            throw e;
        } finally {
            renderResponse(validationResult);
        }
    }

    private DataNode createBodyDataNode(HttpResponse response) {
        try {
            DataNodeId id = new DataNodeId("body");

            if (!response.isBinary() && response.nullOrEmptyTextContent()) {
                return new StructuredDataNode(id, new TraceableValue(null));
            }

            if (response.isText()) {
                return new StructuredDataNode(id, new TraceableValue(response.getTextContent()));
            }

            if (response.isJson()) {
                Object object = JsonUtils.deserialize(response.getTextContent());
                return DataNodeBuilder.fromValue(id, object);
            }

            return new StructuredDataNode(id, new TraceableValue(response.getBinaryContent()));
        } catch (JsonParseException e) {
            throw new RuntimeException("error parsing body: " + response.getTextContent(), e);
        }
    }

    private void validateStatusCode(HttpValidationResult validationResult) {
        DataNode statusCode = validationResult.getHeaderNode().statusCode();
        if (statusCode.getTraceableValue().getCheckLevel() != CheckLevel.None) {
            return;
        }

        statusCode.should(equal(defaultExpectedStatusCodeByRequest(validationResult)));
    }

    private void validateErrorsOnlyStatusCode(HttpValidationResult validationResult) {
        DataNode statusCode = validationResult.getHeaderNode().statusCode();
        if (statusCode.getTraceableValue().getCheckLevel() != CheckLevel.None) {
            return;
        }

        Integer statusCodeValue = (Integer) statusCode.getTraceableValue().getValue();
        if (statusCodeValue >= 200 && statusCodeValue < 300) {
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
            case "PATCH":
                return validationResult.hasResponseContent() ? 200 : 204;
            default:
                return 200;
        }
    }

    private void renderResponse(HttpValidationResult result) {
        if (getCfg().getVerbosityLevel() <= TestStep.getCurrentStep().getNumberOfParents() + 1) {
            return;
        }

        if (result.getResponse().isBinary()) {
            ConsoleOutputs.out(Color.YELLOW, "[binary content]");
        } else if (!result.hasResponseContent()) {
            ConsoleOutputs.out(Color.YELLOW, "[no content]");
        } else {
            new DataNodeAnsiPrinter().print(result.getBodyNode());
        }
    }

    private HttpResponse request(String method, String fullUrl,
                                 HttpHeader requestHeader,
                                 HttpRequestBody requestBody) {
        if (requestHeader == null) {
            throw new IllegalArgumentException("Request header is null, check your header provider is not returning null");
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(fullUrl).openConnection();
            connection.setInstanceFollowRedirects(false);
            setRequestMethod(method, connection);
            connection.setRequestProperty("Content-Type", requestBody.type());
            connection.setRequestProperty("Accept", requestBody.type());
            connection.setRequestProperty("User-Agent", getCfg().getUserAgent());
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

    private void setRequestMethod(String method, HttpURLConnection connection) throws ProtocolException {
        if (method.equals("PATCH")) {
            // Http(s)UrlConnection does not recognize PATCH, unfortunately, nor will it be added, see
            // https://bugs.openjdk.java.net/browse/JDK-8207840 .
            // The Oracle-recommended solution requires JDK 11's new java.net.http package.
            try {
                Object connectionTarget = connection;
                if (connection instanceof HttpsURLConnection) {
                    final Field delegateField = HttpsURLConnectionImpl.class.getDeclaredField("delegate");
                    delegateField.setAccessible(true);
                    connectionTarget = delegateField.get(connection);
                }
                final Field f = HttpURLConnection.class.getDeclaredField("method");
                f.setAccessible(true);
                f.set(connectionTarget, "PATCH");
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException("Failed to enable PATCH on HttpUrlConnection", e);
            }
        } else {
            connection.setRequestMethod(method);
        }
    }

    private HttpResponse extractHttpResponse(HttpURLConnection connection) throws IOException {
        HttpResponse httpResponse = new HttpResponse();
        populateResponseHeader(httpResponse, connection);

        InputStream inputStream = getInputStream(connection);
        httpResponse.setStatusCode(connection.getResponseCode());
        httpResponse.setContentType(connection.getContentType() != null ? connection.getContentType() : "");

        if (!httpResponse.isBinary()) {
            httpResponse.setTextContent(inputStream != null ? IOUtils.toString(inputStream, StandardCharsets.UTF_8) : "");
        } else {
            httpResponse.setBinaryContent(inputStream != null ? IOUtils.toByteArray(inputStream) : new byte[0]);
        }

        return httpResponse;
    }

    private InputStream getInputStream(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getResponseCode() < 400 ? connection.getInputStream() : connection.getErrorStream();

        if ("gzip".equals(connection.getContentEncoding())) {
            inputStream = new GZIPInputStream(inputStream);
        }

        return inputStream;
    }

    private void populateResponseHeader(HttpResponse httpResponse, HttpURLConnection connection) {
        connection.getHeaderFields().forEach((key, values) -> {
            if (!values.isEmpty()) {
                httpResponse.addHeader(key, values.get(0));
            }
        });
    }

    /**
     * Response consist of DataNode and Traceable values but we need to return back a simple value that can be used for
     * regular calculations and to drive test flow
     *
     * @param v value returned from a validation callback
     * @return extracted regular value
     */
    private Object extractOriginalValue(Object v) {
        if (v instanceof DataNode) {
            return ((DataNode) v).get();
        }

        if (v instanceof TraceableValue) {
            return ((TraceableValue) v).getValue();
        }

        if (v instanceof List) {
            return ((List<?>) v).stream().map(this::extractOriginalValue).collect(toList());
        }

        return v;
    }

    private interface HttpCall {
        HttpResponse execute(String fullUrl, HttpHeader fullHeader);
    }

    private static class BeforeFirstHttpCallListenerTrigger {
        static {
            HttpListeners.beforeFirstHttpCall();
        }

        /**
         * no-op to force class loading
         */
        private static void trigger() {
        }
    }
}
