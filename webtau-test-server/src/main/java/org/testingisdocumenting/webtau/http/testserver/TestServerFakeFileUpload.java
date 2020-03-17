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

package com.twosigma.webtau.http.testserver;

import com.twosigma.webtau.utils.JsonUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class TestServerFakeFileUpload implements TestServerResponse {
    @Override
    public byte[] responseBody(HttpServletRequest request) throws IOException, ServletException {
        Collection<Part> parts = request.getParts();
        return JsonUtils.serialize(createResponse(parts)).getBytes();
    }

    @Override
    public String responseType(HttpServletRequest request) {
        return "application/json";
    }

    @Override
    public int responseStatusCode() {
        return 201;
    }

    private Map<String, Object> createResponse(Collection<Part> parts) {
        Optional<Part> file = findPart(parts, "file");
        Optional<Part> descriptionPart = findPart(parts, "fileDescription");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("timestamp", System.currentTimeMillis());
        result.put("fileName", file.map(Part::getSubmittedFileName).orElse("backend-generated-name-as-no-name-provided"));
        result.put("description", descriptionPart.map(this::extractContent).orElse(null));

        return result;
    }

    private Optional<Part> findPart(Collection<Part> parts, String name) {
        return parts.stream().filter(p -> p.getName().equals(name)).findFirst();
    }

    private String extractContent(Part p) {
        try {
            return IOUtils.toString(p.getInputStream(), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
