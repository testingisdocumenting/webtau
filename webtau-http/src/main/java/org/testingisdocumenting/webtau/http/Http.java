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

import org.apache.commons.io.IOUtils;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.datanode.*;
import org.testingisdocumenting.webtau.data.traceable.CheckLevel;
import org.testingisdocumenting.webtau.data.traceable.TraceableValue;
import org.testingisdocumenting.webtau.expectation.AssertionTokenizedError;
import org.testingisdocumenting.webtau.expectation.ExpectationHandler;
import org.testingisdocumenting.webtau.expectation.ExpectationHandlers;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.expectation.equality.ActualPathMessage;
import org.testingisdocumenting.webtau.http.binary.BinaryRequestBody;
import org.testingisdocumenting.webtau.http.config.WebTauHttpConfigurations;
import org.testingisdocumenting.webtau.http.formdata.FormUrlEncodedRequestBody;
import org.testingisdocumenting.webtau.http.json.JsonRequestBody;
import org.testingisdocumenting.webtau.http.listener.HttpListeners;
import org.testingisdocumenting.webtau.http.multipart.MultiPartFile;
import org.testingisdocumenting.webtau.http.multipart.MultiPartFormData;
import org.testingisdocumenting.webtau.http.multipart.MultiPartFormField;
import org.testingisdocumenting.webtau.http.operationid.HttpOperationIdProviders;
import org.testingisdocumenting.webtau.http.request.*;
import org.testingisdocumenting.webtau.http.resource.HttpResource;
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
import org.testingisdocumenting.webtau.utils.UrlUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;
import java.util.zip.GZIPInputStream;

import static java.util.stream.Collectors.*;
import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.cfg.WebTauConfig.*;

public class Http {
    private static final HttpResponseValidatorWithReturn EMPTY_RESPONSE_VALIDATOR = (header, body) -> null;

    public static final Http http = new Http();

    public final HttpDocumentation doc = new HttpDocumentation();

    private final ThreadLocal<HttpValidationResult> lastValidationResult = new ThreadLocal<>();

    public final HttpApplicationMime application = new HttpApplicationMime();
    public final HttpTextMime text = new HttpTextMime();

    /**
     * WebTau has a way to define a lazy value associated with <code>HTTP GET</code> response. After that it can be used in multiple tests, <code>should</code> and <code>waitTo</code> on it.
     * <p>
     * Value can be associated with static urls like <code>/info</code> or dynamic urls like <code>/price/:ticker</code>
     * @param definitionGet resource definition with optional placeholders
     * @return resource definition
     */
    public HttpResource resource(String definitionGet) {
        return new HttpResource(definitionGet, HttpHeader.EMPTY);
    }

    /**
     * WebTau has a way to define a lazy value associated with <code>HTTP GET</code> response. After that it can be used in multiple tests, <code>should</code> and <code>waitTo</code> on it.
     * <p>
     * Value can be associated with static urls like <code>/info</code> or dynamic urls like <code>/price/:ticker</code>
     * @param definitionGet resource definition with optional placeholders
     * @param header header to use for the calls
     * @return resource definition
     */
    public HttpResource resource(String definitionGet, HttpHeader header) {
        return new HttpResource(definitionGet, header);
    }

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
            HttpClient.Builder clientBuilder = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMillis(getCfg().getHttpTimeout()))
                    .followRedirects(HttpClient.Redirect.NEVER);

            if (getCfg().isHttpProxySet()) {
                HostPort hostPort = new HostPort(getCfg().getHttpProxyConfigValue().getAsString());
                clientBuilder.proxy(ProxySelector.of(new InetSocketAddress(hostPort.host, hostPort.port)));
            }

            HttpClient httpClient = clientBuilder.build();

            HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
                    .uri(new URI(fullUrl))
                    .header("Content-Type", requestBody.type())
                    .header("Accept", requestBody.type())
                    .header("User-Agent", getCfg().getUserAgent())
                    .timeout(Duration.ofMillis(getCfg().getHttpTimeout()));
            requestHeader.forEachProperty(httpRequestBuilder::header);

            httpRequestBuilder = setRequestMethod(httpRequestBuilder, method, requestBody);

            return sendAndExtractHttpResponse(httpClient, httpRequestBuilder.build());
        } catch (IOException|InterruptedException|URISyntaxException e) {
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

    private HttpRequest.Builder setRequestMethod(HttpRequest.Builder builder, String method, HttpRequestBody requestBody) {
        switch (method) {
            case "POST":
                 return builder.POST(bodyPublisherFromRequestBody(requestBody));
            case "PUT":
                return builder.PUT(bodyPublisherFromRequestBody(requestBody));
            case "GET":
                return builder.GET();
            case "DELETE":
                return builder.DELETE();
            case "PATCH":
                return builder.method("PATCH",bodyPublisherFromRequestBody(requestBody));
            default:
                throw new IllegalArgumentException("unrecognized request method <" + method + ">");
        }
    }

    private HttpRequest.BodyPublisher bodyPublisherFromRequestBody(HttpRequestBody requestBody) {
        if (requestBody instanceof EmptyRequestBody) {
            return HttpRequest.BodyPublishers.noBody();
        }

        validateRequestContent(requestBody);

        if (requestBody.isBinary()) {
            return HttpRequest.BodyPublishers.ofByteArray(requestBody.asBytes());
        }

        return HttpRequest.BodyPublishers.ofString(requestBody.asString());
    }

    private HttpResponse sendAndExtractHttpResponse(HttpClient client, HttpRequest httpRequest) throws IOException, InterruptedException {
        HttpResponse webTauResponse = new HttpResponse();

        java.net.http.HttpResponse<InputStream> javaResponse = client.send(httpRequest, BodyHandlers.ofInputStream());

        webTauResponse.setContentType(javaResponse.headers().firstValue("content-type").orElse(""));

        InputStream inputStream = getInputStream(javaResponse);
        if (webTauResponse.isBinary()) {
            webTauResponse.setBinaryContent(inputStream != null ? IOUtils.toByteArray(inputStream) : new byte[0]);
        } else {
            webTauResponse.setTextContent(inputStream != null ? IOUtils.toString(inputStream, StandardCharsets.UTF_8) : "");
        }

        webTauResponse.setStatusCode(javaResponse.statusCode());
        populateResponseHeader(webTauResponse, javaResponse);

        return webTauResponse;
    }

    private InputStream getInputStream(java.net.http.HttpResponse<InputStream> javaResponse) throws IOException {
        String contentEncoding = javaResponse.headers().firstValue("content-encoding").orElse("");
        InputStream inputStream = javaResponse.body();

        if ("gzip".equals(contentEncoding)) {
            inputStream = new GZIPInputStream(inputStream);
        }

        return inputStream;
    }

    private void populateResponseHeader(HttpResponse webTauResponse, java.net.http.HttpResponse<InputStream> javaResponse) {
        Map<CharSequence, CharSequence> header = new LinkedHashMap<>();

        javaResponse.headers().map().forEach((key, values) -> {
            if (!values.isEmpty()) {
                header.put(key, values.get(0));
            }
        });

        webTauResponse.addHeader(header);
    }

    /**
     * Response consist of DataNode and Traceable values, but we need to return a simple value that can be used for
     * regular calculations and to drive test flow
     *
     * @param v value returned from a validation callback
     * @return extracted regular value
     */
    private Object extractOriginalValue(Object v) {
        if (v instanceof DataNodeReturnNoConversionWrapper) {
            return v;
        }

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

            host = parts[0].trim();
            port = Integer.parseInt(parts[1].trim());
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
