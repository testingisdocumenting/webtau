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
import static org.testingisdocumenting.webtau.WebTauCore.tokenizedMessage;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

import org.testingisdocumenting.webtau.data.traceable.CheckLevel;
import org.testingisdocumenting.webtau.data.traceable.TraceableValue;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.AssertionTokenizedError;
import org.testingisdocumenting.webtau.expectation.ExpectationHandler;
import org.testingisdocumenting.webtau.expectation.ExpectationHandlers;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.expectation.equality.ActualPathMessage;
import org.testingisdocumenting.webtau.http.binary.BinaryRequestBody;
import org.testingisdocumenting.webtau.http.config.WebTauHttpConfigurations;
import org.testingisdocumenting.webtau.http.datanode.DataNode;
import org.testingisdocumenting.webtau.http.datanode.DataNodeBuilder;
import org.testingisdocumenting.webtau.http.datanode.DataNodeId;
import org.testingisdocumenting.webtau.http.datanode.StructuredDataNode;
import org.testingisdocumenting.webtau.http.formdata.FormUrlEncodedRequestBody;
import org.testingisdocumenting.webtau.http.json.JsonRequestBody;
import org.testingisdocumenting.webtau.http.listener.HttpListeners;
import org.testingisdocumenting.webtau.http.multipart.MultiPartFile;
import org.testingisdocumenting.webtau.http.multipart.MultiPartFormData;
import org.testingisdocumenting.webtau.http.multipart.MultiPartFormField;
import org.testingisdocumenting.webtau.http.operationid.HttpOperationIdProviders;
import org.testingisdocumenting.webtau.http.request.EmptyRequestBody;
import org.testingisdocumenting.webtau.http.request.HttpApplicationMime;
import org.testingisdocumenting.webtau.http.request.HttpQueryParams;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.http.request.HttpTextMime;
import org.testingisdocumenting.webtau.http.text.TextRequestBody;
import org.testingisdocumenting.webtau.http.validation.*;
import org.testingisdocumenting.webtau.persona.Persona;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.reporter.stacktrace.StackTraceUtils;
import org.testingisdocumenting.webtau.time.Time;
import org.testingisdocumenting.webtau.utils.CollectionUtils;
import org.testingisdocumenting.webtau.utils.JsonParseException;
import org.testingisdocumenting.webtau.utils.JsonUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.io.IOUtils;
import org.testingisdocumenting.webtau.utils.UrlUtils;
import sun.net.www.protocol.https.HttpsURLConnectionImpl;

public class Http {
    private static final HttpResponseValidatorWithReturn EMPTY_RESPONSE_VALIDATOR = (header, body) -> null;

    public static final Http http = new Http();

    public final HttpDocumentation doc = new HttpDocumentation();

    private final ThreadLocal<HttpValidationResult> lastValidationResult = new ThreadLocal<>();

    public final HttpApplicationMime application = new HttpApplicationMime();
    public final HttpTextMime text = new HttpTextMime();

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
        String fullUrl = WebTauHttpConfigurations.fullUrl(queryParams.attachToUrl(url));
        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("pinging").url(fullUrl),
                () -> tokenizedMessage().action("pinged").url(fullUrl),
                () -> HttpValidationHandlers.withDisabledHandlers(() -> {
                    HttpOperationIdProviders.withDisabledProviders(() -> {
                        http.get(url, header);
                        return null;
                    });

                    return null;
                })
        );

        try {
            step.execute(StepReportOptions.REPORT_ALL);
        } catch (Throwable e) {
            return false;
        }

        return true;
    }

    public String concatUrl(String baseUrl, String relativeUrl) {
        return UrlUtils.concat(baseUrl, relativeUrl);
    }

    public HttpRequestBody json(String firstKey, Object firstValue, Object... rest) {
        return application.json(firstKey, firstValue, rest);
    }

    public HttpRequestBody json(String json) {
        return application.json(json);
    }

    public <E> E get(String url,
                     HttpQueryParams queryParams,
                     HttpHeader header,
                     HttpResponseValidatorWithReturn validator) {
        return executeAndValidateHttpCall("GET", queryParams.attachToUrl(url),
                this::getToFullUrl,
                header, null, validator);
    }

    public <E> E get(String url, Map<CharSequence, ?> queryParams, HttpHeader header, HttpResponseValidatorWithReturn validator) {
        return get(url, new HttpQueryParams(queryParams), header, validator);
    }

    public void get(String url, HttpQueryParams queryParams, HttpHeader header, HttpResponseValidator validator) {
        get(url, queryParams, header, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void get(String url, Map<CharSequence, ?> queryParams, HttpHeader header, HttpResponseValidator validator) {
        get(url, queryParams, header, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void get(String url, HttpQueryParams queryParams, HttpHeader header) {
        get(url, queryParams, header, EMPTY_RESPONSE_VALIDATOR);
    }

    public void get(String url, Map<CharSequence, ?> queryParams, HttpHeader header) {
        get(url, queryParams, header, EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E get(String url, Map<CharSequence, ?> queryParams, HttpResponseValidatorWithReturn validator) {
        return get(url, queryParams, HttpHeader.EMPTY, validator);
    }

    public <E> E get(String url, HttpQueryParams queryParams, HttpResponseValidatorWithReturn validator) {
        return get(url, queryParams, HttpHeader.EMPTY, validator);
    }

    public void get(String url, Map<CharSequence, ?> queryParams, HttpResponseValidator validator) {
        get(url, queryParams, HttpHeader.EMPTY, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void get(String url, HttpQueryParams queryParams, HttpResponseValidator validator) {
        get(url, queryParams, HttpHeader.EMPTY, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void get(String url, Map<CharSequence, ?> queryParams) {
        get(url, queryParams, HttpHeader.EMPTY, EMPTY_RESPONSE_VALIDATOR);
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

    public <E> E patch(String url, HttpQueryParams queryParams, HttpHeader header, Map<String, ?> requestBody,
        HttpResponseValidatorWithReturn validator) {
        return patch(url, queryParams, header, new JsonRequestBody(requestBody), validator);
    }

    public <E> E patch(String url, HttpQueryParams queryParams, HttpHeader header, Collection<?> requestBody,
        HttpResponseValidatorWithReturn validator) {
        return patch(url, queryParams, header, new JsonRequestBody(requestBody), validator);
    }

    public <E> E patch(String url, HttpHeader header, Map<String, ?> requestBody, HttpResponseValidatorWithReturn validator) {
        return patch(url, header, new JsonRequestBody(requestBody), validator);
    }

    public <E> E patch(String url, HttpHeader header, Collection<?> requestBody, HttpResponseValidatorWithReturn validator) {
        return patch(url, header, new JsonRequestBody(requestBody), validator);
    }

    public void patch(String url, HttpQueryParams queryParams, HttpHeader header, Map<String, ?> requestBody,
        HttpResponseValidator validator) {
        patch(url, queryParams, header, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void patch(String url, HttpQueryParams queryParams, HttpHeader header, Collection<?> requestBody,
        HttpResponseValidator validator) {
        patch(url, queryParams, header, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void patch(String url, HttpHeader header, Map<String, ?> requestBody, HttpResponseValidator validator) {
        patch(url, header, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void patch(String url, HttpHeader header, Collection<?> requestBody, HttpResponseValidator validator) {
        patch(url, header, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public <E> E patch(String url, HttpQueryParams queryParams, Map<String, ?> requestBody, HttpResponseValidatorWithReturn validator) {
        return patch(url, queryParams, HttpHeader.EMPTY, new JsonRequestBody(requestBody), validator);
    }

    public <E> E patch(String url, HttpQueryParams queryParams, Collection<?> requestBody, HttpResponseValidatorWithReturn validator) {
        return patch(url, queryParams, HttpHeader.EMPTY, new JsonRequestBody(requestBody), validator);
    }

    public <E> E patch(String url, Map<String, ?> requestBody, HttpResponseValidatorWithReturn validator) {
        return patch(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), validator);
    }

    public <E> E patch(String url, Collection<?> requestBody, HttpResponseValidatorWithReturn validator) {
        return patch(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), validator);
    }

    public void patch(String url, HttpQueryParams queryParams, Map<String, ?> requestBody, HttpResponseValidator validator) {
        patch(url, queryParams, HttpHeader.EMPTY, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void patch(String url, HttpQueryParams queryParams, Collection<?> requestBody, HttpResponseValidator validator) {
        patch(url, queryParams, HttpHeader.EMPTY, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void patch(String url, Map<String, ?> requestBody, HttpResponseValidator validator) {
        patch(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void patch(String url, Collection<?> requestBody, HttpResponseValidator validator) {
        patch(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void patch(String url, Map<String, ?> requestBody) {
        patch(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public void patch(String url, Collection<?> requestBody) {
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

    public <E> E post(String url, HttpQueryParams queryParams, HttpHeader header, Map<String, ?> requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, queryParams, header, new JsonRequestBody(requestBody), validator);
    }

    public <E> E post(String url, HttpQueryParams queryParams, HttpHeader header, Collection<?> requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, queryParams, header, new JsonRequestBody(requestBody), validator);
    }

    public <E> E post(String url, HttpHeader header, Map<String, ?> requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, header, new JsonRequestBody(requestBody), validator);
    }

    public <E> E post(String url, HttpHeader header, Collection<?> requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, header, new JsonRequestBody(requestBody), validator);
    }

    public void post(String url, HttpQueryParams queryParams, HttpHeader header, Map<String, ?> requestBody, HttpResponseValidator validator) {
        post(url, queryParams, header, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, HttpQueryParams queryParams, HttpHeader header, Collection<?> requestBody, HttpResponseValidator validator) {
        post(url, queryParams, header, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, HttpQueryParams queryParams, HttpHeader header, Map<String, ?> requestBody) {
        post(url, queryParams, header, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public void post(String url, HttpQueryParams queryParams, HttpHeader header, Collection<?> requestBody) {
        post(url, queryParams, header, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public void post(String url, HttpHeader header, Map<String, ?> requestBody, HttpResponseValidator validator) {
        post(url, header, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, HttpHeader header, Collection<?> requestBody, HttpResponseValidator validator) {
        post(url, header, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, HttpHeader header, Map<String, ?> requestBody) {
        post(url, header, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public void post(String url, HttpHeader header, Collection<?> requestBody) {
        post(url, header, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E post(String url, HttpQueryParams queryParams, Map<String, ?> requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, queryParams, HttpHeader.EMPTY, new JsonRequestBody(requestBody), validator);
    }

    public <E> E post(String url, HttpQueryParams queryParams, Collection<?> requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, queryParams, HttpHeader.EMPTY, new JsonRequestBody(requestBody), validator);
    }

    public <E> E post(String url, Map<String, ?> requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), validator);
    }

    public <E> E post(String url, Collection<?> requestBody, HttpResponseValidatorWithReturn validator) {
        return post(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), validator);
    }

    public void post(String url, HttpQueryParams queryParams, Map<String, ?> requestBody, HttpResponseValidator validator) {
        post(url, queryParams, HttpHeader.EMPTY, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, HttpQueryParams queryParams, Collection<?> requestBody, HttpResponseValidator validator) {
        post(url, queryParams, HttpHeader.EMPTY, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, HttpQueryParams queryParams, Map<String, ?> requestBody) {
        post(url, queryParams, HttpHeader.EMPTY, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public void post(String url, HttpQueryParams queryParams, Collection<?> requestBody) {
        post(url, queryParams, HttpHeader.EMPTY, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public void post(String url, Map<String, ?> requestBody, HttpResponseValidator validator) {
        post(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, Collection<?> requestBody, HttpResponseValidator validator) {
        post(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void post(String url, Map<String, ?> requestBody) {
        post(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public void post(String url, Collection<?> requestBody) {
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

    public void put(String url, HttpQueryParams queryParams, HttpHeader header, Map<String, ?> requestBody, HttpResponseValidator validator) {
        put(url, queryParams, header, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, HttpQueryParams queryParams, HttpHeader header, Collection<?> requestBody, HttpResponseValidator validator) {
        put(url, queryParams, header, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
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

    public <E> E put(String url, HttpQueryParams queryParams, Map<String, ?> requestBody, HttpResponseValidatorWithReturn validator) {
        return put(url, queryParams, HttpHeader.EMPTY, requestBody, validator);
    }

    public <E> E put(String url, HttpQueryParams queryParams, Collection<?> requestBody, HttpResponseValidatorWithReturn validator) {
        return put(url, queryParams, HttpHeader.EMPTY, requestBody, validator);
    }

    public void put(String url, HttpQueryParams queryParams, HttpRequestBody requestBody, HttpResponseValidator validator) {
        put(url, queryParams, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, HttpQueryParams queryParams, Map<String, ?> requestBody, HttpResponseValidator validator) {
        put(url, queryParams, requestBody, new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, HttpQueryParams queryParams, Collection<?> requestBody, HttpResponseValidator validator) {
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

    public <E> E put(String url, HttpQueryParams queryParams, HttpHeader header, Map<String, ?> requestBody, HttpResponseValidatorWithReturn validator) {
        return put(url, queryParams, header, new JsonRequestBody(requestBody), validator);
    }

    public <E> E put(String url, HttpQueryParams queryParams, HttpHeader header, Collection<?> requestBody, HttpResponseValidatorWithReturn validator) {
        return put(url, queryParams, header, new JsonRequestBody(requestBody), validator);
    }

    public <E> E put(String url, HttpHeader header, Map<String, ?> requestBody, HttpResponseValidatorWithReturn validator) {
        return put(url, header, new JsonRequestBody(requestBody), validator);
    }

    public <E> E put(String url, HttpHeader header, Collection<?> requestBody, HttpResponseValidatorWithReturn validator) {
        return put(url, header, new JsonRequestBody(requestBody), validator);
    }

    public void put(String url, HttpHeader header, Map<String, ?> requestBody, HttpResponseValidator validator) {
        put(url, header, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, HttpHeader header, Collection<?> requestBody, HttpResponseValidator validator) {
        put(url, header, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, HttpHeader header, Map<String, ?> requestBody) {
        put(url, header, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public void put(String url, HttpHeader header, Collection<?> requestBody) {
        put(url, header, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public void put(String url, HttpQueryParams queryParams, HttpHeader header, Map<String, ?> requestBody) {
        put(url, queryParams, header, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public void put(String url, HttpQueryParams queryParams, HttpHeader header, Collection<?> requestBody) {
        put(url, queryParams, header, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public <E> E put(String url, Map<String, ?> requestBody, HttpResponseValidatorWithReturn validator) {
        return put(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), validator);
    }

    public <E> E put(String url, Collection<?> requestBody, HttpResponseValidatorWithReturn validator) {
        return put(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), validator);
    }

    public void put(String url, Map<String, ?> requestBody, HttpResponseValidator validator) {
        put(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, Collection<?> requestBody, HttpResponseValidator validator) {
        put(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), new HttpResponseValidatorIgnoringReturn(validator));
    }

    public void put(String url, Map<String, ?> requestBody) {
        put(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public void put(String url, Collection<?> requestBody) {
        put(url, HttpHeader.EMPTY, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public void put(String url, HttpQueryParams queryParams, Map<String, ?> requestBody) {
        put(url, queryParams, HttpHeader.EMPTY, new JsonRequestBody(requestBody), EMPTY_RESPONSE_VALIDATOR);
    }

    public void put(String url, HttpQueryParams queryParams, Collection<?> requestBody) {
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

    public HttpHeader header(CharSequence firstKey, CharSequence firstValue, CharSequence... restKv) {
        return new HttpHeader().with(firstKey, firstValue, restKv);
    }

    public HttpHeader header(Map<CharSequence, CharSequence> properties) {
        return new HttpHeader().with(properties);
    }

    public HttpQueryParams query(Map<?, ?> params) {
        return new HttpQueryParams(params);
    }

    public HttpQueryParams query(CharSequence firstKey, Object firstValue, Object... restKv) {
        return new HttpQueryParams(CollectionUtils.map(firstKey, firstValue, restKv));
    }

    public HttpRequestBody body(String mimeType, String content) {
        return TextRequestBody.withType(mimeType, content);
    }

    public HttpRequestBody body(String mimeType, byte[] content) {
        return BinaryRequestBody.withType(mimeType, content);
    }

    public HttpRequestBody formDataUrlEncoded(Map<CharSequence, CharSequence> data) {
        return new FormUrlEncodedRequestBody(data);
    }

    public HttpRequestBody formDataUrlEncoded(CharSequence firstKey, CharSequence firstValue, Object... restKv) {
        return new FormUrlEncodedRequestBody(CollectionUtils.map(firstKey, firstValue, restKv));
    }

    public MultiPartFormData formData(MultiPartFormField... fields) {
        MultiPartFormData formData = new MultiPartFormData();
        Arrays.stream(fields).forEach(formData::addField);

        return formData;
    }

    public MultiPartFormData formData(Map<String, ?> fields) {
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

    private <R> R executeAndValidateHttpCall(String requestMethod,
                                             String url,
                                             HttpCall httpCall,
                                             HttpHeader requestHeader,
                                             HttpRequestBody requestBody,
                                             HttpResponseValidatorWithReturn validator) {
        String fullUrl = WebTauHttpConfigurations.fullUrl(url);
        HttpHeader fullHeader = WebTauHttpConfigurations.fullHeader(fullUrl, url, requestHeader);

        HttpValidationResult validationResult = new HttpValidationResult(Persona.getCurrentPersona().getId(),
                requestMethod, url, fullUrl, fullHeader, requestBody);

        WebTauStep step = createHttpStep(validationResult, httpCall, validator);
        step.setInput(new HttpStepInput(validationResult));
        step.setStepOutputFunc((stepResult) -> validationResult);

        try {
            return step.execute(StepReportOptions.REPORT_ALL);
        } finally {
            lastValidationResult.set(validationResult);
        }
    }

    private <R> WebTauStep createHttpStep(HttpValidationResult validationResult,
                                          HttpCall httpCall,
                                          HttpResponseValidatorWithReturn validator) {
        Supplier<Object> httpCallSupplier = () -> {
            HttpResponse response = null;
            try {
                BeforeFirstHttpCallListenerTrigger.trigger();
                HttpListeners.beforeHttpCall(validationResult.getRequestMethod(),
                        validationResult.getUrl(), validationResult.getFullUrl(),
                        validationResult.getRequestHeader(), validationResult.getRequestBody());

                long startTime = Time.currentTimeMillis();
                validationResult.setStartTime(startTime);

                response = httpCall.execute(validationResult.getFullUrl(),
                        validationResult.getRequestHeader());

                response = followRedirects(validationResult.getRequestMethod(),
                        httpCall, validationResult.getRequestHeader(), response);

                validationResult.calcElapsedTimeIfNotCalculated();
                validationResult.setResponse(response);
                
                validationResult.setOperationId(HttpOperationIdProviders.operationId(
                        validationResult.getRequestMethod(),
                        validationResult.getUrl(),
                        validationResult.getFullUrl(),
                        validationResult.getRequestHeader(),
                        validationResult.getRequestBody()));

                R validationBlockReturnedValue = validateAndRecord(validationResult, validator);

                if (validationResult.hasMismatches()) {
                    throw new AssertionError("check validation errors above");
                }

                return validationBlockReturnedValue;
            } catch (AssertionError e) {
                throw e;
            } catch (Throwable e) {
                validationResult.setErrorMessage(StackTraceUtils.fullCauseMessage(e));
                throw new HttpException("error during http." + validationResult.getRequestMethod().toLowerCase() + "(" +
                        validationResult.getFullUrl() + "): " + StackTraceUtils.fullCauseMessage(e), e);
            } finally {
                validationResult.calcElapsedTimeIfNotCalculated();

                HttpListeners.afterHttpCall(validationResult.getRequestMethod(),
                        validationResult.getUrl(), validationResult.getFullUrl(),
                        validationResult.getRequestHeader(), validationResult.getRequestBody(),
                        response);
            }
        };

        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("executing HTTP").classifier(validationResult.getRequestMethod()).url(validationResult.getFullUrl()),
                () -> tokenizedMessage().action("executed HTTP").classifier(validationResult.getRequestMethod()).url(validationResult.getFullUrl()),
                httpCallSupplier);
        step.setMatcherOutputActualValueDisabled(true);

        return step;
    }

    private HttpResponse followRedirects(String requestMethod, HttpCall httpCall, HttpHeader fullRequestHeader, HttpResponse response) {
        int retryCount = 0;
        while (response.isRedirect() && getCfg().shouldFollowRedirects() && retryCount++ < getCfg().maxRedirects()) {
            WebTauStep httpStep = createRedirectStep(requestMethod, response.locationHeader(), httpCall, fullRequestHeader);
            response = httpStep.execute(StepReportOptions.REPORT_ALL);
        }
        return response;
    }

    private WebTauStep createRedirectStep(String requestMethod, String fullUrl, HttpCall httpCall,
                                          HttpHeader fullRequestHeader) {
        Supplier<Object> httpCallSupplier = () -> httpCall.execute(fullUrl, fullRequestHeader);

        return WebTauStep.createStep(tokenizedMessage().action("executing HTTP redirect").to().classifier(requestMethod).url(fullUrl),
                () -> tokenizedMessage().action("executed HTTP redirect").to().classifier(requestMethod).url(fullUrl),
                httpCallSupplier);
    }

    @SuppressWarnings("unchecked")
    private <R> R validateAndRecord(HttpValidationResult validationResult,
                                    HttpResponseValidatorWithReturn validator) {

        HeaderDataNode header = new HeaderDataNode(validationResult.getResponse());
        BodyDataNode body = new BodyDataNode(validationResult.getResponse(),
                createBodyDataNodeAndMarkResponseInvalidWhenParsingError(validationResult));

        validationResult.setResponseHeaderNode(header);
        validationResult.setResponseBodyNode(body);

        ExpectationHandler recordAndThrowHandler = new ExpectationHandler() {
            @Override
            public Flow onValueMismatch(ValueMatcher valueMatcher, ValuePath actualPath, Object actualValue, TokenizedMessage message) {
                validationResult.addMismatch(new ActualPathMessage(actualPath, message).getFullMessage());
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
                public Flow onValueMismatch(ValueMatcher valueMatcher, ValuePath actualPath, Object actualValue, TokenizedMessage message) {
                    validationResult.addMismatch(message);

                    // another assertion happened before status code check
                    // we discard it and throw status code instead
                    if (e instanceof AssertionError) {
                        throw new AssertionTokenizedError(message);
                    }

                    // originally an exception happened,
                    // so we combine its message with status code failure
                    throw new AssertionTokenizedError(tokenizedMessage().add(message).doubleNewLine()
                            .error("additional exception message: " + e.getMessage()));

                }
            }, () -> {
                validateErrorsOnlyStatusCode(validationResult);
                return null;
            });

            throw e;
        }
    }

    private DataNode createBodyDataNodeAndMarkResponseInvalidWhenParsingError(HttpValidationResult validationResult) {
        DataNodeId id = new DataNodeId("body");
        HttpResponse response = validationResult.getResponse();

        if (!response.isBinary() && response.nullOrEmptyTextContent()) {
            return new StructuredDataNode(id, new TraceableValue(null));
        }

        if (response.isText()) {
            return new StructuredDataNode(id, new TraceableValue(response.getTextContent()));
        }

        if (response.isJson()) {
            return tryParseJsonAndReturnTextIfFails(validationResult, id, response.getTextContent());
        }

        return new StructuredDataNode(id, new TraceableValue(response.getBinaryContent()));
    }

    private DataNode tryParseJsonAndReturnTextIfFails(HttpValidationResult validationResult,
                                                      DataNodeId id,
                                                      String textContent) {
        try {
            Object object = JsonUtils.deserialize(textContent);
            return DataNodeBuilder.fromValue(id, object);
        } catch (JsonParseException e) {
            validationResult.setBodyParseErrorMessage(e.getMessage());
            validationResult.addMismatch(tokenizedMessage().error("can't parse JSON response").of().url(validationResult.getFullUrl()).colon().error(e.getMessage()));

            return new StructuredDataNode(id,
                    new TraceableValue("invalid JSON:\n" + textContent));
        }
    }

    private void validateStatusCode(HttpValidationResult validationResult) {
        DataNode statusCode = validationResult.getHeaderNode().statusCode;
        if (statusCode.getTraceableValue().getCheckLevel() != CheckLevel.None) {
            return;
        }

        statusCode.should(equal(defaultExpectedStatusCodeByRequest(validationResult)));
    }

    private void validateErrorsOnlyStatusCode(HttpValidationResult validationResult) {
        DataNode statusCode = validationResult.getHeaderNode().statusCode;
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
            case "POST":
                return 201;
            case "PUT":
            case "DELETE":
            case "PATCH":
                return validationResult.hasResponseContent() ? 200 : 204;
            case "GET":
            default:
                return 200;
        }
    }

    private HttpResponse request(String method, String fullUrl,
                                 HttpHeader requestHeader,
                                 HttpRequestBody requestBody) {
        if (requestHeader == null) {
            throw new IllegalArgumentException("Request header is null, check your header provider is not returning null");
        }

        try {
            HttpURLConnection connection = createConnection(fullUrl);
            connection.setInstanceFollowRedirects(false);
            setRequestMethod(method, connection);
            connection.setConnectTimeout(getCfg().getHttpTimeout());
            connection.setReadTimeout(getCfg().getHttpTimeout());
            connection.setRequestProperty("Content-Type", requestBody.type());
            connection.setRequestProperty("Accept", requestBody.type());
            connection.setRequestProperty("User-Agent", getCfg().getUserAgent());
            requestHeader.forEachProperty(connection::setRequestProperty);

            if (! (requestBody instanceof EmptyRequestBody)) {
                validateRequestContent(requestBody);
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

    private void validateRequestContent(HttpRequestBody requestBody) {
        if (requestBody.type().contains("/json")) {
            validateJsonRequestContent(requestBody.asString());
        }
    }

    private void validateJsonRequestContent(String json) {
        JsonUtils.deserialize(json);
    }

    private HttpURLConnection createConnection(String fullUrl) {
        try {
            if (getCfg().isHttpProxySet()) {
                HostPort hostPort = new HostPort(getCfg().getHttpProxyConfigValue().getAsString());

                return (HttpURLConnection) new URL(fullUrl).openConnection(new Proxy(Proxy.Type.HTTP,
                        new InetSocketAddress(hostPort.host, hostPort.port)));
            }

            return (HttpURLConnection) new URL(fullUrl).openConnection();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
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
        Map<CharSequence, CharSequence> header = new LinkedHashMap<>();

        connection.getHeaderFields().forEach((key, values) -> {
            if (!values.isEmpty()) {
                header.put(key, values.get(0));
            }
        });

        httpResponse.addHeader(header);
    }

    /**
     * Response consist of DataNode and Traceable values, but we need to return a simple value that can be used for
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

    private static class HostPort {
        private final String host;
        private final int port;

        private HostPort(String hostPort) {
            String[] parts = hostPort.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("expect host:port format for proxy, given: " + hostPort);
            }

            host = parts[0];
            port = Integer.parseInt(parts[1]);
        }
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
