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

import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.utils.StringUtils;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class HandlerMessages {
    static final TokenizedMessage ACTUAL_PREFIX = tokenizedMessage().delimiterNoAutoSpacing("  ").classifier("actual").colon();
    static final TokenizedMessage EXPECTED_PREFIX = tokenizedMessage().classifier("expected").colon();

    public static TokenizedMessage renderActualExpected(CompareToComparator.AssertionMode assertionMode, Object actual, Object expected) {
        return tokenizedMessage().add(ACTUAL_PREFIX).add(valueAndType(actual)).newLine()
                .add(expectedPrefixAndAssertionMode(assertionMode)).add(valueAndType(expected));
    }

    public static TokenizedMessage renderActualExpectedWithConversionInfo(CompareToComparator.AssertionMode assertionMode,
                                                                   Object convertedActual, Object convertedExpected,
                                                                   Object actual, Object expected) {
        return tokenizedMessage().add(ACTUAL_PREFIX).add(valueAndType(convertedActual)).add(beforeConversion(actual)).newLine()
                .add(expectedPrefixAndValueWithAssertionMode(EXPECTED_PREFIX, assertionMode, expected))
                .add(type(convertedExpected)).add(beforeConversion(expected));
    }

    static TokenizedMessage beforeConversion(Object value) {
        return tokenizedMessage().delimiterNoAutoSpacing(" (").classifier("before conversion").colon()
                .add(valueAndType(value)).delimiterNoAutoSpacing(")");
    }

    static TokenizedMessage valueAndType(Object value) {
        return tokenizedMessage().valueFirstLinesOnly(value).delimiterNoAutoSpacing(" ").add(type(value));
    }

    static TokenizedMessage type(Object value) {
        return tokenizedMessage().objectType("<" + (value == null ? "null" : value.getClass().getCanonicalName()) + ">");
    }

    static TokenizedMessage expectedPrefixAndAssertionMode(CompareToComparator.AssertionMode assertionMode) {
        TokenizedMessage result = tokenizedMessage().add(EXPECTED_PREFIX);
        String assertionModeMessage = assertionMode.getMessage();

        return assertionModeMessage.isEmpty() ? result : result.classifier(assertionModeMessage);
    }

    static TokenizedMessage expectedPrefixAndValueWithAssertionMode(TokenizedMessage prefix, CompareToComparator.AssertionMode assertionMode, Object expected) {
        String assertionMessage = assertionMode.getMessage();
        return assertionMessage.isEmpty() ?
                tokenizedMessage().add(prefix).value(expected):
                tokenizedMessage().add(prefix).classifier(assertionMessage).value(expected);
    }

    static TokenizedMessage valueAndTypeWithPadding(int padLeft, Object v) {
        return tokenizedMessage().delimiter(StringUtils.createIndentation(padLeft)).add(valueAndType(v));
    }
}
