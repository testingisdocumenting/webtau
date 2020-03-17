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

package com.twosigma.webtau.http.datacoverage;

import com.twosigma.webtau.data.traceable.TraceableValue;
import com.twosigma.webtau.http.datanode.DataNode;
import com.twosigma.webtau.http.datanode.DataNodeId;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class DataNodeToMapOfValuesConverter {
    private TraceableValueConverter traceableValueConverter;

    public DataNodeToMapOfValuesConverter(TraceableValueConverter traceableValueConverter) {
        this.traceableValueConverter = traceableValueConverter;
    }

    public Object convert(DataNode n) {
        if (n.isList()) {
            return convertToList(n);
        } else if (n.isSingleValue()) {
            return convertSingleValue(n.id(), n.getTraceableValue());
        } else {
            return convertToMap(n);
        }
    }

    private Map<String, Object> convertToMap(DataNode dataNode) {
        Map<String, Object> converted = new LinkedHashMap<>();
        dataNode.asMap().forEach((k, v) -> converted.put(k, convert(v)));

        return converted;
    }

    private List<Object> convertToList(DataNode dataNode) {
        return dataNode.elements().stream()
                .map(this::convert)
                .collect(toList());
    }

    private Object convertSingleValue(DataNodeId id, TraceableValue value) {
        return traceableValueConverter.convert(id, value);
    }
}
