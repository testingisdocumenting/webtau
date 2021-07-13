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

package org.testingisdocumenting.webtau.server;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

public class WebtauServerOverrideList implements WebtauServerOverride {
    private final List<WebtauServerOverride> overrides = new ArrayList<>();
    private final String listId;

    public WebtauServerOverrideList(String listId) {
        this.listId = listId;
    }

    public void addOverride(WebtauServerOverride override) {
        overrides.add(override);
    }

    @Override
    public boolean matchesUri(String method, String uri) {
        return findOverride(method, uri).isPresent();
    }

    @Override
    public String overrideId() {
        return listId;
    }

    @Override
    public String toString() {
        return "id:" + overrideId() + "; list:\n" + overrides.stream().map(WebtauServerOverride::overrideId).collect(Collectors.joining("\n"));
    }

    @Override
    public byte[] responseBody(HttpServletRequest request) {
        Optional<WebtauServerOverride> found = findOverride(request);
        return found.map(override -> override.responseBody(request))
                .orElseThrow(this::shouldNotHappen);
    }

    @Override
    public String responseType(HttpServletRequest request) {
        Optional<WebtauServerOverride> found = findOverride(request);
        return found.map(override -> override.responseType(request))
                .orElseThrow(this::shouldNotHappen);
    }

    @Override
    public Map<String, String> responseHeader(HttpServletRequest request) {
        Optional<WebtauServerOverride> found = findOverride(request);
        return found.map(override -> override.responseHeader(request))
                .orElseThrow(this::shouldNotHappen);
    }

    @Override
    public int responseStatusCode(HttpServletRequest request) {
        Optional<WebtauServerOverride> found = findOverride(request);
        return found.map(override -> override.responseStatusCode(request))
                .orElseThrow(this::shouldNotHappen);
    }

    private Optional<WebtauServerOverride> findOverride(HttpServletRequest request) {
        return findOverride(request.getMethod(), request.getRequestURI());
    }

    private Optional<WebtauServerOverride> findOverride(String method, String uri) {
        return overrides.stream().filter(override -> override.matchesUri(method, uri)).findFirst();
    }

    private RuntimeException shouldNotHappen() {
        return new IllegalStateException("should not happen");
    }
}
