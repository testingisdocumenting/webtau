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

import com.twosigma.webtau.utils.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class TestServerMultiPartMetaEcho implements TestServerResponse {
    private final int statusCode;

    public TestServerMultiPartMetaEcho(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public byte[] responseBody(HttpServletRequest request) throws IOException, ServletException {
        Collection<Part> parts = request.getParts();
        return JsonUtils.serialize(parts.stream().map(this::partToMap).collect(toList())).getBytes();
    }

    @Override
    public String responseType(HttpServletRequest request) {
        return "application/json";
    }

    @Override
    public int responseStatusCode() {
        return statusCode;
    }

    private Map<String, Object> partToMap(Part part) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("fieldName", part.getName());
        result.put("fileName", part.getSubmittedFileName());

        return result;
    }
}
