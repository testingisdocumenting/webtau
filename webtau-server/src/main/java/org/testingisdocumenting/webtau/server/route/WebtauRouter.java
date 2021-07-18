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

public class WebtauRouter implements WebtauServerOverride {
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String TEXT_CONTENT_TYPE = "text/plain";
    private static final String HTML_CONTENT_TYPE = "text/html";

    private final WebtauServerOverrideList overrideList;

    private final String contentType;

    public WebtauRouter(String id) {
        this.overrideList = new WebtauServerOverrideList(id);
        this.contentType = JSON_CONTENT_TYPE;
    }

    private WebtauRouter(String contentType, WebtauServerOverrideList overrideList) {
        this.contentType = contentType;
        this.overrideList = overrideList;
    }

    public WebtauRouter json() {
        return new WebtauRouter(JSON_CONTENT_TYPE, overrideList);
    }

    public WebtauRouter text() {
        return new WebtauRouter(TEXT_CONTENT_TYPE, overrideList);
    }

    public WebtauRouter html() {
        return new WebtauRouter(HTML_CONTENT_TYPE, overrideList);
    }

    public void setId(String id) {
        this.overrideList.setListId(id);
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

    public WebtauRouter get(String urlWithParams, Function<RouteParams, Integer> statusCodeFunc, Function<RouteParams, Map<String, Object>> responseFunc) {
        return registerJson("GET", urlWithParams, statusCodeFunc, responseFunc);
    }

    public WebtauRouter get(String urlWithParams, Function<RouteParams, Map<String, Object>> responseFunc) {
        return get(urlWithParams, (params) -> 200, responseFunc);
    }

    public WebtauRouter post(String urlWithParams, Function<RouteParams, Integer> statusCodeFunc, Function<RouteParams, Map<String, Object>> responseFunc) {
        return registerJson("POST", urlWithParams, statusCodeFunc, responseFunc);
    }

    public WebtauRouter post(String urlWithParams, Function<RouteParams, Map<String, Object>> responseFunc) {
        return post(urlWithParams, (params) -> 201, responseFunc);
    }

    public WebtauRouter put(String urlWithParams, Function<RouteParams, Integer> statusCodeFunc, Function<RouteParams, Map<String, Object>> responseFunc) {
        return registerJson("PUT", urlWithParams, statusCodeFunc, responseFunc);
    }

    public WebtauRouter put(String urlWithParams, Function<RouteParams, Map<String, Object>> responseFunc) {
        return put(urlWithParams, (params) -> 200, responseFunc);
    }

    public WebtauRouter delete(String urlWithParams, Function<RouteParams, Integer> statusCodeFunc, Function<RouteParams, Map<String, Object>> responseFunc) {
        return registerJson("DELETE", urlWithParams, statusCodeFunc, responseFunc);
    }

    public WebtauRouter delete(String urlWithParams, Function<RouteParams, Map<String, Object>> responseFunc) {
        return delete(urlWithParams, (params) -> 200, responseFunc);
    }

    public WebtauRouter patch(String urlWithParams, Function<RouteParams, Integer> statusCodeFunc, Function<RouteParams, Map<String, Object>> responseFunc) {
        return registerJson("PATCH", urlWithParams, statusCodeFunc, responseFunc);
    }

    public WebtauRouter patch(String urlWithParams, Function<RouteParams, Map<String, Object>> responseFunc) {
        return patch(urlWithParams, (params) -> 200, responseFunc);
    }

    private WebtauRouter registerJson(String method, String urlWithParams, Function<RouteParams, Integer> statusCodeFunc, Function<RouteParams, Map<String, Object>> responseFunc) {
        overrideList.addOverride(new WebtauServerOverrideRouteFake(method, urlWithParams, contentType,
                statusCodeFunc,
                (params) -> JsonUtils.serialize(responseFunc.apply(params))));
        return this;
    }
}
