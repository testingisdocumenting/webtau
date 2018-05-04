package com.twosigma.webtau.http.datanode;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.twosigma.webtau.data.traceable.TraceableValue;

import static java.util.stream.Collectors.joining;

public class StructuredDataNode implements DataNode {
    private final DataNodeId id;

    private Map<String, DataNode> children;
    private TraceableValue value;
    private List<DataNode> values;

    private boolean isSingleValue;

    public StructuredDataNode(DataNodeId id, TraceableValue value) {
        this.id = id;
        this.value = value;
        this.isSingleValue = true;
    }

    public StructuredDataNode(DataNodeId id, List<DataNode> values) {
        this.id = id;
        this.values = values;
    }

    public StructuredDataNode(DataNodeId id, Map<String, DataNode> children) {
        this.id = id;
        this.children = children;
    }

    @Override
    public DataNodeId id() {
        return id;
    }

    @Override
    public DataNode get(String name) {
        return (children != null && children.containsKey(name)) ? children.get(name) : new NullDataNode(id.child(name));
    }

    @Override
    public DataNode get(int idx) {
        return (values == null || idx < 0 || idx >= values.size()) ?
            new NullDataNode(id.peer(idx)):
            values.get(idx);
    }

    @Override
    public TraceableValue get() {
        return value;
    }

    @Override
    public boolean isList() {
        return values != null;
    }

    @Override
    public boolean isSingleValue() {
        return isSingleValue;
    }

    @Override
    public List<DataNode> all() {
        return Collections.unmodifiableList(values);
    }

    @Override
    public int numberOfChildren() {
        return isSingleValue ? 0 :
            isList() ? 0 :
                children != null ? children.size() : 0;
    }

    @Override
    public int numberOfElements() {
        return isList() ? values.size() : 0;
    }

    @Override
    public Map<String, DataNode> asMap() {
        return Collections.unmodifiableMap(children);
    }

    @Override
    public String toString() {
        if (isSingleValue) {
            return value == null ? "null" : value.toString();
        }

        if (values != null) {
            return "[" + values.stream().map(DataNode::toString).collect(joining(", ")) + "]";
        }

        return "{" + children.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).collect(joining(", "))  + "}";
    }
}
