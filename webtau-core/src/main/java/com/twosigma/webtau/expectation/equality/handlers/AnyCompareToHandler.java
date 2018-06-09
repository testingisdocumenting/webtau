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

import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.CompareToComparator;
import com.twosigma.webtau.expectation.equality.CompareToHandler;

import static com.twosigma.webtau.utils.TraceUtils.renderValueAndType;

public class AnyCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return true;
    }

    @Override
    public boolean handleGreaterLess(Object actual, Object expected) {
        return actual instanceof Comparable && expected instanceof Comparable;
    }

    @Override
    public void compareEqual(CompareToComparator compareToComparator, ActualPath actualPath, Object actual, Object expected) {
        boolean areEqual = actual.equals(expected);
        if (areEqual) {
            compareToComparator.reportEqual(this, actualPath, renderActualExpected(actual, expected));
        } else {
            compareToComparator.reportNotEqual(this, actualPath, renderActualExpected(actual, expected));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void compareGreaterLess(CompareToComparator compareToComparator, ActualPath actualPath, Object actual, Object expected) {
        Comparable<Object> actualComparable = (Comparable<Object>) actual;
        int compareTo = actualComparable.compareTo(expected);

        String message = renderActualExpected(actual, expected);

        if (compareTo == 0) {
            compareToComparator.reportEqual(this, actualPath, message);
        } else if (compareTo < 0) {
            compareToComparator.reportLess(this, actualPath, message);
            compareToComparator.reportNotEqual(this, actualPath, message);
        } else {
            compareToComparator.reportGreater(this, actualPath, message);
            compareToComparator.reportNotEqual(this, actualPath, message);
        }
    }

    private String renderActualExpected(Object actual, Object expected) {
        return "  actual: " + renderValueAndType(actual) + "\n" +
                "expected: " + renderValueAndType(expected);
    }
}
