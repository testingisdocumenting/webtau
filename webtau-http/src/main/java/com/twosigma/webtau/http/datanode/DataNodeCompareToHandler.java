/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.CompareToComparator;
import com.twosigma.webtau.expectation.equality.CompareToHandler;

import java.util.Map;
import java.util.Set;

public class DataNodeCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleNulls() {
        return true;
    }

    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return handles(actual);
    }

    @Override
    public boolean handleGreaterLessEqual(Object actual, Object expected) {
        return handles(actual);
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        if (expected instanceof Map) {
            compareWithMap(comparator, actualPath, (DataNode) actual, (Map) expected);
        } else {
            Object extractedActual = extractActual((DataNode) actual);
            comparator.compareUsingEqualOnly(actualPath, extractedActual, expected);
        }
    }

    @Override
    public void compareGreaterLessEqual(CompareToComparator compareToComparator, ActualPath actualPath, Object actual, Object expected) {
        DataNode actualDataNode = (DataNode) actual;
        compareToComparator.compareUsingCompareTo(actualPath, actualDataNode.getTraceableValue(), expected);
    }

    private void compareWithMap(CompareToComparator comparator, ActualPath actualPath, DataNode actual, Map expected) {
        Map<String, DataNode> actualAsMap = actual.asMap();

        Set keys = expected.keySet();
        for (Object key : keys) {
            String p = (String) key;
            ActualPath propertyPath = actualPath.property(p);

            Object expectedValue = expected.get(p);
            if (! actualAsMap.containsKey(p)) {
                comparator.reportMissing(this, propertyPath, expectedValue);
            } else {
                comparator.compareUsingEqualOnly(propertyPath, actualAsMap.get(p), expectedValue);
            }
        }
    }

    private boolean handles(Object actual) {
        return actual instanceof DataNode;
    }

    private Object extractActual(DataNode actual) {
        if (actual.isBinary()) {
            return actual.getTraceableValue();
        }

        if (actual.isSingleValue()) {
            return actual.getTraceableValue();
        }

        if (actual.isList()) {
            return actual.elements();
        }

        return actual.asMap();
    }
}
