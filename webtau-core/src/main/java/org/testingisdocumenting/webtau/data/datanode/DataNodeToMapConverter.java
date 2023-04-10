/*
 * Copyright 2020 webtau maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.webtau.data.datanode;

import org.testingisdocumenting.webtau.data.converters.ToMapConverter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataNodeToMapConverter implements ToMapConverter {
    @Override
    public Map<String, ?> convert(Object v) {
        if (v instanceof DataNode) {
            return convertToMap((DataNode) v);
        }

        return null;
    }

    public static Map<String, DataNode> convertToMap(DataNode node) {
        return node.children().stream()
                .collect(Collectors.toMap(n -> n.id().getName(), Function.identity(), (a, b) -> a, LinkedHashMap::new));
    }
}
