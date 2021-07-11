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

import java.util.LinkedHashMap;
import java.util.Map;

public class RouteParams {
    private final Map<String, String> values;

    public RouteParams() {
        values = new LinkedHashMap<>();
    }

    void add(String name, String value) {
        values.put(name, value);
    }

    public String get(String name) {
        return values.get(name);
    }
}
