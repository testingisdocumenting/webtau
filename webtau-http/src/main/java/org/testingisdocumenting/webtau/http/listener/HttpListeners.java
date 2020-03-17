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

package com.twosigma.webtau.http.listener;

import com.twosigma.webtau.http.HttpHeader;
import com.twosigma.webtau.http.HttpResponse;
import com.twosigma.webtau.http.request.HttpRequestBody;
import com.twosigma.webtau.utils.ServiceLoaderUtils;

import java.util.List;

public class HttpListeners {
    private static final List<HttpListener> listeners = ServiceLoaderUtils.load(HttpListener.class);

    private HttpListeners() {
    }

    public static void beforeFirstHttpCall() {
        listeners.forEach(HttpListener::beforeFirstHttpCall);
    }

    public static void beforeHttpCall(String requestMethod,
                                      String passedUrl,
                                      String fullUrl,
                                      HttpHeader requestHeader,
                                      HttpRequestBody requestBody) {
        listeners.forEach(listener -> listener.beforeHttpCall(requestMethod, passedUrl, fullUrl, requestHeader, requestBody));
    }

    public static void afterHttpCall(String requestMethod,
                                     String passedUrl,
                                     String fullUrl,
                                     HttpHeader requestHeader,
                                     HttpRequestBody requestBody,
                                     HttpResponse response) {
        listeners.forEach(listener -> listener.afterHttpCall(
                requestMethod, passedUrl, fullUrl, requestHeader, requestBody, response));
    }

    public static void add(HttpListener listener) {
        listeners.add(listener);
    }

    public static void remove(HttpListener listener) {
        listeners.remove(listener);
    }
}
