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

package com.twosigma.webtau.expectation.equality.handlers;

import java.util.Map;

import com.twosigma.webtau.data.table.Record;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;

public class RecordAndMapEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof Record && mapWithStringKeys(expected);
    }

    private boolean mapWithStringKeys(Object expected) {
        return expected instanceof Map &&
                ((Map<?, ?>) expected).keySet().stream().allMatch(k -> k instanceof String);

    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        Record actualRecord = (Record) actual;
        Map expectedMap = (Map) expected;

        for (Object key : expectedMap.keySet()) {
            String name = key.toString();
            ActualPath propertyPath = actualPath.property(name);

            if (actualRecord.getHeader().has(name)) {
                Object actualValue = actualRecord.get(name);
                Object expectedValue = expectedMap.get(name);
                equalComparator.compare(propertyPath, actualValue, expectedValue);
            } else {
                equalComparator.reportMismatch(this, actualPath, propertyPath + " is not found");
            }
        }
    }
}
