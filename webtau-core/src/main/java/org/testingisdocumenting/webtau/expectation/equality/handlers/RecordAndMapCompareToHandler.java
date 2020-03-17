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

package org.testingisdocumenting.webtau.expectation.equality.handlers;

import org.testingisdocumenting.webtau.data.table.Record;
import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;

import java.util.Map;

public class RecordAndMapCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual instanceof Record && mapWithStringKeys(expected);
    }

    private boolean mapWithStringKeys(Object expected) {
        return expected instanceof Map &&
                ((Map<?, ?>) expected).keySet().stream().allMatch(k -> k instanceof String);
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        Record actualRecord = (Record) actual;
        Map expectedMap = (Map) expected;

        for (Object key : expectedMap.keySet()) {
            String name = key.toString();
            ActualPath propertyPath = actualPath.property(name);

            if (actualRecord.getHeader().has(name)) {
                Object actualValue = actualRecord.get(name);
                Object expectedValue = expectedMap.get(name);
                comparator.compareUsingEqualOnly(propertyPath, actualValue, expectedValue);
            } else {
                comparator.reportMissing(this, propertyPath, expectedMap.get(name));
            }
        }
    }
}
