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
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Objects;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.expectation.equality.handlers.HandlerMessages.*;

public class ByteArrayCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual.getClass().equals(byte[].class) &&
                expected.getClass().equals(byte[].class);
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ValuePath actualPath, Object actual, Object expected) {
        byte[] actualArray = (byte[]) actual;
        byte[] expectedArray = (byte[]) expected;

        if (actualArray.length != expectedArray.length) {
            comparator.reportNotEqual(this, actualPath,
                    () -> tokenizedMessage().error("binary content has different size").colon().newLine()
                            .add(ACTUAL_PREFIX).value(actualArray.length).newLine()
                            .add(HandlerMessages.EXPECTED_PREFIX).value(expectedArray.length));
        }

        int diffIdx = indexOfFirstDifference(actualArray, expectedArray);
        boolean isEqual = diffIdx == -1;
        if (isEqual) {
            comparator.reportEqual(this, actualPath,
                    () -> tokenizedMessage().value(actualArray).newLine()
                    .add(renderActualExpected(actualArray, expectedArray, 0)));
        } else {
            comparator.reportNotEqual(this, actualPath,
                    () -> tokenizedMessage().error("binary content first difference idx").colon().number(diffIdx).newLine()
                            .add(renderActualExpected(actualArray, expectedArray, diffIdx)));
        }
    }

    private int indexOfFirstDifference(byte[] actualArray, byte[] expectedArray) {
        int minLength = Math.min(actualArray.length, expectedArray.length);
        for (int idx = 0; idx < minLength; idx++) {
            if (!Objects.equals(expectedArray[idx], actualArray[idx])) {
                return idx;
            }
        }

        return -1;
    }

    private TokenizedMessage renderActualExpected(byte[] actual, byte[] expected, int startIdx) {
        return tokenizedMessage().add(ACTUAL_PREFIX).add(portionOfArrayAsHex(actual, startIdx)).newLine()
                .add(EXPECTED_PREFIX).add(portionOfArrayAsHex(expected, startIdx));
    }

    private TokenizedMessage portionOfArrayAsHex(byte[] array, int startIdx) {
        int len = Math.min(array.length - startIdx, 16);
        byte[] subArray = Arrays.copyOfRange(array, startIdx, startIdx + len);

        String possibleEllipsisPrefix = startIdx > 0 ? " ..." : "";
        String possibleEllipsisSuffix = (startIdx + len) < array.length ? "..." : "";
        return tokenizedMessage().delimiterNoAutoSpacing(possibleEllipsisPrefix).none(renderAsHex(subArray)).delimiterNoAutoSpacing(possibleEllipsisSuffix);
    }

    private String renderAsHex(byte[] content) {
        Formatter formatter = new Formatter();
        for (byte b : content) {
            formatter.format("%02X", b);
        }

        return formatter.toString();
    }
}
