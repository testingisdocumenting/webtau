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

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;

import com.twosigma.webtau.data.table.TableData;
import com.twosigma.webtau.expectation.ActualPath;

public class DataNodeEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handleNulls() {
        return true;
    }

    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof DataNode;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {

        if (expected instanceof Map) {
            compareWithMap(equalComparator, actualPath, (DataNode) actual, (Map) expected);
        } else {
            Object convertedActual = convertBasedOnExpected((DataNode) actual, expected);
            equalComparator.compare(actualPath, convertedActual, expected);
        }
    }

    private void compareWithMap(EqualComparator equalComparator, ActualPath actualPath, DataNode actual, Map expected) {
        Map<String, DataNode> actualAsMap = actual.asMap();

        Set keys = expected.keySet();
        for (Object key : keys) {
            String p = (String) key;
            ActualPath propertyPath = actualPath.property(p);

            Object expectedValue = expected.get(p);
            if (! actualAsMap.containsKey(p)) {
                equalComparator.reportMissing(this, propertyPath, expectedValue);
            } else {
                equalComparator.compare(propertyPath, actualAsMap.get(p), expectedValue);
            }
        }
    }

    private Object convertBasedOnExpected(DataNode actual, Object expected) {
        if (expected instanceof List || expected instanceof TableData) {
            return actual.elements();
        }

        return actual.get();
    }
}
