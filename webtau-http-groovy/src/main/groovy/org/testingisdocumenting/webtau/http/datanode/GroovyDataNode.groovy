/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.webtau.http.datanode

import org.testingisdocumenting.webtau.data.traceable.TraceableValue
import org.testingisdocumenting.webtau.expectation.ActualPath
import org.testingisdocumenting.webtau.http.datacoverage.DataNodeToMapOfValuesConverter

import static org.testingisdocumenting.webtau.groovy.ast.ShouldAstTransformation.SHOULD_BE_REPLACED_MESSAGE

//TODO does this need to be IdValidatedDataNode?
class GroovyDataNode implements DataNodeExpectations, IdValidatedDataNode {
    private DataNode node

    GroovyDataNode(final DataNode node) {
        this.node = node
    }

    def getProperty(String name) {
        switch (name) {
            case "should":
            case "shouldNot":
                throw new IllegalStateException(SHOULD_BE_REPLACED_MESSAGE)
            case "waitTo":
            case "waitToNot":
                throw new UnsupportedOperationException("waitTo and waitToNot is not supported for data node")
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
    DataNode get(String pathOrName) {
        return new GroovyDataNode(node.get(pathOrName))
    }

    @Override
    boolean has(String pathOrName) {
        return node.has(pathOrName)
    }

    @Override
    DataNode get(int idx) {
        return new GroovyDataNode(node.get(idx))
    }

    @Override
    TraceableValue getTraceableValue() {
        return node.getTraceableValue()
    }

    @Override
    <E> E get() {
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
    List<DataNode> elements() {
        return node.elements().collect { new GroovyDataNode(it) }
    }

    @Override
    Iterator<DataNode> iterator() {
        return elements().iterator()
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

    void each(Closure consumer) {
        node.elements().each(delegateToDataNodeClosure(consumer))
    }

    DataNode find(Closure predicate) {
        def result = node.elements().find(delegateToNodeAndRemovedDataNodeFromClosure(predicate))
        return (result instanceof DataNode) ?
                new GroovyDataNode(result) :
                result
    }

    DataNode findAll(Closure predicate) {
        def list = node.elements().findAll(delegateToNodeAndRemovedDataNodeFromClosure(predicate))
        return wrapIntoDataNode('findAll', list)
    }

    List collect(Closure transformation) {
        return node.elements().collect(delegateToNodeAndRemovedDataNodeFromClosure(transformation))
    }

    @Override
    String toString() {
        return node.toString()
    }

    @Override
    ActualPath actualPath() {
        return node.actualPath()
    }

    private DataNode wrapIntoDataNode(String operationId, List list) {
        return new GroovyDataNode(new StructuredDataNode(node.id().child(operationId), list))
    }

    private static Closure delegateToNodeAndRemovedDataNodeFromClosure(Closure original) {
        def newClosure = { dataNode ->
            def converter = new DataNodeToMapOfValuesConverter({ id, traceableValue ->
                traceableValue.getValue()
            })

            def convertedToSimple = converter.convert(dataNode)
            Closure cloned = cloneAndAssignDelegate(original, convertedToSimple)

            return cloned.call(convertedToSimple)
        }

        return newClosure
    }

    private static Closure delegateToDataNodeClosure(Closure original) {
        def newClosure = { dataNode ->
            Closure cloned = cloneAndAssignDelegate(original, dataNode)
            return cloned.call(dataNode)
        }

        return newClosure
    }

    private static Closure cloneAndAssignDelegate(Closure original, Object delegate) {
        Closure cloned = original.clone() as Closure
        cloned.resolveStrategy = Closure.DELEGATE_FIRST
        cloned.delegate = delegate

        return cloned
    }
}
