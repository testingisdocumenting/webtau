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

package org.testingisdocumenting.webtau.http.listeners

import org.testingisdocumenting.webtau.http.HttpHeader
import org.testingisdocumenting.webtau.http.HttpResponse
import org.testingisdocumenting.webtau.http.HttpTestBase
import org.testingisdocumenting.webtau.http.listener.HttpListener
import org.testingisdocumenting.webtau.http.listener.HttpListeners
import org.testingisdocumenting.webtau.http.request.HttpRequestBody
import org.junit.After
import org.junit.Before
import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.contain
import static org.testingisdocumenting.webtau.http.Http.http

class HttpListenersTest extends HttpTestBase implements HttpListener {
    Map beforePayload
    Map afterPayload

    // Note that testing beforeFirstHttpCall is not done here due to implementation leveraging singleton approach to
    // make sure callback is only called once

    @Override
    void beforeHttpCall(String requestMethod, String passedUrl, String fullUrl, HttpHeader requestHeader, HttpRequestBody requestBody) {
        beforePayload = [
                requestMethod: requestMethod,
                passedUrl: passedUrl,
                fullUrl: fullUrl,
                requestHeader: requestHeader,
                requestBody: requestBody
        ]
    }

    @Override
    void afterHttpCall(String requestMethod, String passedUrl, String fullUrl, HttpHeader requestHeader, HttpRequestBody requestBody, HttpResponse response) {
        afterPayload = [
                requestMethod: requestMethod,
                passedUrl: passedUrl,
                fullUrl: fullUrl,
                requestHeader: requestHeader,
                requestBody: requestBody,
                response: response
        ]
    }

    @Before
    void init() {
        HttpListeners.add(this)

        beforePayload = [:]
        afterPayload = [:]
    }

    @After
    void cleanup() {
        HttpListeners.remove(this)
    }

    @Test
    void "before and after call callbacks should have essential http call info"() {
        http.put("/end-point", http.header([custom: 'value']), [payload: 'message']) {}

        validateCommonCallBackPayload(beforePayload)
        validateCommonCallBackPayload(afterPayload)

        afterPayload.response.textContent.should contain("complexList")
        afterPayload.response.statusCode.should == 200
    }

    private static void validateCommonCallBackPayload(payload) {
        payload.requestMethod.should == 'PUT'
        payload.passedUrl.should == "/end-point"
        payload.fullUrl.should == ~/^*\d\+*\/end-point$/
        payload.requestHeader.custom.should == 'value'
        payload.requestBody.type().should == 'application/json'
    }
}
