package org.testingisdocumenting.webtau.http.datanode;

import org.testingisdocumenting.webtau.data.traceable.TraceableValue;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ValueDataNode implements DataNode {
    private final DataNodeId id;
    private final TraceableValue value;

    public ValueDataNode(DataNodeId id, TraceableValue value) {
        //TODO check when value might be null
        this.id = id;
        this.value = value;
    }

    @Override
    public DataNodeId id() {
        return id;
    }

    @Override
    public DataNode get(String pathOrName) {
        return new NullDataNode(id.child(pathOrName));
    }

    @Override
    public boolean has(String pathOrName) {
        return false;
    }

    @Override
    public DataNode get(int idx) {
        return new NullDataNode(id.peer(idx));
    }

    @Override
    public TraceableValue getTraceableValue() {
        return value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E get() {
        if (value == null) {
            return null;
        }

        return (E) value.getValue();
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
    public List<DataNode> elements() {
        return Collections.emptyList();
    }

    @Override
    public Iterator<DataNode> iterator() {
        return elements().iterator();
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
        return value == null ? "null" : value.toString();
    }
}
