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

import org.testingisdocumenting.webtau.server.route.RouteParams;
import org.testingisdocumenting.webtau.server.route.RouteParamsParser;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

/**
 * server override
 */
public class WebtauServerOverrideRouteFake implements WebtauServerOverride {
    private final String method;
    private final RouteParamsParser routeParamsParser;
    private final String responseType;
    private final Function<RouteParams, Integer> statusCodeFunc;
    private final Function<RouteParams, String> responseFunc;

    public WebtauServerOverrideRouteFake(String method, String urlWithParams, String responseType,
                                         Function<RouteParams, Integer> statusCodeFunc,
                                         Function<RouteParams, String> responseFunc) {
        routeParamsParser = new RouteParamsParser(urlWithParams);
        this.method = method.toUpperCase();
        this.responseType = responseType;
        this.statusCodeFunc = statusCodeFunc;
        this.responseFunc = responseFunc;
    }

    @Override
    public boolean matchesUri(String method, String uri) {
        return this.method.equals(method.toUpperCase()) &&
                this.routeParamsParser.matches(uri);
    }

    @Override
    public String overrideId() {
        return method + "-" + routeParamsParser.getPathDefinition();
    }

    @Override
    public byte[] responseBody(HttpServletRequest request) {
        String response = responseFunc.apply(routeParamsParser.parse(request.getRequestURI()));
        return response == null ? null : response.getBytes();
    }

    @Override
    public String responseType(HttpServletRequest request) {
        return responseType;
    }

    @Override
    public Map<String, String> responseHeader(HttpServletRequest request) {
        return Collections.emptyMap();
    }

    @Override
    public int responseStatusCode(HttpServletRequest request) {
        return statusCodeFunc.apply(routeParamsParser.parse(request.getRequestURI()));
    }

    @Override
    public String toString() {
        return "WebtauServerOverrideRouteFake{" +
                "method='" + method + '\'' +
                ", route=" + routeParamsParser.getPathDefinition() +
                ", responseType='" + responseType + '\'' +
                '}';
    }
}
