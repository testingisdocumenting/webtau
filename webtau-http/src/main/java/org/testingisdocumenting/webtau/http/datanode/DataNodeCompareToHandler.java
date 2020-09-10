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

package org.testingisdocumenting.webtau.http.datanode;

import org.testingisdocumenting.webtau.data.traceable.CheckLevel;
import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;
import org.testingisdocumenting.webtau.expectation.equality.CompareToResult;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import static org.testingisdocumenting.webtau.expectation.equality.CheckLevelDerivation.determineCompareToCheckLevel;
import static org.testingisdocumenting.webtau.expectation.equality.CheckLevelDerivation.determineEqualOnlyCheckLevel;

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
        DataNode actualDataNode = (DataNode) actual;
        CompareToResult result;
        if (expected instanceof Map) {
            result = compareWithMap(comparator, actualPath, actualDataNode, (Map) expected);
        } else {
            Object extractedActual = extractActual(actualDataNode);
            result = comparator.compareUsingEqualOnly(actualPath, extractedActual, expected);
        }

        if (actualDataNode.isNull() || actualDataNode.get() == null) {
            CheckLevel checkLevel = determineEqualOnlyCheckLevel(result, comparator.getAssertionMode());
            updateCheckLevels(actualDataNode, expected, checkLevel);
        }
    }

    @Override
    public void compareGreaterLessEqual(CompareToComparator compareToComparator, ActualPath actualPath, Object actual, Object expected) {
        DataNode actualDataNode = (DataNode) actual;
        CompareToResult result = compareToComparator.compareUsingCompareTo(actualPath, actualDataNode.getTraceableValue(), expected);

        if (actualDataNode.isNull() || actualDataNode.get() == null) {
            CheckLevel checkLevel = determineCompareToCheckLevel(result, compareToComparator.getAssertionMode());
            updateCheckLevels(actualDataNode, expected, checkLevel);
        }
    }

    private CompareToResult compareWithMap(CompareToComparator comparator, ActualPath actualPath, DataNode actual, Map expected) {
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

        return comparator.createCompareToResult();
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

    @SuppressWarnings("unchecked")
    private void updateCheckLevels(DataNode actual, Object expected, CheckLevel checkLevel) {
        if (expected instanceof Map) {
            ((Map<String, ?>) expected).entrySet().forEach(entry -> {
                String key = entry.getKey();
                actual.get(key).getTraceableValue().updateCheckLevel(checkLevel);
                updateCheckLevels(actual.get(key), entry.getValue(), checkLevel);
            });
        } else if (expected instanceof List) {
            List expectedList = (List) expected;
            IntStream.range(0, expectedList.size()).forEach(i -> {
                actual.get(i).getTraceableValue().updateCheckLevel(checkLevel);
                updateCheckLevels(actual.get(i), expectedList.get(i), checkLevel);
            });
        }
    }
}
