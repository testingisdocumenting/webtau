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

import org.testingisdocumenting.webtau.utils.RegexpUtils;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RouteParamsParser {
    private final Pattern CHARS_TO_ESCAPE = Pattern.compile("([<(\\[^\\-=\\\\$!|\\])?*+.>])");
    private final Pattern NAMED_PARAM_REGEXP = Pattern.compile("\\{(\\w+)}");

    private final String pathDefinition;
    private final Pattern pathDefinitionRegexp;
    private final Set<String> groupNames;

    public RouteParamsParser(String pathDefinition) {
        this.pathDefinition = pathDefinition;
        this.groupNames = new LinkedHashSet<>();
        this.pathDefinitionRegexp = buildRegexp();
    }

    public boolean matches(String url) {
        return pathDefinitionRegexp.matcher(url).find();
    }

    public String getPathDefinition() {
        return pathDefinition;
    }

    public RouteParams parse(String url) {
        Matcher matcher = pathDefinitionRegexp.matcher(url);
        if (!matcher.find()) {
            throw new RuntimeException("url <" + url + "> does not match pattern <" +
                    pathDefinitionRegexp.pattern() + ">");
        }

        RouteParams params = new RouteParams();
        groupNames.forEach(name -> params.add(name, matcher.group(name)));

        return params;
    }

    public Set<String> getGroupNames() {
        return groupNames;
    }

    private Pattern buildRegexp() {
        String escaped = RegexpUtils.replaceAll(pathDefinition, CHARS_TO_ESCAPE, (m) -> {
            String name = m.group(1);
            return "\\\\" + name;
        });

        String pathRegexp = RegexpUtils.replaceAll(escaped, NAMED_PARAM_REGEXP, (m) -> {
            String name = m.group(1);
            groupNames.add(name);

            return "(?<" + name + ">\\\\w+)";
        });

        return Pattern.compile(pathRegexp);
    }
}
