/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.http.resource;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.ActualPathAndDescriptionAware;
import org.testingisdocumenting.webtau.expectation.ActualValueAware;
import org.testingisdocumenting.webtau.expectation.ActualValueExpectations;
import org.testingisdocumenting.webtau.http.Http;
import org.testingisdocumenting.webtau.data.datanode.DataNodeId;
import org.testingisdocumenting.webtau.data.datanode.DataNodeReturnNoConversionWrapper;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.utils.CollectionUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class HttpLazyResponseValue implements ActualValueExpectations, ActualValueAware, ActualPathAndDescriptionAware {
    // we use _webtauInternal to avoid automatic groovy properties resolution and clash when using shortcut like resource.list[0].id
    private final DataNodeId _webtauInternalId;
    private final HttpResourceDefinition _webtauInternalResourceDefinition;

    HttpLazyResponseValue(HttpResourceDefinition resourceDefinition, DataNodeId id) {
        this._webtauInternalResourceDefinition = resourceDefinition;
        this._webtauInternalId = id;
    }

    /**
     * declares response at a specific path based using current parent path as a base, e.g. details.price[0]
     * @param path path to a value
     * @return lazy response value
     */
    public HttpLazyResponseValue get(String path) {
        return new HttpLazyResponseValue(_webtauInternalResourceDefinition, _webtauInternalId.concat(path));
    }

    /**
     * declares response at a specific index using current parent path as a base
     * @param idx index in the response
     * @return lazy response value
     */
    public HttpLazyResponseValue get(int idx) {
        return new HttpLazyResponseValue(_webtauInternalResourceDefinition, _webtauInternalId.peer(idx));
    }

    /**
     * alias to {@link #get(String)} for Groovy DSL
     * @param idx index in the response
     * @return lazy response value
     */
    public HttpLazyResponseValue getAt(int idx) {
        return get(idx);
    }

    @Override
    public StepReportOptions shouldReportOption() {
        return StepReportOptions.REPORT_ALL;
    }

    @Override
    public boolean isDisabledMatcherStepOutput() {
        return true;
    }

    @Override
    public Object actualValue() {
        DataNodeReturnNoConversionWrapper returnWrapper = Http.http.get(_webtauInternalResourceDefinition.buildUrl(), (header, body) -> _webtauInternalId.getPath().isEmpty() ?
                new DataNodeReturnNoConversionWrapper(body):
                new DataNodeReturnNoConversionWrapper(body.get(_webtauInternalId.getPath())));

        return returnWrapper.getDataNode();
    }

    @Override
    public ValuePath actualPath() {
        return new ValuePath(_webtauInternalId.getPath());
    }

    @Override
    public TokenizedMessage describe() {
        TokenizedMessage result = tokenizedMessage().classifier("value").of().value(_webtauInternalResourceDefinition).colon();
        if (!_webtauInternalId.getPath().isEmpty()) {
            result.id(_webtauInternalId.getPath());
        }

        return result;
    }

    public HttpLazyResponseValue of(String firstKey, Object firstValue, Object... restKv) {
        Map<String, Object> map = CollectionUtils.map(firstKey, firstValue, restKv);
        return of(map);
    }

    public HttpLazyResponseValue of(String param) {
        Set<String> paramNames = _webtauInternalResourceDefinition.getParamNames();
        if (paramNames.isEmpty()) {
            throw new IllegalArgumentException("route definition must have one parameter, but definition has zero: " + _webtauInternalResourceDefinition.getRoute());
        }

        return of(Collections.singletonMap(paramNames.iterator().next(), param));
    }

    public HttpLazyResponseValue of(Map<String, Object> routeParams) {
        Set<String> paramNames = _webtauInternalResourceDefinition.getParamNames();
        if (paramNames.isEmpty()) {
            throw new IllegalArgumentException("no route parameter names found in the definition: " + _webtauInternalResourceDefinition.getRoute());
        }

        if (routeParams.size() != paramNames.size() || !paramNames.containsAll(routeParams.keySet())) {
            throw new IllegalArgumentException("route parameter names mismatch with provided, expected names: " +
                    paramNames + ", given: " + routeParams.keySet());
        }

        return new HttpLazyResponseValue(_webtauInternalResourceDefinition.withRouteParams(routeParams), _webtauInternalId);
    }
}
