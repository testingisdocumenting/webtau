/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.http.validation;

import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.data.traceable.TraceableValue;
import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.http.HttpResponse;
import org.testingisdocumenting.webtau.http.datanode.DataNode;
import org.testingisdocumenting.webtau.http.datanode.DataNodeId;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Represents parsed body response
 * @see <a href="https://testingisdocumenting.org/webtau/HTTP/data-node">DataNode</a>
 */
public class BodyDataNode implements DataNode {
    private final HttpResponse response;
    private final DataNode body;

    public BodyDataNode(HttpResponse response, DataNode body) {
        this.response = response;
        this.body = body;
    }

    /**
     * Access to the raw textual content. Do not use it for business logic validation.
     * @return raw text content
     */
    public String getTextContent() {
        return response.getTextContent();
    }

    /**
     * Access to the raw binary content. Do not use it for business logic validation.
     * @return raw binary content, null if response is not binary
     */
    public byte[] getBinaryContent() {
        return response.getBinaryContent();
    }

    @Override
    public DataNodeId id() {
        return body.id();
    }

    @Override
    public DataNode get(String pathOrName) {
        return body.get(pathOrName);
    }

    @Override
    public boolean has(String pathOrName) {
        return body.has(pathOrName);
    }

    @Override
    public DataNode get(int idx) {
        return body.get(idx);
    }

    @Override
    public TraceableValue getTraceableValue() {
        return body.getTraceableValue();
    }

    @Override
    public <E> E get() {
        return body.get();
    }

    @Override
    public boolean isList() {
        return body.isList();
    }

    @Override
    public boolean isSingleValue() {
        return body.isSingleValue();
    }

    @Override
    public List<DataNode> elements() {
        return body.elements();
    }

    @Override
    public Collection<DataNode> children() {
        return body.children();
    }

    @Override
    public DataNode find(Predicate<DataNode> predicate) {
        return body.find(predicate);
    }

    @Override
    public DataNode findAll(Predicate<DataNode> predicate) {
        return body.findAll(predicate);
    }

    @Override
    public int numberOfChildren() {
        return body.numberOfChildren();
    }

    @Override
    public int numberOfElements() {
        return body.numberOfElements();
    }

    @Override
    public boolean isNull() {
        return body.isNull();
    }

    @Override
    public boolean isBinary() {
        return body.isBinary();
    }

    @Override
    public ActualPath actualPath() {
        return body.actualPath();
    }

    @Override
    public TokenizedMessage describe() {
        return body.describe();
    }

    @Override
    public int compareTo(Object rhv) {
        return body.compareTo(rhv);
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        body.prettyPrint(printer);
    }

    @Override
    public Iterator<DataNode> iterator() {
        return body.iterator();
    }
}
