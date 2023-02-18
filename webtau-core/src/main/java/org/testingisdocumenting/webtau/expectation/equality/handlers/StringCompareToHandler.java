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
import org.testingisdocumenting.webtau.reporter.MessageToken;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.ArrayList;
import java.util.List;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.expectation.equality.handlers.HandlerMessages.*;

public class StringCompareToHandler implements CompareToHandler {
    @Override
    public boolean handleEquality(Object actual, Object expected) {
        return (actual instanceof Character || actual instanceof CharSequence) &&
                (expected instanceof Character || expected instanceof CharSequence);
    }

    @Override
    public Object convertedActual(Object actual, Object expected) {
        return new MultilineString(actual);
    }

    public Object convertedExpected(Object actual, Object expected) {
        return new MultilineString(expected);
    }

    @Override
    public void compareEqualOnly(CompareToComparator compareToComparator, ValuePath actualPath, Object actual, Object expected) {
        Comparator comparator = new Comparator(compareToComparator, actualPath, (MultilineString) actual, (MultilineString) expected);
        comparator.compare();
    }

    private class Comparator {
        private final CompareToComparator compareToComparator;
        private final ValuePath actualPath;
        private final MultilineString actual;
        private final MultilineString expected;

        private final List<TokenizedMessage> mismatchDetails;

        Comparator(CompareToComparator compareToComparator, ValuePath actualPath,
                   MultilineString actual, MultilineString expected) {
            this.compareToComparator = compareToComparator;
            this.actualPath = actualPath;

            this.actual = actual;
            this.expected = expected;

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
            if (!actual.getText().equals(expected.getText())) {
                report(singleLineActualExpectedMessage());
                report(firstLineMismatchMessage());
            }
        }

        private TokenizedMessage matchFullMessage() {
            return singleLineActualExpectedMessage();
        }

        private TokenizedMessage mismatchFullMessage() {
            return TokenizedMessage.join("\n", mismatchDetails);
        }

        private void compareNumberOfLines() {
            if (actual.getNumberOfLines() != expected.getNumberOfLines()) {
                report(tokenizedMessage().error("different number of lines").comma()
                        .classifier("expected").colon().number(actual.getNumberOfLines())
                        .classifier("actual").colon().number(expected.getNumberOfLines()));
            }
        }

        private void compareEndOfLineChars() {
            TokenizedMessage message = tokenizedMessage().error("different line endings").colon().newLine();
            MessageToken slashR = TokenizedMessage.TokenTypes.DELIMITER_NO_AUTO_SPACING.token(" \\r");

            if (actual.hasSlashR() && !expected.hasSlashR()) {
                report(message.add(ACTUAL_PREFIX).error("contains").add(slashR).newLine().add(EXPECTED_PREFIX).error("doesn't contain").add(slashR));
            } else if (!actual.hasSlashR() && expected.hasSlashR()) {
                report(message.add(ACTUAL_PREFIX).error("doesn't contains").add(slashR).newLine().add(EXPECTED_PREFIX).error("contains").add(slashR));
            }
        }

        private TokenizedMessage singleLineActualExpectedMessage() {
            if (!actual.isSingleLine() || !expected.isSingleLine()) {
                return tokenizedMessage();
            }

            int indexOfFirstMismatch = indexOfFirstMismatch(actual.getText(), expected.getText());

            int assertionModePaddingSize = assertionModePaddingSize();

            TokenizedMessage withoutCaret = tokenizedMessage().add(ACTUAL_PREFIX).add(valueAndTypeWithPadding(assertionModePaddingSize, actual.getText()))
                    .add(additionalTypeInfo(actual.getOriginal(), actual.getText())).newLine()
                    .add(expectedPrefixAndAssertionMode(compareToComparator.getAssertionMode()).add(valueAndType(expected.getText())))
                    .add(additionalTypeInfo(expected.getOriginal(), expected.getText()));

            String caret = renderCaretIfRequired(indexOfFirstMismatch, assertionModePaddingSize);

            return caret.isEmpty() ?
                    withoutCaret :
                    withoutCaret.newLine().delimiterNoAutoSpacing(caret);
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

        private TokenizedMessage firstLineMismatchMessage() {
            if (expected.isSingleLine() && actual.isSingleLine()) {
                return tokenizedMessage();
            }

            int minLines = Math.min(expected.getNumberOfLines(), actual.getNumberOfLines());

            for (int idx = 0; idx < minLines; idx++) {
                String actualLine = actual.getLine(idx);
                String expectedLine = expected.getLine(idx);
                if (!actualLine.equals(expectedLine)) {
                    String caret = renderCaretIfRequired(indexOfFirstMismatch(actualLine, expectedLine), assertionModePaddingSize());

                    TokenizedMessage noCaret = tokenizedMessage().error("first mismatch at line idx").number(idx).colon().newLine()
                            .add(ACTUAL_PREFIX).value(actualLine).newLine()
                            .add(EXPECTED_PREFIX).value(expectedLine);

                    actual.setFirstFailedLineIdx(idx);

                    return caret.isEmpty() ? noCaret : noCaret.newLine().delimiterNoAutoSpacing(caret);
                }
            }

            return tokenizedMessage();
        }

        private TokenizedMessage additionalTypeInfo(Object original, Object converted) {
            return (original.getClass() != converted.getClass() ? beforeConversion(original) : tokenizedMessage());
        }

        private void report(TokenizedMessage message) {
            if (!message.isEmpty()) {
                mismatchDetails.add(message);
            }
        }

        private String renderCaretIfRequired(int idx, int assertionModeAdj) {
            String prefixAdjustment = ACTUAL_PREFIX.toString() + " ";
            if (idx == -1) {
                return "";
            }

            return String.format("%" + (idx + 1 /* quote */ + 1 /*for 0-based index*/ + assertionModeAdj +
                    prefixAdjustment.length()) + "s", "^");
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
