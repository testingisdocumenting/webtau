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

package org.testingisdocumenting.webtau.http.listener;

import org.testingisdocumenting.webtau.http.HttpHeader;
import org.testingisdocumenting.webtau.http.HttpResponse;
import org.testingisdocumenting.webtau.http.request.HttpRequestBody;

public interface HttpListener {
    /**
     * called once right before first <code>http.post|get|put|patch|delete</code> call
     */
    default void beforeFirstHttpCall() {
    }

    /**
     * called before each http call
     * @param requestMethod http method (e.g. POST)
     * @param passedUrl url used in a test (e.g. /customers)
     * @param fullUrl fully resolved url (e.g. https://my-server:3010/customers)
     * @param requestHeader request header
     * @param requestBody request body
     */
    default void beforeHttpCall(String requestMethod,
                                String passedUrl,
                                String fullUrl,
                                HttpHeader requestHeader,
                                HttpRequestBody requestBody) {
    }

    /**
     * called after each http call
     * @param requestMethod http method (e.g. POST)
     * @param passedUrl url used in a test (e.g. /customers)
     * @param fullUrl fully resolved url (e.g. https://my-server:3010/customers)
     * @param requestHeader request header
     * @param requestBody request body
     * @param response response
     */
    default void afterHttpCall(String requestMethod,
                               String passedUrl,
                               String fullUrl,
                               HttpHeader requestHeader,
                               HttpRequestBody requestBody,
                               HttpResponse response) {
    }
}
