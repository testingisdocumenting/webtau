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

package com.twosigma.webtau.browser.expectation;

import com.twosigma.webtau.browser.page.ElementValue;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.CompareToComparator;
import com.twosigma.webtau.expectation.equality.CompareToHandler;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

import static com.twosigma.webtau.Ddjt.createActualPath;

public class ElementValueCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual instanceof ElementValue;
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        ElementValue actualElementValue = (ElementValue) actual;
        Object actualValue = extractActualValue(actualElementValue, expected);

        comparator.compareUsingEqualOnly(createActualPath(actualElementValue.getName()), actualValue, expected);
    }

    private Object extractActualValue(ElementValue actualElementValue, Object expected) {
        if (expected instanceof Number) {
            return convertToNumber(actualElementValue.get().toString());
        }

        return actualElementValue.get();
    }

    private Object convertToNumber(String text) {
        try {
            return NumberFormat.getInstance().parse(text);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
