/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.http.testserver;

import org.testingisdocumenting.webtau.utils.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestServerJsonLiveResponse implements TestServerResponse {
    private final List<String> responses;
    private final int statusCode;
    private final Map<String, Object> headerResponse;
    private int requestIdx;

    public TestServerJsonLiveResponse(List<String> responses, int statusCode) {
        this(responses, statusCode, Collections.emptyMap());
    }

    public TestServerJsonLiveResponse(List<String> responses) {
        this(responses, 200);
    }

    public TestServerJsonLiveResponse(List<String> responses, int statusCode, Map<String, Object> headerResponse) {
        this.responses = responses;
        this.statusCode = statusCode;
        this.headerResponse = headerResponse;
        reset();
    }

    public void reset() {
        requestIdx = 0;
    }

    @Override
    public byte[] responseBody(HttpServletRequest request) {
        String response = requestIdx < responses.size() ? responses.get(requestIdx) : null;
        byte[] result = response == null ? null : response.getBytes();
        requestIdx++;

        return result;
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
        return CollectionUtils.toStringStringMap(headerResponse);
    }
}
