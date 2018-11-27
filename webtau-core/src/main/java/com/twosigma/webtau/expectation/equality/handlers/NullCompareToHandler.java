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

import static com.twosigma.webtau.expectation.equality.handlers.HandlerMessages.expected;
import static com.twosigma.webtau.utils.TraceUtils.renderValueAndType;

public class NullCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual == null || expected == null;
    }

    @Override
    public boolean handleNulls() {
        return true;
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        if (actual == null && expected == null) {
            comparator.reportEqual(this, actualPath,
                    "  actual: null\n" + expected(comparator.getAssertionMode(), null));
        } else if (actual == null) {
            comparator.reportNotEqual(this, actualPath,
                    "  actual: null\n" + expected(comparator.getAssertionMode(), renderValueAndType(expected)));
        } else {
            comparator.reportNotEqual(this, actualPath,
                    "  actual: " + renderValueAndType(actual) + "\n" + expected(comparator.getAssertionMode(), null));
        }
    }
}
