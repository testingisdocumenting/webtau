package com.twosigma.webtau.http.datanode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.twosigma.webtau.data.traceable.TraceableValue;

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
