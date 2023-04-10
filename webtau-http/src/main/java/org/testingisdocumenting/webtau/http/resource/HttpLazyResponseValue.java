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
    private final DataNodeId id;
    private final HttpResourceDefinition resourceDefinition;

    HttpLazyResponseValue(HttpResourceDefinition resourceDefinition, DataNodeId id) {
        this.resourceDefinition = resourceDefinition;
        this.id = id;
    }

    public HttpLazyResponseValue get(String path) {
        // TODO if path starts with [ need a different `concat` method
        return new HttpLazyResponseValue(resourceDefinition, id.child(path));
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
        DataNodeReturnNoConversionWrapper returnWrapper = Http.http.get(resourceDefinition.buildUrl(), (header, body) -> {
            return new DataNodeReturnNoConversionWrapper(body.get(id.getPath()));
        });

        return returnWrapper.getDataNode();
    }

    @Override
    public ValuePath actualPath() {
        return new ValuePath(id.getPath());
    }

    @Override
    public TokenizedMessage describe() {
        return tokenizedMessage().classifier("value").of().value(resourceDefinition).colon().id(id.getPath());
    }

    public HttpLazyResponseValue of(String firstKey, Object firstValue, Object... restKv) {
        Map<String, Object> map = CollectionUtils.map(firstKey, firstValue, restKv);
        return of(map);
    }

    public HttpLazyResponseValue of(String param) {
        Set<String> paramNames = resourceDefinition.getParamNames();
        if (paramNames.isEmpty()) {
            throw new IllegalArgumentException("route definition must have one parameter, but definition has zero: " + resourceDefinition.getRoute());
        }

        return of(Collections.singletonMap(paramNames.iterator().next(), param));
    }

    public HttpLazyResponseValue of(Map<String, Object> routeParams) {
        Set<String> paramNames = resourceDefinition.getParamNames();
        if (paramNames.isEmpty()) {
            throw new IllegalArgumentException("no route parameter names found in the definition: " + resourceDefinition.getRoute());
        }

        if (routeParams.size() != paramNames.size() || !paramNames.containsAll(routeParams.keySet())) {
            throw new IllegalArgumentException("route parameter names mismatch with provided, expected names: " +
                    paramNames + ", given: " + routeParams.keySet());
        }

        return new HttpLazyResponseValue(resourceDefinition.withRouteParams(routeParams), id);
    }
}
