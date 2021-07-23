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

package org.testingisdocumenting.webtau.server.journal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebtauServerHandledRequest {
    public static final WebtauServerHandledRequest NULL = new WebtauServerHandledRequest();

    private final String method;
    private final String url;
    private final String requestType;
    private final String responseType;
    private final String capturedResponse;
    private final long startTime;
    private final long elapsedTime;

    private WebtauServerHandledRequest() {
        method = "";
        url = "";
        requestType = "";
        responseType = "";
        startTime = 0;
        elapsedTime = 0;
        capturedResponse = "[null handled request]";
    }

    public WebtauServerHandledRequest(HttpServletRequest request, HttpServletResponse response,
                                      long startTime,
                                      long endTime,
                                      String capturedResponse) {
        this.method = request.getMethod();
        this.url = request.getRequestURI();
        this.requestType = request.getContentType();
        this.responseType = response.getContentType();
        this.startTime = startTime;
        this.elapsedTime = endTime - startTime;
        this.capturedResponse = capturedResponse;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getResponseType() {
        return responseType;
    }

    public String getCapturedResponse() {
        return capturedResponse;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }
}
