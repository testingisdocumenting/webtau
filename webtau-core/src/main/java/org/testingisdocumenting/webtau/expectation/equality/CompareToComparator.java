/*
 * Copyright 2022 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.equality;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.data.render.DataRenderers;
import org.testingisdocumenting.webtau.expectation.equality.handlers.AnyCompareToHandler;
import org.testingisdocumenting.webtau.expectation.equality.handlers.NullCompareToHandler;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;
import org.testingisdocumenting.webtau.utils.TraceUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class CompareToComparator {
    public enum AssertionMode {
        EQUAL(""),
        NOT_EQUAL("not"),
        GREATER_THAN("greater than"),
        GREATER_THAN_OR_EQUAL("greater than or equal to"),
        LESS_THAN("less than"),
        LESS_THAN_OR_EQUAL("less than or equal to");

        private final String message;

        AssertionMode(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    private static final TokenizedMessage MISMATCHES_LABEL = tokenizedMessage().error("mismatches");
    private static final TokenizedMessage MATCHES_LABEL = tokenizedMessage().matcher("matches");

    private static final List<CompareToHandler> handlers = discoverHandlers();

    private final List<ActualPathMessage> equalMessages = new ArrayList<>();
    private final List<ActualPathMessage> notEqualMessages = new ArrayList<>();
    private final List<ActualPathMessage> greaterMessages = new ArrayList<>();
    private final List<ActualPathMessage> lessMessages = new ArrayList<>();
    private final List<ActualPathMessage> missingMessages = new ArrayList<>();
    private final List<ActualPathMessage> extraMessages = new ArrayList<>();

    // when actual value was converted for comparison, e.g. bean to Map, it will go into this map
    private final Map<ValuePath, Object> convertedActualByPath = new HashMap<>();

    private AssertionMode assertionMode;

    public static CompareToComparator comparator() {
        return new CompareToComparator(null);
    }

    public static CompareToComparator comparator(AssertionMode assertionMode) {
        return new CompareToComparator(assertionMode);
    }

    private CompareToComparator(AssertionMode assertionMode) {
        this.assertionMode = assertionMode;
    }

    public ValueConverter createValueConverter() {
        return convertedActualByPath::getOrDefault;
    }

    public boolean compareIsEqual(ValuePath actualPath, Object actual, Object expected) {
        CompareToHandler handler = findCompareToEqualHandler(actual, expected);
        return compareIsEqual(handler, actualPath, actual, expected);
    }

    public boolean compareIsEqual(CompareToHandler compareToHandler, ValuePath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareUsingEqualOnly(compareToHandler, AssertionMode.EQUAL, actualPath, actual, expected);
        return compareResult.isEqual();
    }

    public boolean compareIsNotEqual(ValuePath actualPath, Object actual, Object expected) {
        CompareToHandler handler = findCompareToEqualHandler(actual, expected);
        return compareIsNotEqual(handler, actualPath, actual, expected);
    }

    public boolean compareIsNotEqual(CompareToHandler compareToHandler, ValuePath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareUsingEqualOnly(compareToHandler, AssertionMode.NOT_EQUAL, actualPath, actual, expected);
        return compareResult.isNotEqual();
    }

    public boolean compareIsGreater(ValuePath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareUsingCompareTo(AssertionMode.GREATER_THAN, actualPath, actual, expected);
        return compareResult.isGreater();
    }

    public boolean compareIsGreaterOrEqual(ValuePath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareUsingCompareTo(AssertionMode.GREATER_THAN_OR_EQUAL, actualPath, actual, expected);
        return compareResult.isGreaterOrEqual();
    }

    public boolean compareIsLess(ValuePath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareUsingCompareTo(AssertionMode.LESS_THAN, actualPath, actual, expected);
        return compareResult.isLess();
    }

    public boolean compareIsLessOrEqual(ValuePath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareUsingCompareTo(AssertionMode.LESS_THAN_OR_EQUAL, actualPath, actual, expected);
        return compareResult.isLessOrEqual();
    }

    public CompareToResult compareUsingEqualOnly(ValuePath actualPath, Object actual, Object expected) {
        return compareUsingEqualOnly(findCompareToEqualHandler(actual, expected), assertionMode, actualPath, actual, expected);
    }

    public CompareToResult compareUsingEqualOnly(CompareToHandler compareToHandler, ValuePath actualPath, Object actual, Object expected) {
        validateAssertionModeIsPresent();
        return compareUsingEqualOnly(compareToHandler, assertionMode, actualPath, actual, expected);
    }

    public CompareToResult compareUsingCompareTo(ValuePath actualPath, Object actual, Object expected) {
        validateAssertionModeIsPresent();
        return compareUsingCompareTo(assertionMode, actualPath, actual, expected);
    }

    public AssertionMode getAssertionMode() {
        return assertionMode;
    }

    public TokenizedMessage generateGreaterThanMismatchReport() {
        return generateReportPart(MISMATCHES_LABEL, Arrays.asList(lessMessages, equalMessages));
    }

    public TokenizedMessage generateGreaterThanOrEqualMismatchReport() {
        return generateReportPart(MISMATCHES_LABEL, Collections.singletonList(lessMessages));
    }

    public TokenizedMessage generateLessThanOrEqualMismatchReport() {
        return generateReportPart(MISMATCHES_LABEL, Collections.singletonList(greaterMessages));
    }

    public TokenizedMessage generateLessThanMismatchReport() {
        return generateReportPart(MISMATCHES_LABEL, Arrays.asList(greaterMessages, equalMessages));
    }

    public TokenizedMessage generateNotEqualMismatchReport() {
        return generateReportPartWithoutLabel(Collections.singletonList(equalMessages));
    }

    public TokenizedMessage generateEqualMismatchReport() {
        if (missingMessages.isEmpty() && extraMessages.isEmpty()) {
            return generateReportPartWithoutLabel(Collections.singletonList(notEqualMessages));
        }

        return combineReportParts(
                generateReportPart(MISMATCHES_LABEL, Collections.singletonList(notEqualMessages)),
                generateReportPart(tokenizedMessage().error("missing, but expected values"), Collections.singletonList(missingMessages)),
                generateReportPart(tokenizedMessage().error("unexpected values"), Collections.singletonList(extraMessages)));
    }

    public List<ActualPathMessage> getNotEqualMessages() {
        return notEqualMessages;
    }

    public Set<ValuePath> generateEqualMismatchPaths() {
        return extractActualPaths(notEqualMessages);
    }

    public TokenizedMessage generateNotEqualMatchReport() {
        if (missingMessages.isEmpty() && extraMessages.isEmpty()) {
            return generateReportPartWithoutLabel(Collections.singletonList(notEqualMessages));
        }

        return combineReportParts(
                generateReportPart(MATCHES_LABEL, Collections.singletonList(notEqualMessages)),
                generateReportPart(tokenizedMessage().matcher("missing values"), Collections.singletonList(missingMessages)),
                generateReportPart(tokenizedMessage().matcher("extra values"), Collections.singletonList(extraMessages)));
    }

    public TokenizedMessage generateGreaterThanMatchReport() {
        return generateReportPartWithoutLabel(Collections.singletonList(greaterMessages));
    }

    public TokenizedMessage generateGreaterThanOrEqualMatchReport() {
        return generateReportPartWithoutLabel(Arrays.asList(greaterMessages, equalMessages));
    }

    public TokenizedMessage generateLessThanMatchReport() {
        return generateReportPartWithoutLabel(Collections.singletonList(lessMessages));
    }

    public TokenizedMessage generateLessThanOrEqualToMatchReport() {
        return generateReportPartWithoutLabel(Arrays.asList(equalMessages, lessMessages));
    }

    public TokenizedMessage generateEqualMatchReport() {
        return generateReportPartWithoutLabel(Collections.singletonList(equalMessages));
    }

    public Set<ValuePath> generateEqualMatchPaths() {
        return extractActualPaths(equalMessages);
    }

    @Deprecated
    public void reportEqual(CompareToHandler reporter, ValuePath actualPath, String message) {
        reportEqual(reporter, actualPath, tokenizedMessage().none(message));
    }

    @Deprecated
    public void reportNotEqual(CompareToHandler reporter, ValuePath actualPath, String message) {
        reportNotEqual(reporter, actualPath, tokenizedMessage().none(message));
    }

    @Deprecated
    public void reportGreater(CompareToHandler reporter, ValuePath actualPath, String message) {
        reportGreater(reporter, actualPath, tokenizedMessage().none(message));
    }

    @Deprecated
    public void reportLess(CompareToHandler reporter, ValuePath actualPath, String message) {
        reportLess(reporter, actualPath, tokenizedMessage().none(message));
    }

    @Deprecated
    public void reportEqualOrNotEqual(CompareToHandler reporter, boolean isEqual, ValuePath actualPath, String message) {
        reportEqualOrNotEqual(reporter, isEqual, actualPath, tokenizedMessage().none(message));
    }

    @Deprecated
    public void reportCompareToValue(CompareToHandler reporter, int compareTo, ValuePath actualPath, String message) {
        reportCompareToValue(reporter, compareTo, actualPath, tokenizedMessage().none(message));
    }

    public void reportMissing(CompareToHandler reporter, ValuePath actualPath, Object value) {
        missingMessages.add(new ActualPathMessage(actualPath, tokenizedMessage().value(value)));
    }

    public void reportExtra(CompareToHandler reporter, ValuePath actualPath, Object value) {
        extraMessages.add(new ActualPathMessage(actualPath, tokenizedMessage().value(value)));
    }

    public void reportEqual(CompareToHandler reporter, ValuePath actualPath, TokenizedMessage message) {
        equalMessages.add(new ActualPathMessage(actualPath, message));
    }

    public void reportNotEqual(CompareToHandler reporter, ValuePath actualPath, TokenizedMessage message) {
        notEqualMessages.add(new ActualPathMessage(actualPath, message));
    }

    public void reportGreater(CompareToHandler reporter, ValuePath actualPath, TokenizedMessage message) {
        greaterMessages.add(new ActualPathMessage(actualPath, message));
    }

    public void reportLess(CompareToHandler reporter, ValuePath actualPath, TokenizedMessage message) {
        lessMessages.add(new ActualPathMessage(actualPath, message));
    }

    public void reportEqualOrNotEqual(CompareToHandler reporter, boolean isEqual, ValuePath actualPath, TokenizedMessage message) {
        if (isEqual) {
            reportEqual(reporter, actualPath, message);
        } else {
            reportNotEqual(reporter, actualPath, message);
        }
    }

    public void reportCompareToValue(CompareToHandler reporter, int compareTo, ValuePath actualPath, TokenizedMessage message) {
        if (compareTo == 0) {
            reportEqual(reporter, actualPath, message);
        } else if (compareTo < 0) {
            reportLess(reporter, actualPath, message);
            reportNotEqual(reporter, actualPath, message);
        } else {
            reportGreater(reporter, actualPath, message);
            reportNotEqual(reporter, actualPath, message);
        }
    }

    public static CompareToHandler findCompareToEqualHandler(Object actual, Object expected) {
        return handlers.stream().
                filter(h -> h.handleEquality(actual, expected)).findFirst().
                orElseThrow(() -> noHandlerFound(actual, expected));
    }

    private CompareToResult compareUsingEqualOnly(CompareToHandler handler, AssertionMode mode, ValuePath actualPath, Object actual, Object expected) {
        setAssertionMode(mode);

        Object convertedActual = handler.convertedActual(actual, expected);
        recordConvertedActual(actualPath, actual, convertedActual);

        Object convertedExpected = handler.convertedExpected(actual, expected);

        CompareToComparator comparator = CompareToComparator.comparator(mode);
        handler.compareEqualOnly(comparator, actualPath, convertedActual, convertedExpected);

        mergeResults(comparator);

        return createCompareToResult(comparator);
    }

    private CompareToResult compareUsingCompareTo(AssertionMode mode, ValuePath actualPath, Object actual, Object expected) {
        setAssertionMode(mode);
        CompareToHandler handler = findCompareToGreaterLessHandler(actual, expected);

        Object convertedActual = handler.convertedActual(actual, expected);
        recordConvertedActual(actualPath, actual, convertedActual);

        Object convertedExpected = handler.convertedExpected(actual, expected);

        CompareToComparator comparator = CompareToComparator.comparator(mode);
        handler.compareGreaterLessEqual(comparator, actualPath, convertedActual, convertedExpected);

        mergeResults(comparator);

        return createCompareToResult(comparator);
    }

    private void recordConvertedActual(ValuePath actualPath, Object actual, Object convertedActual) {
        if (actual == convertedActual) {
            return;
        }

        convertedActualByPath.put(actualPath, convertedActual);
    }

    private void setAssertionMode(AssertionMode mode) {
        assertionMode = mode;
    }

    private void validateAssertionModeIsPresent() {
        if (assertionMode == null) {
            throw new IllegalStateException("assertionMode is not set");
        }
    }

    private CompareToResult createCompareToResult(CompareToComparator comparator) {
        CompareToResult result = new CompareToResult();
        result.setEqualMessages(comparator.equalMessages);
        result.setNotEqualMessages(comparator.notEqualMessages);
        result.setGreaterMessages(comparator.greaterMessages);
        result.setLessMessages(comparator.lessMessages);
        result.setMissingMessages(comparator.missingMessages);
        result.setExtraMessages(comparator.extraMessages);

        return result;
    }

    private void mergeResults(CompareToComparator comparator) {
        equalMessages.addAll(comparator.equalMessages);
        notEqualMessages.addAll(comparator.notEqualMessages);
        greaterMessages.addAll(comparator.greaterMessages);
        lessMessages.addAll(comparator.lessMessages);
        missingMessages.addAll(comparator.missingMessages);
        extraMessages.addAll(comparator.extraMessages);
        convertedActualByPath.putAll(comparator.convertedActualByPath);
    }

    private TokenizedMessage generateReportPart(TokenizedMessage label, List<List<ActualPathMessage>> messagesGroups) {
        if (messagesGroups.stream().allMatch(List::isEmpty)) {
            return tokenizedMessage();
        }

        return tokenizedMessage().add(label).colon().doubleNewLine().add(generateReportPartWithoutLabel(messagesGroups));
    }

    private TokenizedMessage generateReportPartWithoutLabel(List<List<ActualPathMessage>> messagesGroups) {
        if (messagesGroups.stream().allMatch(List::isEmpty)) {
            return tokenizedMessage();
        }

        TokenizedMessage result = tokenizedMessage();
        int groupIdx = 0;
        for (List<ActualPathMessage> group : messagesGroups) {
            for (ActualPathMessage message : group) {
                result.add(group.size() > 1 ? message.getFullMessage() : message.getMessage());
            }

            boolean isLastGroup = groupIdx == messagesGroups.size() - 1;
            if (!isLastGroup) {
                result.newLine();
            }

            groupIdx++;
        }

        return result;
    }

    private TokenizedMessage combineReportParts(TokenizedMessage... parts) {
        TokenizedMessage result = tokenizedMessage();

        List<TokenizedMessage> nonEmpty = Arrays.stream(parts)
                .filter(part -> !part.isEmpty())
                .collect(Collectors.toList());

        int idx = 0;
        for (TokenizedMessage message : nonEmpty) {
            boolean isLast = idx == nonEmpty.size() - 1;

            result.add(message);
            if (!isLast) {
                result.doubleNewLine();
            }

            idx++;
        }

        return result;
    }

    private static CompareToHandler findCompareToGreaterLessHandler(Object actual, Object expected) {
        return handlers.stream().
                filter(h -> h.handleGreaterLessEqual(actual, expected)).findFirst().
                orElseThrow(() -> noHandlerFound(actual, expected));
    }

    private static List<CompareToHandler> discoverHandlers() {
        List<CompareToHandler> result = new ArrayList<>();

        List<CompareToHandler> discovered = ServiceLoaderUtils.load(CompareToHandler.class);

        discovered.stream().filter(CompareToHandler::handleNulls).forEach(result::add);
        result.add(new NullCompareToHandler());
        discovered.stream().filter(h -> ! h.handleNulls()).forEach(result::add);

        result.add(new AnyCompareToHandler());

        return result;
    }

    private static RuntimeException noHandlerFound(Object actual, Object expected) {
        return new RuntimeException(
            "no compareUsingCompareTo handler found for" +
                    "\nactual: " + DataRenderers.render(actual) + " " + TraceUtils.renderType(actual) +
                    "\nexpected: " + DataRenderers.render(expected) + " " + TraceUtils.renderType(expected));
    }

    private Set<ValuePath> extractActualPaths(List<ActualPathMessage> notEqualMessages) {
        return notEqualMessages
                .stream()
                .map(ActualPathMessage::getActualPath)
                .collect(Collectors.toSet());
    }
}
