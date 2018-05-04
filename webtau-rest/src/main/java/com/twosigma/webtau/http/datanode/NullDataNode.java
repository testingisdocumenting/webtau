package com.twosigma.webtau.http.datanode;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.twosigma.webtau.data.traceable.TraceableValue;

public class NullDataNode implements DataNode {
    private final DataNodeId id;

    public NullDataNode(DataNodeId id) {
        this.id = id;
    }

    @Override
    public DataNodeId id() {
        return id;
    }

    @Override
    public DataNode get(String name) {
        return new NullDataNode(id.child(name));
    }

    @Override
    public DataNode get(int idx) {
        return new NullDataNode(id.peer(idx));
    }

    @Override
    public TraceableValue get() {
        return new TraceableValue(null);
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public boolean isSingleValue() {
        return true;
    }

    @Override
    public List<DataNode> all() {
        return Collections.singletonList(new NullDataNode(id.peer(0)));
    }

    @Override
    public int numberOfChildren() {
        return 0;
    }

    @Override
    public int numberOfElements() {
        return 0;
    }

    @Override
    public Map<String, DataNode> asMap() {
        return Collections.emptyMap();
    }

    @Override
    public String toString() {
        return "[null node]@" + id;
    }
}
