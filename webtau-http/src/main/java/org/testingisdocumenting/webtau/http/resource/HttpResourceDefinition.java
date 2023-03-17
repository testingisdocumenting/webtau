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

package org.testingisdocumenting.webtau.http.resource;

import org.testingisdocumenting.webtau.console.ansi.Color;
import org.testingisdocumenting.webtau.console.ansi.FontStyle;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.utils.UrlRouteRegexp;
import org.testingisdocumenting.webtau.utils.UrlUtils;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class HttpResourceDefinition implements PrettyPrintable {
    private final String route;
    private final Set<String> paramNames;
    private final HttpRouteParams routeParams;

    public HttpResourceDefinition(String definition, Map<String, Object> params) {
        UrlRouteRegexp urlRouteRegexp = UrlUtils.buildRouteRegexpAndGroupNames(definition);
        route = definition;
        paramNames = new LinkedHashSet<>(urlRouteRegexp.getGroupNames());
        routeParams = new HttpRouteParams(params);
    }

    HttpResourceDefinition(String route, Set<String> paramNames, HttpRouteParams routeParams) {
        this.route = route;
        this.paramNames = paramNames;
        this.routeParams = routeParams;
    }

    public String buildUrl() {
        return UrlUtils.buildUrlFromPathDefinitionAndParams(route, routeParams.getParams());
    }

    public String getRoute() {
        return route;
    }

    public Set<String> getParamNames() {
        return paramNames;
    }

    public HttpResourceDefinition withRouteParams(Map<String, Object> params) {
        return new HttpResourceDefinition(route, paramNames, new HttpRouteParams(params));
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        Map<String, Object> coloredParams = new HashMap<>();
        routeParams.getParams().forEach((k, v) -> {
            coloredParams.put(k, Color.RESET.toString() + FontStyle.BOLD + v + Color.PURPLE);
        });

        String coloredUrl = UrlUtils.buildUrlFromPathDefinitionAndParams(route, coloredParams);
        printer.print(Color.PURPLE, coloredUrl);
    }
}
