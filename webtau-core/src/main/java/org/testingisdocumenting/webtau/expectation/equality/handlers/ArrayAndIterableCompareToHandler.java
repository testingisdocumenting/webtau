/*
 * Copyright 2020 webtau maintainers
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

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;
import org.testingisdocumenting.webtau.utils.CollectionUtils;

import java.util.List;

public class ArrayAndIterableCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual.getClass().isArray() && expected instanceof Iterable;
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ValuePath actualPath, Object actual, Object expected) {
        List<?> actualList = CollectionUtils.convertArrayToList(actual);
        comparator.compareUsingEqualOnly(actualPath, actualList, expected);
    }
}
