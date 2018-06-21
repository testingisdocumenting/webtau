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

import com.twosigma.webtau.data.render.DataRenderers;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.CompareToComparator;
import com.twosigma.webtau.expectation.equality.CompareToHandler;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Objects;

public class ByteArrayCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return actual.getClass().equals(byte[].class) &&
                expected.getClass().equals(byte[].class);
    }

    @Override
    public void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected) {
        byte[] actualArray = (byte[]) actual;
        byte[] expectedArray = (byte[]) expected;

        if (actualArray.length != expectedArray.length) {
            comparator.reportNotEqual(this, actualPath,
                    "binary content has different size:\n" +
                            "  actual: " + actualArray.length + "\n" +
                            "expected: " + expectedArray.length);
        }

        int diffIdx = indexOfFirstDifference(actualArray, expectedArray);
        boolean isEqual = diffIdx == -1;
        if (isEqual) {
            comparator.reportEqual(this, actualPath, DataRenderers.render(actualArray) + "\n" +
                    renderActualExpected(actualArray, expectedArray, 0));
        } else {
            comparator.reportNotEqual(this, actualPath,
                    "binary content first difference idx: " + diffIdx + "\n" +
                            renderActualExpected(actualArray, expectedArray, diffIdx));
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

    private String renderActualExpected(byte[] actual, byte[] expected, int startIdx) {
        return "  actual: " + portionOfArrayAsHex(actual, startIdx) + "\n" +
                "expected: " + portionOfArrayAsHex(expected, startIdx);
    }

    private String portionOfArrayAsHex(byte[] array, int startIdx) {
        int len = Math.min(array.length - startIdx, 16);
        byte[] subArray = Arrays.copyOfRange(array, startIdx, startIdx + len);

        String possibleEllipsisPrefix = startIdx > 0 ? "..." : "";
        String possibleEllipsisSuffix = (startIdx + len) < array.length ? "..." : "";
        return possibleEllipsisPrefix + renderAsHex(subArray) + possibleEllipsisSuffix;
    }

    private String renderAsHex(byte[] content) {
        Formatter formatter = new Formatter();
        for (byte b : content) {
            formatter.format("%02X", b);
        }

        return formatter.toString();
    }
}
