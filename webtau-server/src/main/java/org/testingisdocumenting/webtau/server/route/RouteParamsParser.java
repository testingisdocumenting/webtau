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

import org.testingisdocumenting.webtau.utils.UrlRouteRegexp;
import org.testingisdocumenting.webtau.utils.UrlUtils;

import java.util.Set;
import java.util.regex.Matcher;

public class RouteParamsParser {
    private final UrlRouteRegexp urlRouteRegexp;
    private final String pathDefinition;

    public RouteParamsParser(String pathDefinition) {
        this.pathDefinition = pathDefinition;
        this.urlRouteRegexp = UrlUtils.buildRouteRegexpAndGroupNames(pathDefinition);
    }

    public boolean matches(String url) {
        return urlRouteRegexp.matches(url);
    }

    public String getPathDefinition() {
        return pathDefinition;
    }

    public RouteParams parse(String url) {
        Matcher matcher = urlRouteRegexp.getRegexp().matcher(url);
        if (!matcher.find()) {
            throw new RuntimeException("url <" + url + "> does not match pattern <" +
                    urlRouteRegexp.getRegexp() + ">");
        }

        RouteParams params = new RouteParams();
        urlRouteRegexp.getGroupNames().forEach(name -> params.add(name, matcher.group(name)));

        return params;
    }

    public Set<String> getGroupNames() {
        return urlRouteRegexp.getGroupNames();
    }
}
