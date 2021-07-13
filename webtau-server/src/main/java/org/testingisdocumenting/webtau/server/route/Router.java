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

package org.testingisdocumenting.webtau.server.route;

import org.testingisdocumenting.webtau.server.WebtauServerOverride;
import org.testingisdocumenting.webtau.server.WebtauServerOverrideList;
import org.testingisdocumenting.webtau.server.WebtauServerOverrideRouteFake;
import org.testingisdocumenting.webtau.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

public class Router implements WebtauServerOverride {
    private final WebtauServerOverrideList overrideList;

    public Router(String id) {
        this.overrideList = new WebtauServerOverrideList(id);
    }

    @Override
    public boolean matchesUri(String method, String uri) {
        return overrideList.matchesUri(method, uri);
    }

    @Override
    public String overrideId() {
        return overrideList.overrideId();
    }

    @Override
    public byte[] responseBody(HttpServletRequest request) {
        return overrideList.responseBody(request);
    }

    @Override
    public String responseType(HttpServletRequest request) {
        return overrideList.responseType(request);
    }

    @Override
    public Map<String, String> responseHeader(HttpServletRequest request) {
        return Collections.emptyMap();
    }

    @Override
    public int responseStatusCode(HttpServletRequest request) {
        return overrideList.responseStatusCode(request);
    }

    @Override
    public String toString() {
        return overrideList.toString();
    }

    public void getJson(String urlWithParams, Function<RouteParams, Integer> statusCodeFunc, Function<RouteParams, Map<String, Object>> responseFunc) {
        registerJson("GET", urlWithParams, statusCodeFunc, responseFunc);
    }

    public void getJson(String urlWithParams, Function<RouteParams, Map<String, Object>> responseFunc) {
        getJson(urlWithParams, (params) -> 200, responseFunc);
    }

    public void postJson(String urlWithParams, Function<RouteParams, Integer> statusCodeFunc, Function<RouteParams, Map<String, Object>> responseFunc) {
        registerJson("POST", urlWithParams, statusCodeFunc, responseFunc);
    }

    public void postJson(String urlWithParams, Function<RouteParams, Map<String, Object>> responseFunc) {
        postJson(urlWithParams, (params) -> 201, responseFunc);
    }

    public void putJson(String urlWithParams, Function<RouteParams, Integer> statusCodeFunc, Function<RouteParams, Map<String, Object>> responseFunc) {
        registerJson("PUT", urlWithParams, statusCodeFunc, responseFunc);
    }

    public void putJson(String urlWithParams, Function<RouteParams, Map<String, Object>> responseFunc) {
        putJson(urlWithParams, (params) -> 200, responseFunc);
    }

    public void deleteJson(String urlWithParams, Function<RouteParams, Integer> statusCodeFunc, Function<RouteParams, Map<String, Object>> responseFunc) {
        registerJson("DELETE", urlWithParams, statusCodeFunc, responseFunc);
    }

    public void deleteJson(String urlWithParams, Function<RouteParams, Map<String, Object>> responseFunc) {
        deleteJson(urlWithParams, (params) -> 200, responseFunc);
    }

    public void patchJson(String urlWithParams, Function<RouteParams, Integer> statusCodeFunc, Function<RouteParams, Map<String, Object>> responseFunc) {
        registerJson("PATCH", urlWithParams, statusCodeFunc, responseFunc);
    }

    public void patchJson(String urlWithParams, Function<RouteParams, Map<String, Object>> responseFunc) {
        patchJson(urlWithParams, (params) -> 200, responseFunc);
    }

    private void registerJson(String method, String urlWithParams, Function<RouteParams, Integer> statusCodeFunc, Function<RouteParams, Map<String, Object>> responseFunc) {
        overrideList.addOverride(new WebtauServerOverrideRouteFake(method, urlWithParams, "application/json",
                statusCodeFunc,
                (params) -> JsonUtils.serialize(responseFunc.apply(params))));
    }
}
