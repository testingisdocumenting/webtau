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

package com.twosigma.webtau.http.datanode;

import com.twosigma.webtau.data.traceable.TraceableValue;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.CompareToComparator;
import com.twosigma.webtau.expectation.equality.CompareToResult;

import java.util.List;
import java.util.Map;

import static com.twosigma.webtau.WebTauCore.createActualPath;

public interface DataNode extends DataNodeExpectations, Comparable, Iterable<DataNode> {
    DataNodeId id();

    DataNode get(String pathOrName);

    boolean has(String pathOrName);

    DataNode get(int idx);

    TraceableValue getTraceableValue();

    <E> E get();

    boolean isList();

    boolean isSingleValue();

    List<DataNode> elements();

    int numberOfChildren();

    int numberOfElements();

    Map<String, DataNode> asMap();

    default boolean isNull() {
        return false;
    }

    default boolean isBinary() {
        return getTraceableValue() != null &&
                getTraceableValue().getValue() != null &&
                getTraceableValue().getValue().getClass().equals(byte[].class);
    }

    @Override
    default ActualPath actualPath() {
        return createActualPath(id().getPath());
    }

    @Override
    default int compareTo(Object rhv) {
        CompareToComparator comparator = CompareToComparator.comparator(
                CompareToComparator.AssertionMode.GREATER_THAN);

        CompareToResult compareToResult = TraceableValue.withAlwaysFuzzyPassedChecks(
                () -> comparator.compareUsingCompareTo(actualPath(), this, rhv));

        if (compareToResult.isGreater()) {
            return 1;
        } else if (compareToResult.isLess()) {
            return -1;
        } else {
            return 0;
        }
    }
}
