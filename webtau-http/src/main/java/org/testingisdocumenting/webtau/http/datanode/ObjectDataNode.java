package org.testingisdocumenting.webtau.http.datanode;

import org.testingisdocumenting.webtau.data.traceable.TraceableValue;
import org.testingisdocumenting.webtau.http.datacoverage.DataNodeToMapOfValuesConverter;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class ObjectDataNode implements DataNode {
    private final DataNodeId id;
    private final Map<String, DataNode> children;

    public ObjectDataNode(DataNodeId id, Map<String, DataNode> children) {
        //TODO assert not null children
        this.id = id;
        this.children = children;
    }

    @Override
    public DataNodeId id() {
        return id;
    }

    @Override
    public DataNode get(String nameOrPath) {
        int dotIdx = nameOrPath.indexOf('.');
        if (dotIdx == -1) {
            // simple name
            return getChild(nameOrPath);
        }

        String rootName = nameOrPath.substring(0, dotIdx);
        DataNode root = getChild(rootName);
        String pathUnderRoot = nameOrPath.substring(dotIdx + 1);

        return root.get(pathUnderRoot);
    }

    private DataNode getChild(String name) {
        int openBraceIdx = name.indexOf('[');
        int closeBraceIdx = name.indexOf(']');
        if (openBraceIdx != -1 && closeBraceIdx != -1) {
            return getIndexedChild(name, openBraceIdx, closeBraceIdx);
        } else if (openBraceIdx != -1 || closeBraceIdx != -1) {
            throw new IllegalArgumentException("Requested name " + name + " is not a simple name nor does it contain a properly formatted index");
        }

        // simple name
        return (children.containsKey(name)) ?
                children.get(name) :
                new NullDataNode(id.child(name));
    }

    private DataNode getIndexedChild(String name, int openBraceIdx, int closeBraceIdx) {
        int additionalOpenIdx = name.indexOf('[', openBraceIdx + 1);
        int additionalCloseId = name.indexOf(']', closeBraceIdx + 1);

        if (additionalOpenIdx != -1 || additionalCloseId != -1 || openBraceIdx > closeBraceIdx) {
            throw new IllegalArgumentException("Requested name " + name + " contains mismatched indexing brackets");
        }

        String indexStr = name.substring(openBraceIdx + 1, closeBraceIdx);
        try {
            int idx = Integer.parseInt(indexStr);
            String nameWithoutIndex = name.substring(0, openBraceIdx);
            DataNode node = get(nameWithoutIndex);

            if (idx < 0) {
                idx = node.numberOfElements() + idx;
            }

            return node.get(idx);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Requested index " + indexStr + " of name " + name.substring(0, openBraceIdx) + " is not an integer");
        }
    }

    @Override
    public boolean has(String pathOrName) {
        return !get(pathOrName).isNull();
    }

    @Override
    public DataNode get(int idx) {
        return new NullDataNode(id.peer(idx));
    }

    @Override
    public TraceableValue getTraceableValue() {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E get() {
        return (E) new DataNodeToMapOfValuesConverter((id, traceableValue) -> traceableValue.getValue())
                .convert(this);
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public boolean isSingleValue() {
        return false;
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
        return children.size();
    }

    @Override
    public int numberOfElements() {
        return 0;
    }

    @Override
    public Map<String, DataNode> asMap() {
        return Collections.unmodifiableMap(children);
    }

    @Override
    public String toString() {
        return "{" + children.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).collect(joining(", "))  + "}";
    }
}
