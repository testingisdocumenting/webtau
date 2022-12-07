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

package org.testingisdocumenting.webtau.http.operationid;

import org.testingisdocumenting.webtau.WebTauCore;
import org.testingisdocumenting.webtau.utils.UrlUtils;

import java.util.*;

class HttpTextDefinedOperations {
    private static final Set<String> SUPPORTED_METHODS = new LinkedHashSet<>(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));

    private final Map<String, List<HttpTextOperation>> operationsByMethod;
    private final List<String> lines;

    HttpTextDefinedOperations(String definition) {
        this.operationsByMethod = new HashMap<>();

        this.lines = Arrays.asList(definition.split("\n"));
        parseLines();
    }

    public String findOperationId(String method, String fullUrl) {
        String relativeUrl = UrlUtils.extractPath(fullUrl);

        List<HttpTextOperation> byMethod = operationsByMethod.getOrDefault(method, Collections.emptyList());

        String result = byMethod.isEmpty() ?
                "":
                byMethod.stream()
                        .filter(op -> op.matches(relativeUrl))
                        .findFirst()
                        .map(HttpTextOperation::getOperationId)
                        .orElse("");

        if (result.isEmpty()) {
            WebTauCore.warning("route is not defined for HTTP " + method, "url", fullUrl);
        }

        return result;
    }

    private void parseLines() {
       lines.forEach(this::parseLine);
    }

    private void parseLine(String line) {
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return;
        }

        int firstSpaceIdx = trimmed.indexOf(' ');
        if (firstSpaceIdx == -1) {
            throwFormatMessage();
        }

        String method = trimmed.substring(0, firstSpaceIdx).toUpperCase();
        validateMethod(method);

        String route = trimmed.substring(firstSpaceIdx).trim();
        HttpTextOperation operation = new HttpTextOperation(method, route);

        operationsByMethod.computeIfAbsent(method, (m) -> new ArrayList<>()).add(operation);
    }

    private void validateMethod(String method) {
        if (!SUPPORTED_METHODS.contains(method)) {
            throw new IllegalArgumentException("unsupported method <" + method + ">, must be one of the following: " +
                    String.join(", ", SUPPORTED_METHODS));
        }
    }

    private void throwFormatMessage() {
        throw new IllegalArgumentException("route line must be in format METHOD /path/:with/params/:id");
    }
}
