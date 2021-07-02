/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.http;

import org.testingisdocumenting.webtau.console.ConsoleOutput;
import org.testingisdocumenting.webtau.data.traceable.TraceableValue;
import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.http.datanode.DataNode;
import org.testingisdocumenting.webtau.http.datanode.DataNodeId;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class HttpResourceResponse implements DataNode {
    private final String givenUrl;
    private final String method;
    private final DataNode lastResponse;

    public HttpResourceResponse(String givenUrl, String method, DataNode lastResponse) {
        this.givenUrl = givenUrl;
        this.method = method;
        this.lastResponse = lastResponse;
    }

    @Override
    public DataNodeId id() {
        return lastResponse.id();
    }

    @Override
    public DataNode get(String pathOrName) {
        return lastResponse.get(pathOrName);
    }

    @Override
    public boolean has(String pathOrName) {
        return lastResponse.has(pathOrName);
    }

    @Override
    public DataNode get(int idx) {
        return lastResponse.get(idx);
    }

    @Override
    public TraceableValue getTraceableValue() {
        return lastResponse.getTraceableValue();
    }

    @Override
    public <E> E get() {
        return lastResponse.get();
    }

    @Override
    public boolean isList() {
        return lastResponse.isList();
    }

    @Override
    public boolean isSingleValue() {
        return lastResponse.isSingleValue();
    }

    @Override
    public List<DataNode> elements() {
        return lastResponse.elements();
    }

    @Override
    public Collection<DataNode> children() {
        return lastResponse.children();
    }

    @Override
    public int numberOfChildren() {
        return lastResponse.numberOfChildren();
    }

    @Override
    public int numberOfElements() {
        return lastResponse.numberOfElements();
    }

    @Override
    public boolean isNull() {
        return lastResponse.isNull();
    }

    @Override
    public boolean isBinary() {
        return lastResponse.isBinary();
    }

    @Override
    public ActualPath actualPath() {
        return lastResponse.actualPath();
    }

    @Override
    public int compareTo(Object rhv) {
        return lastResponse.compareTo(rhv);
    }

    @Override
    public void prettyPrint(ConsoleOutput console) {
        lastResponse.prettyPrint(console);
    }

    @Override
    public Iterator<DataNode> iterator() {
        return lastResponse.iterator();
    }

    @Override
    public void forEach(Consumer<? super DataNode> action) {
        lastResponse.forEach(action);
    }

    @Override
    public Spliterator<DataNode> spliterator() {
        return lastResponse.spliterator();
    }
}
