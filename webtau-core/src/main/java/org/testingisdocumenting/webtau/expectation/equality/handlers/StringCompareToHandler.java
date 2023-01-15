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

package org.testingisdocumenting.webtau.expectation.equality.handlers;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToHandler;

import java.util.ArrayList;
import java.util.List;

import static org.testingisdocumenting.webtau.expectation.equality.handlers.HandlerMessages.*;
import static org.testingisdocumenting.webtau.utils.TraceUtils.*;

public class StringCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return (actual instanceof Character || actual instanceof CharSequence) &&
                (expected instanceof Character || expected instanceof CharSequence);
    }

    @Override
    public void compareEqualOnly(CompareToComparator compareToComparator, ValuePath actualPath, Object actual, Object expected) {
        Comparator comparator = new Comparator(compareToComparator, actualPath, actual, expected);
        comparator.compare();
    }

    private class Comparator {
        private final Object actual;
        private final Object expected;
        private final CompareToComparator compareToComparator;
        private final ValuePath actualPath;
        private String actualString;
        private String expectedString;

        private final boolean actualHasSlashR;
        private final boolean expectedHasSlashR;

        private final String[] actualLines;
        private final String[] expectedLines;

        private final String longestLineUnderscore;

        private final List<String> mismatchDetails;

        Comparator(CompareToComparator compareToComparator, ValuePath actualPath,
                   Object actual, Object expected) {
            this.compareToComparator = compareToComparator;
            this.actualPath = actualPath;
            this.actual = actual;
            this.expected = expected;

            this.actualString = actual.toString();
            this.expectedString = expected.toString();

            this.actualHasSlashR = actualString.contains("\r");
            this.expectedHasSlashR = expectedString.contains("\r");

            this.expectedString = this.expectedString.replaceAll("\r", "");
            this.actualString = this.actualString.replaceAll("\r", "");

            this.actualLines = actualString.split("\n", -1);
            this.expectedLines = expectedString.split("\n", -1);

            this.longestLineUnderscore = createLongestLineUnderscore();

            this.mismatchDetails = new ArrayList<>();
        }

        void compare() {
            compareNumberOfLines();
            compareEndOfLineChars();
            compareContent();

            boolean isEqual = mismatchDetails.isEmpty();
            compareToComparator.reportEqualOrNotEqual(StringCompareToHandler.this, isEqual, actualPath,
                    isEqual ? matchFullMessage() : mismatchFullMessage());
        }

        private void compareContent() {
            if (!actualString.equals(expectedString)) {
                report(renderActualExpected());
                report(renderFirstLineMismatch());
            }
        }

        private String matchFullMessage() {
            return renderActualExpected();
        }

        private String mismatchFullMessage() {
            return String.join("\n", mismatchDetails);
        }

        private void compareNumberOfLines() {
            if (actualLines.length != expectedLines.length) {
                report("different number of lines" +
                        ", expected: " + expectedLines.length +
                        ", actual: " + actualLines.length);
            }
        }

        private void compareEndOfLineChars() {
            if (actualHasSlashR && !expectedHasSlashR) {
                report("different line endings, expected doesn't contain \\r, but actual does");
            } else if (!actualHasSlashR && expectedHasSlashR) {
                report("different line endings, expected contains \\r, but actual doesn't");
            }
        }

        private String renderActualExpected() {
            if (actualLines.length == 1 && expectedLines.length == 1) {
                int indexOfFirstMismatch = indexOfFirstMismatch(actualString, expectedString);

                int assertionModePaddingSize = assertionModePaddingSize();
                return ACTUAL_PREFIX + renderValueAndTypeWithPadding(
                        assertionModePaddingSize,
                        actualString) +
                        additionalTypeInfo(actual, actualString) + "\n" +
                        expected(compareToComparator.getAssertionMode(), renderValueAndType(expectedString) +
                                additionalTypeInfo(expected, expectedString)) +
                        renderCaretIfRequired(ACTUAL_PREFIX, true, indexOfFirstMismatch + assertionModePaddingSize);
            } else {
                return ACTUAL_PREFIX + renderType(actualString) + additionalTypeInfo(actual, actualString) + "\n" +
                        renderMultilineString(actualString) + "\n" +
                        expected(compareToComparator.getAssertionMode(), renderType(expectedString) +
                                additionalTypeInfo(expected, expectedString)) +
                        renderMultilineString(expectedString);
            }
        }

        // we need to pad actual string to match number of spaces from expected assertionMode rendered
        // to make caret that shows first mismatch aligned
        //   actual:     "hello"
        // expected: not "help"
        //                   ^
        private int assertionModePaddingSize() {
            int modeLength = compareToComparator.getAssertionMode().getMessage().length();
            return modeLength > 0 ? modeLength + 1 /* space */: 0;
        }

        private String renderFirstLineMismatch() {
            int minLines = Math.min(expectedLines.length, actualLines.length);
            if (minLines == 1) {
                return "";
            }

            for (int idx = 0; idx < minLines; idx++) {
                String actualLine = actualLines[idx];
                String expectedLine = expectedLines[idx];
                if (!actualLine.equals(expectedLine)) {
                    return "first mismatch at line idx " + idx + ":\n" +
                            ACTUAL_PREFIX + actualLine + "\n" +
                            EXPECTED_PREFIX + expectedLine + "\n" +
                            renderCaretIfRequired(ACTUAL_PREFIX, false,
                                    indexOfFirstMismatch(actualLine, expectedLine));
                }
            }

            return "";
        }

        private String additionalTypeInfo(Object original, Object converted) {
            return (original.getClass() != converted.getClass() ? "(before conversion: " + renderValueAndType(original) + ")" : "");
        }


        private String renderMultilineString(String text) {
            return longestLineUnderscore + "\n" + text + "\n" + longestLineUnderscore + "\n";
        }

        private void report(String message) {
            mismatchDetails.add(message);
        }

        private String renderCaretIfRequired(String prefixAdjustment, boolean adjustForQuote, int idx) {
            return idx != -1 ? String.format("%" + (idx + (adjustForQuote ? 1 : 0) + 1 /*for 0-based index*/ +
                    prefixAdjustment.length()) + "s", "^") : "";
        }

        private String createLongestLineUnderscore() {
            int maxLength = 0;
            for (String actualLine : actualLines) {
                maxLength = Math.max(maxLength, actualLine.length());
            }

            for (String expectedLine : expectedLines) {
                maxLength = Math.max(maxLength, expectedLine.length());
            }

            StringBuilder result = new StringBuilder();
            for (int idx = 0; idx < maxLength; idx++) {
                result.append('_');
            }

            return result.toString();
        }

        private int indexOfFirstMismatch(String left, String right) {
            int minLength = Math.min(left.length(), right.length());
            for (int idx = 0; idx < minLength; idx++) {
                if (left.charAt(idx) != right.charAt(idx)) {
                    return idx;
                }
            }

            if (left.length() != right.length()) {
                return minLength;
            }

            return -1;
        }
    }
}
