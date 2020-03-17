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

package com.twosigma.webtau.browser.expectation;

import com.twosigma.webtau.browser.page.value.ElementValue;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.CompareToComparator;
import com.twosigma.webtau.expectation.equality.CompareToHandler;

import static com.twosigma.webtau.WebTauCore.createActualPath;

public class ElementValueCompareToHandler implements CompareToHandler {
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
        ElementValue actualElementValue = (ElementValue) actual;
        comparator.compareUsingEqualOnly(creataPath(actualElementValue), extractActualValue(actualElementValue), expected);
    }

    @Override
    public void compareGreaterLessEqual(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        ElementValue actualElementValue = (ElementValue) actual;
        comparator.compareUsingCompareTo(creataPath(actualElementValue), extractActualValue(actualElementValue), expected);
    }

    private Object extractActualValue(ElementValue actualElementValue) {
        return actualElementValue.get();
    }

    private ActualPath creataPath(ElementValue elementValue) {
        return createActualPath(elementValue.getName());
    }

    private boolean handles(Object actual) {
        return actual instanceof ElementValue;
    }
}
