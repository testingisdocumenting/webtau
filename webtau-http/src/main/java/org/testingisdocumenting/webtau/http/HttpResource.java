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
import org.testingisdocumenting.webtau.http.validation.HttpValidationResult;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.reporter.WebTauStep;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

import static org.testingisdocumenting.webtau.http.Http.*;

public class HttpResource implements DataNode {
    private final String givenUrl;
    private final ThreadLocal<DataNode> lastResponse = ThreadLocal.withInitial(() -> null);

    public HttpResource(String givenUrl) {
        this.givenUrl = givenUrl;
    }

    @Override
    public TokenizedMessage describe() {
        updateResponseIfRequired();
        return getLastResponse().describe();
    }

    @Override
    public DataNodeId id() {
        updateResponseIfRequired();
        return getLastResponse().id();
    }

    @Override
    public DataNode get(String pathOrName) {
        updateResponseIfRequired();
        return getLastResponse().get(pathOrName);
    }

    @Override
    public boolean has(String pathOrName) {
        updateResponseIfRequired();
        return getLastResponse().has(pathOrName);
    }

    @Override
    public DataNode get(int idx) {
        updateResponseIfRequired();
        return getLastResponse().get(idx);
    }

    @Override
    public TraceableValue getTraceableValue() {
        updateResponseIfRequired();
        return getLastResponse().getTraceableValue();
    }

    @Override
    public <E> E get() {
        updateResponseIfRequired();
        return getLastResponse().get();
    }

    @Override
    public boolean isList() {
        updateResponseIfRequired();
        return getLastResponse().isList();
    }

    @Override
    public boolean isSingleValue() {
        updateResponseIfRequired();
        return getLastResponse().isSingleValue();
    }

    @Override
    public List<DataNode> elements() {
        updateResponseIfRequired();
        return getLastResponse().elements();
    }

    @Override
    public Collection<DataNode> children() {
        updateResponseIfRequired();
        return getLastResponse().children();
    }

    @Override
    public int numberOfChildren() {
        updateResponseIfRequired();
        return getLastResponse().numberOfChildren();
    }

    @Override
    public int numberOfElements() {
        updateResponseIfRequired();
        return getLastResponse().numberOfElements();
    }

    @Override
    public boolean isNull() {
        updateResponseIfRequired();
        return getLastResponse().isNull();
    }

    @Override
    public boolean isBinary() {
        updateResponseIfRequired();
        return getLastResponse().isBinary();
    }

    @Override
    public ActualPath actualPath() {
        updateResponseIfRequired();
        return getLastResponse().actualPath();
    }

    @Override
    public int compareTo(Object rhv) {
        updateResponseIfRequired();
        return getLastResponse().compareTo(rhv);
    }

    @Override
    public void prettyPrint(ConsoleOutput console) {
        updateResponseIfRequired();
        getLastResponse().prettyPrint(console);
    }

    @Override
    public Iterator<DataNode> iterator() {
        updateResponseIfRequired();
        return getLastResponse().iterator();
    }

    @Override
    public void forEach(Consumer<? super DataNode> action) {
        updateResponseIfRequired();
        getLastResponse().forEach(action);
    }

    @Override
    public Spliterator<DataNode> spliterator() {
        updateResponseIfRequired();
        return getLastResponse().spliterator();
    }

    /**
     * issue HTTP get for the resource to get up to date response,
     * by default resource is read once and caches the response
     * @return this resource
     */
    public HttpResource reRead() {
        lastResponse.set(read());
        return this;
    }

    private DataNode getLastResponse() {
        return lastResponse.get();
    }

    private void updateResponseIfRequired() {
        if (lastResponse.get() != null) {
            return;
        }

        reRead();
    }

    private HttpResourceResponse read() {
        HttpValidationResult validationResult = http.createValidationResult(GET_METHOD, givenUrl, HttpHeader.EMPTY, null);
        WebTauStep step = http.createHttpStep(validationResult, http::getToFullUrl, (header, body) -> null);

        step.execute(StepReportOptions.REPORT_ALL);
        return new HttpResourceResponse(givenUrl, GET_METHOD, validationResult.getBodyNode());
    }
}
