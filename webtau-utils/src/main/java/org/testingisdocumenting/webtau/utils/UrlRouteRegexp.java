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

package org.testingisdocumenting.webtau.utils;

import java.util.Set;
import java.util.regex.Pattern;

public class UrlRouteRegexp {
    private final Pattern regexp;
    private final Set<String> groupNames;

    public UrlRouteRegexp(Pattern regexp, Set<String> groupNames) {
        this.regexp = regexp;
        this.groupNames = groupNames;
    }

    public boolean matches(String url) {
        return regexp.matcher(url).find();
    }

    public Pattern getRegexp() {
        return regexp;
    }

    public Set<String> getGroupNames() {
        return groupNames;
    }
}
