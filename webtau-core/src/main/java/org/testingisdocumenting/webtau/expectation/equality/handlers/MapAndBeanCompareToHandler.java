/*
 * Copyright 2022 webtau maintainers
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

import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;
import org.testingisdocumenting.webtau.utils.JavaBeanUtils;

import java.util.Map;

public class MapAndBeanCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return isMapOfProps(expected) && isBean(actual); // only handles equality if actual is a java bean and expected is a map
    }

    @SuppressWarnings("unchecked")
    private boolean isMapOfProps(Object o) {
        if (!(o instanceof Map)) {
            return false;
        }

        return ((Map<?, Object>) o).keySet().stream().allMatch(k -> k instanceof String); // making sure all the keys are strings
    }

    private boolean isBean(Object o) {
        return !(o instanceof Iterable || o instanceof Map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void compareEqualOnly(CompareToComparator comparator,
                                 ActualPath actualPath, Object actual,
                                 Object expected) {
        Map<String, ?> expectedMap = (Map<String, ?>) expected;
        Map<String, ?> actualAsMap = JavaBeanUtils.convertBeanToMap(actual);

        expectedMap.keySet().forEach(p -> { // going only through expected keys, ignoring all other bean properties
            ActualPath propertyPath = actualPath.property(p);

            if (actualAsMap.containsKey(p)) {
                // use provided comparator to delegate comparison of properties
                comparator.compareUsingEqualOnly(propertyPath, actualAsMap.get(p), expectedMap.get(p));
            } else {
                // report missing properties
                comparator.reportMissing(this, propertyPath, expectedMap.get(p));
            }
        });
    }
}
