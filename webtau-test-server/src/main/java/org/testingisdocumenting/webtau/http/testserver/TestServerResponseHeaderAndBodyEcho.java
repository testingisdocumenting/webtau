/*
 * Copyright 2022 webtau maintainers
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

import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

import static org.testingisdocumenting.webtau.http.testserver.ResponseUtils.echoHeaders;

public class TestServerResponseHeaderAndBodyEcho implements TestServerResponse {
    private final int statusCode;

    public TestServerResponseHeaderAndBodyEcho(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public byte[] responseBody(HttpServletRequest request) {
        try {
            return IOUtils.toByteArray(request.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> responseHeader(HttpServletRequest request) {
        return echoHeaders(request);
    }

    @Override
    public String responseType(HttpServletRequest request) {
        return "application/json";
    }

    @Override
    public int responseStatusCode() {
        return statusCode;
    }
}
