package com.twosigma.webtau.http.datacoverage;

import com.twosigma.webtau.data.traceable.TraceableValue;
import com.twosigma.webtau.http.datanode.DataNode;
import com.twosigma.webtau.http.datanode.DataNodeId;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataNodeToMapOfValuesConverter {
    private TraceableValueConverter traceableValueConverter;

    public DataNodeToMapOfValuesConverter(TraceableValueConverter traceableValueConverter) {
        this.traceableValueConverter = traceableValueConverter;
    }

    public Object convert(DataNode n) {
        if (n.isList()) {
            return convertToList(n);
        } else if (n.isSingleValue()) {
            return convertSingleValue(n.id(), n.get());
        } else {
            return convertToMap(n);
        }
    }

    private Map<String, Object> convertToMap(DataNode dataNode) {
        Map<String, Object> converted = new LinkedHashMap<>();
        dataNode.asMap().forEach((k, v) -> converted.put(k, convert(v)));

        return converted;
    }

    private List<Object> convertToList(DataNode dataNode) {
        List<Object> converted = new ArrayList<>();
        dataNode.all().forEach(n -> converted.add(convert(n)));

        return converted;
    }

    private Object convertSingleValue(DataNodeId id, TraceableValue value) {
        return traceableValueConverter.convert(id, value);
    }
}
