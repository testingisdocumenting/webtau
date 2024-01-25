/*
 * Copyright 2023 webtau maintainers
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

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator.AssertionMode;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;
import org.testingisdocumenting.webtau.reporter.MessageToken;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.function.Supplier;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.expectation.equality.handlers.HandlerMessages.*;

public class NullCompareToHandler implements CompareToHandler {
    private final MessageToken NULL = TokenizedMessage.TokenTypes.CLASSIFIER.token("null");
    private final MessageToken NULL_RED = TokenizedMessage.TokenTypes.ERROR.token("null");

    private final TokenizedMessage ACTUAL_NULL_PREFIX = tokenizedMessage().add(ACTUAL_PREFIX).add(NULL);
    private final TokenizedMessage ACTUAL_NULL_RED_PREFIX = tokenizedMessage().add(ACTUAL_PREFIX).add(NULL_RED);

    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return eitherIsNull(actual, expected);
    }

    @Override
    public boolean handleGreaterLessEqual(Object actual, Object expected) {
        return eitherIsNull(actual, expected);
    }

    @Override
    public boolean handleNulls() {
        return true;
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ValuePath actualPath, Object actual, Object expected) {
        if (actual == null && expected == null) {
            comparator.reportEqual(this, actualPath,
                    () -> tokenizedMessage().add(ACTUAL_NULL_PREFIX).newLine()
                            .add(expectedPrefixAndAssertionMode(comparator.getAssertionMode()))
                            .add(NULL));
        } else if (actual == null) {
            comparator.reportNotEqual(this, actualPath,
                    () -> tokenizedMessage().add(ACTUAL_NULL_RED_PREFIX).newLine()
                            .add(expectedPrefixAndAssertionMode(comparator.getAssertionMode())).add(valueAndType(expected)));
        } else {
            comparator.reportNotEqual(this, actualPath,
                    () -> tokenizedMessage().add(HandlerMessages.ACTUAL_PREFIX).add(valueAndType(actual)).newLine()
                            .add(expectedPrefixAndAssertionMode(comparator.getAssertionMode()).add(NULL_RED)));
        }
    }

    @Override
    public void compareGreaterLessEqual(CompareToComparator comparator, ValuePath actualPath, Object actual, Object expected) {
        if (actual == null && expected == null && checksEquality(comparator)) {
            comparator.reportEqual(this, actualPath,
                    () -> tokenizedMessage().add(ACTUAL_NULL_PREFIX).newLine()
                            .add(expectedPrefixAndAssertionMode(comparator.getAssertionMode())).add(NULL));
        } else if (actual == null) {
            Supplier<TokenizedMessage> message = () -> tokenizedMessage().add(ACTUAL_NULL_RED_PREFIX).newLine()
                    .add(expectedPrefixAndAssertionMode(comparator.getAssertionMode()).add(valueAndType(expected)));
            generateOppositeReport(comparator, actualPath, message);
        } else {
            Supplier<TokenizedMessage> message = () -> tokenizedMessage().add(HandlerMessages.ACTUAL_PREFIX).add(valueAndType(actual)).newLine()
                    .add(expectedPrefixAndAssertionMode(comparator.getAssertionMode())).add(NULL_RED);

            generateOppositeReport(comparator, actualPath, message);
        }
    }

    private void generateOppositeReport(CompareToComparator comparator, ValuePath actualPath, Supplier<TokenizedMessage> message) {
        switch (comparator.getAssertionMode()) {
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUAL:
                comparator.reportLess(this, actualPath, message);
                break;
            case LESS_THAN:
            case LESS_THAN_OR_EQUAL:
                comparator.reportGreater(this, actualPath, message);
                break;
        }
    }

    private boolean checksEquality(CompareToComparator comparator) {
        return comparator.getAssertionMode() == AssertionMode.EQUAL ||
                comparator.getAssertionMode() == AssertionMode.NOT_EQUAL ||
                comparator.getAssertionMode() == AssertionMode.LESS_THAN_OR_EQUAL ||
                comparator.getAssertionMode() == AssertionMode.GREATER_THAN_OR_EQUAL;
    }

    private boolean eitherIsNull(Object actual, Object expected) {
        return actual == null || expected == null;
    }
}
