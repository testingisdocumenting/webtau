package com.twosigma.webtau.http.datanode;

import com.twosigma.webtau.data.table.header.CompositeKeyUnderlyingValueExtractor;

public class CompositeKeyDataNodeValueExtractor implements CompositeKeyUnderlyingValueExtractor {
    @Override
    public boolean handles(Object value) {
        return value instanceof DataNode;
    }

    @Override
    public Object extract(Object value) {
        return ((DataNode)value).get();
    }
}
