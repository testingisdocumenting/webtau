package org.testingisdocumenting.webtau.http.datanode;

import java.util.LinkedHashMap;
import java.util.Map;

public class NonExistentNodesTracker {
    private final Map<DataNodeId, DataNode> queriedNonExistentNodes = new LinkedHashMap<>();
    private final DataNodeId parentId;

    public NonExistentNodesTracker(DataNodeId parentId) {
        this.parentId = parentId;
    }

    public DataNode register(String childName) {
        return register(parentId.child(childName));
    }

    public DataNode register(int idx) {
        return register(parentId.peer(idx));
    }

    public DataNode register(DataNodeId id) {
        return queriedNonExistentNodes.computeIfAbsent(id, NullDataNode::new);
    }
}
