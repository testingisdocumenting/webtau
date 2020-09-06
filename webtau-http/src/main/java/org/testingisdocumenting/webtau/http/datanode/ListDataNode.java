package org.testingisdocumenting.webtau.http.datanode;

import org.testingisdocumenting.webtau.data.traceable.TraceableValue;
import org.testingisdocumenting.webtau.http.datacoverage.DataNodeToMapOfValuesConverter;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class ListDataNode implements DataNode {
    private final DataNodeId id;
    private final List<DataNode> values;

    public ListDataNode(DataNodeId id, List<DataNode> values) {
        //TODO assert not null values
        this.id = id;
        this.values = values;
    }

    @Override
    public DataNodeId id() {
        return id;
    }

    @Override
    public DataNode get(String nameOrPath) {
        if (values.stream().noneMatch(v -> v.has(nameOrPath))) {
            return new NullDataNode(id.child(nameOrPath));
        }

        return new ListDataNode(id.child(nameOrPath),
                values.stream()
                        .map(n -> n.get(nameOrPath))
                        .collect(Collectors.toList()));
    }

    @Override
    public boolean has(String pathOrName) {
        return !get(pathOrName).isNull();
    }

    @Override
    public DataNode get(int idx) {
        return (idx < 0 || idx >= values.size()) ?
                new NullDataNode(id.peer(idx)):
                values.get(idx);
    }

    @Override
    public TraceableValue getTraceableValue() {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E get() {
        return (E) values.stream().map(DataNode::get).collect(toList());
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public boolean isSingleValue() {
        return false;
    }

    @Override
    public List<DataNode> elements() {
        return Collections.unmodifiableList(values);
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
        return values.size();
    }

    @Override
    public Map<String, DataNode> asMap() {
        return Collections.emptyMap();
    }

    @Override
    public String toString() {
        return "[" + values.stream().map(DataNode::toString).collect(joining(", ")) + "]";
    }
}
