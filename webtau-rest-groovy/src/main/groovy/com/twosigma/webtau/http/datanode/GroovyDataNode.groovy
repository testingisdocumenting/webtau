package com.twosigma.webtau.http.datanode

import com.twosigma.webtau.data.traceable.TraceableValue
import com.twosigma.webtau.expectation.ActualPath
import com.twosigma.webtau.expectation.Should
import com.twosigma.webtau.expectation.ShouldAndWaitProperty

class GroovyDataNode implements DataNodeExpectations, DataNode {
    private DataNode node

    GroovyDataNode(final DataNode node) {
        this.node = node
    }

    def getProperty(String name) {
        switch (name) {
            case "should":
                return new ShouldAndWaitProperty<>(node, this.&should)
            case "shouldNot":
                return new ShouldAndWaitProperty<>(node, this.&shouldNot)
            case "waitTo":
                return new ShouldAndWaitProperty<>(node, this.&waitTo)
            case "waitToNot":
                return new ShouldAndWaitProperty<>(node, this.&waitToNot)
            default:
                return get(name)
        }
    }

    def getAt(Integer idx) {
        return get(idx)
    }

    @Override
    DataNodeId id() {
        return node.id()
    }

    @Override
    DataNode get(final String name) {
        return new GroovyDataNode(node.get(name))
    }

    @Override
    DataNode get(final int idx) {
        return new GroovyDataNode(node.get(idx))
    }

    @Override
    TraceableValue get() {
        return node.get()
    }

    @Override
    boolean isList() {
        return node.isList()
    }

    @Override
    boolean isSingleValue() {
        return node.isSingleValue()
    }

    @Override
    List<DataNode> all() {
        return node.all().collect { new GroovyDataNode(it) }
    }

    @Override
    int numberOfChildren() {
        return node.numberOfChildren()
    }

    @Override
    int numberOfElements() {
        return node.numberOfElements()
    }

    @Override
    Map<String, DataNode> asMap() {
        return node.asMap().entrySet().collectEntries { [it.key, new GroovyDataNode(it.value)] }
    }

    @Override
    String toString() {
        return node.toString()
    }

    @Override
    ActualPath actualPath() {
        return node.actualPath()
    }
}
