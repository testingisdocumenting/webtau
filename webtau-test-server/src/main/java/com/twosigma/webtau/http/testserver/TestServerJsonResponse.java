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

package com.twosigma.webtau.http.testserver;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

public class TestServerJsonResponse implements TestServerResponse {
    private final String response;
    private final int statusCode;
    private final Map<String, String> headerResponse;

    public TestServerJsonResponse(String response, int statusCode) {
        this(response, statusCode, Collections.emptyMap());
    }

    public TestServerJsonResponse(String response) {
        this(response, 200);
    }

    public TestServerJsonResponse(String response, int statusCode, Map<String, String> headerResponse) {
        this.response = response;
        this.statusCode = statusCode;
        this.headerResponse = headerResponse;
    }

    @Override
    public byte[] responseBody(HttpServletRequest request) {
        return response == null ? null : response.getBytes();
    }

    @Override
    public String responseType(HttpServletRequest request) {
        return "application/json";
    }

    @Override
    public int responseStatusCode() {
        return statusCode;
    }

    @Override
    public Map<String, String> responseHeader(HttpServletRequest request) {
        return headerResponse;
    }
}
