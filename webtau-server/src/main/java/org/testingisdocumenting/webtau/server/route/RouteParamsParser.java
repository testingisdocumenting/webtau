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

import org.apache.commons.lang3.StringEscapeUtils;
import org.testingisdocumenting.webtau.utils.RegexpUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RouteParamsParser {
    private final String pathDefinition;
    private final Pattern pathDefinitionRegexp;

    public RouteParamsParser(String pathDefinition) {
        this.pathDefinition = pathDefinition;
        this.pathDefinitionRegexp = buildRegexp();
    }

    public boolean matches(String url) {
        return false;
    }

    public RouteParams parse(String url) {
        return null;
    }

    private Pattern buildRegexp() {
        Pattern pattern = Pattern.compile("/my/\\^super\\-path/(<id>w+)/hello/(<name>w+)");

        Pattern charsToEscape = Pattern.compile("([<(\\[^\\-=\\\\$!|\\])?*+.>])");
        String escaped = RegexpUtils.replaceAll(pathDefinition, charsToEscape, (m) -> "\\\\" + m.group(1));

        String namedParamRegexp = "\\{(\\w+)}";

        String pathRegexp = escaped.replaceAll(namedParamRegexp, "(<$1>\\w+)");
//        return Pattern.compile(pathRegexp);

        return null;
    }
}
