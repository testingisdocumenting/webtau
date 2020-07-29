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

package org.testingisdocumenting.webtau.http.testserver;

import org.testingisdocumenting.webtau.utils.JsonUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.testingisdocumenting.webtau.http.testserver.ResponseUtils.echoHeaders;

public class TestServerRequestFullEcho implements TestServerResponse {
    private final int statusCode;

    public TestServerRequestFullEcho(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public byte[] responseBody(HttpServletRequest request) {
        try {
            String json = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
            Object parsedRequest = json.equals("") ? Collections.emptyMap() :
                    json.startsWith("[") ?
                            JsonUtils.deserializeAsList(json) :
                            JsonUtils.deserializeAsMap(json);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("request", parsedRequest);
            response.put("urlPath", request.getRequestURI());
            response.put("urlQuery", request.getQueryString());

            response.putAll(echoHeaders(request));

            return IOUtils.toByteArray(new StringReader(JsonUtils.serializePrettyPrint(response)),
                    StandardCharsets.UTF_8);
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
