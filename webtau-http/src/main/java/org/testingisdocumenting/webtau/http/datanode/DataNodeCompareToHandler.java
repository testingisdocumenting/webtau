/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.http.datanode;

import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;
import org.testingisdocumenting.webtau.http.json.JsonRequestBody;

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
            compareWithMap(comparator, actualPath, (DataNode) actual, (Map<?, ?>) expected);
        } else if (expected instanceof JsonRequestBody && ((JsonRequestBody) expected).isMap()) {
            compareWithMap(comparator, actualPath, (DataNode) actual, (Map<?, ?>) ((JsonRequestBody) expected).getOriginal());
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

    private void compareWithMap(CompareToComparator comparator, ActualPath actualPath, DataNode actual, Map<?, ?> expected) {
        Set<?> keys = expected.keySet();
        for (Object key : keys) {
            String p = (String) key;
            ActualPath propertyPath = actualPath.property(p);

            Object expectedValue = expected.get(p);
            if (!actual.has(p)) {
                comparator.reportMissing(this, propertyPath, expectedValue);
            } else {
                comparator.compareUsingEqualOnly(propertyPath, actual.get(p), expectedValue);
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

        // fallback that shouldn't match any real default handler/scenario
        // cases it covers: nodeWithChildren compared against a single value (e.g. null, string, custom class)
        return actual.children();
    }
}
