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

package org.testingisdocumenting.webtau.browser.expectation;

import org.testingisdocumenting.webtau.browser.page.PageElementValue;
import org.testingisdocumenting.webtau.browser.page.PageElement;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;

import java.util.List;

import static org.testingisdocumenting.webtau.WebTauCore.createActualPath;

public class PageElementCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return handles(actual);
    }

    @Override
    public boolean handleGreaterLessEqual(Object actual, Object expected) {
        return handles(actual);
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ValuePath actualPath, Object actual, Object expected) {
        PageElementValue<?> elementValue = extractElementValue(actual, expected);
        comparator.compareUsingEqualOnly(createPath(elementValue), elementValue, expected);
    }

    @Override
    public void compareGreaterLessEqual(CompareToComparator comparator, ValuePath actualPath, Object actual, Object expected) {
        PageElementValue<?> elementValue = extractElementValue(actual, expected);
        comparator.compareUsingCompareTo(createPath(elementValue), elementValue, expected);
    }

    private ValuePath createPath(PageElementValue<?> elementValue) {
        return createActualPath(elementValue.getName());
    }

    private PageElementValue<?> extractElementValue(Object actual, Object expected) {
        PageElement actualPageElement = (PageElement) actual;
        return expected instanceof List ?
                actualPageElement.valuesList :
                actualPageElement.value;
    }

    private boolean handles(Object actual) {
        return actual instanceof PageElement;
    }
}
