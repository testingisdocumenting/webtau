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

package org.testingisdocumenting.webtau.db;

import java.util.Arrays;
import java.util.stream.Collectors;

class TableHeaderConverters {
    static String unmodified(String columnName) {
        return columnName;
    }

    static String toUpperCase(String columnName) {
        return columnName.toUpperCase();
    }

    static String underscoreToCamelCase(String columnName) {
        columnName = columnName.toLowerCase();

        final String[] parts = columnName.split("_");
        return parts[0] + Arrays.stream(parts)
                .skip(1)
                .filter(p -> !p.isEmpty())
                .map(p -> Character.toUpperCase(p.charAt(0)) + p.substring(1))
                .collect(Collectors.joining(""));
    }
}
