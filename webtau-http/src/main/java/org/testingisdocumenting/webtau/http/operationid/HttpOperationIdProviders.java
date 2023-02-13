/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.http.operationid;

import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.WebTauStep;
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static org.testingisdocumenting.webtau.WebTauCore.*;

/**
 * @see HttpOperationIdProvider
 */
public class HttpOperationIdProviders {
    private static final List<HttpOperationIdProvider> globalProviders =
            ServiceLoaderUtils.load(HttpOperationIdProvider.class);

    private static final ThreadLocal<Boolean> enabled = ThreadLocal.withInitial(() -> true);

    private HttpOperationIdProviders() {
    }

    public static String operationId(String requestMethod,
                                     String passedUrl,
                                     String fullUrl,
                                     HttpHeader requestHeader,
                                     HttpRequestBody requestBody) {
        if (!enabled.get()) {
            return "";
        }

        if (globalProviders.isEmpty()) {
            return "";
        }

        if (globalProviders.stream().noneMatch(HttpOperationIdProvider::isEnabled)) {
            return "";
        }

        WebTauStep step = WebTauStep.createStep(
                tokenizedMessage().action("mapping").classifier("operation id"),
                (id) -> tokenizedMessage().action("mapped").classifier("operation id").as().string("\"" + id + "\""),
                () -> extractOperationId(requestMethod, passedUrl, fullUrl, requestHeader, requestBody));

        return step.execute(StepReportOptions.REPORT_ALL);
    }

    public static <R> R withDisabledProviders(Supplier<R> code) {
        enabled.set(false);
        try {
            return code.get();
        } finally {
            enabled.set(true);
        }
    }

    private static String extractOperationId(String requestMethod,
                                             String passedUrl,
                                             String fullUrl,
                                             HttpHeader requestHeader,
                                             HttpRequestBody requestBody) {
        return globalProviders.stream()
                .filter(HttpOperationIdProvider::isEnabled)
                .map((provider) -> provider.operationId(requestMethod, passedUrl, fullUrl, requestHeader, requestBody))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("");
    }
}
