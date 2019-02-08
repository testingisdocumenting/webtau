/*
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

package com.twosigma.webtau.http.datanode;

import com.twosigma.webtau.data.traceable.TraceableValue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DataNodeBuilder {
    public static DataNode fromMap(DataNodeId id, Map<String, Object> map) {
        return fromValue(id, map);
    }

    public static DataNode fromList(DataNodeId id, List<Object> list) {
        return fromValue(id, list);
    }

    private static Map<String, DataNode> buildMapOfNodes(DataNodeId id, Map<String, Object> map) {
        Map<String, DataNode> result = new LinkedHashMap<>();
        for (Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            result.put(key, fromValue(id.child(key), value));
        }

        return result;
    }

    private static List<DataNode> buildListOfNodes(DataNodeId id, List<Object> values) {
        List<DataNode> result = new ArrayList<>();
        int idx = 0;
        for (Object value : values) {
            result.add(fromValue(id.peer(idx), value));
            idx++;
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private static DataNode fromValue(DataNodeId id, Object value) {
        if (value instanceof Map) {
            return new StructuredDataNode(id, buildMapOfNodes(id, (Map<String, Object>)value));
        } else if (value instanceof List) {
            return new StructuredDataNode(id, buildListOfNodes(id, (List<Object>)value));
        } else {
            return new StructuredDataNode(id, new TraceableValue(value));
        }
    }
}
