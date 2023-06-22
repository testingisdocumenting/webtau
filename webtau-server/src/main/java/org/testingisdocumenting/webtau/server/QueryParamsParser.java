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

package org.testingisdocumenting.webtau.server;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

class QueryParamsParser {
    static Map<String, List<String>> parse(String query) {
        if (query.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, List<String>> result = new LinkedHashMap<>();
        String[] keyValueParts = query.split("&");

        for (String keyValueText : keyValueParts) {
            String[] keyValue = keyValueText.split("=");
            if (keyValue.length != 2) {
                continue;
            }

            String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
            String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);

            result.computeIfAbsent(key, (mapKey) -> new ArrayList<>()).add(value);
        }

        return result;
    }
}
