package com.twosigma.webtau.http.datanode;

import java.util.Map;

import com.twosigma.webtau.data.converters.ToMapConverter;

public class DataNodeToMapConverter implements ToMapConverter {
    @Override
    public Map<String, ?> convert(Object v) {
        if (v instanceof DataNode) {
            return ((DataNode) v).asMap();
        }

        return null;
    }
}
