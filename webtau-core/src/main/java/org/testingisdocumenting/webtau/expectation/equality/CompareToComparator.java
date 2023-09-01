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
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.expectation.equality.handlers.AnyCompareToHandler;
import org.testingisdocumenting.webtau.expectation.equality.handlers.NullCompareToHandler;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;
import org.testingisdocumenting.webtau.utils.TraceUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.expectation.TokenizedReportUtils.*;

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

    private final List<ValuePathMessage> equalMessages = new ArrayList<>();
    private final List<ValuePathMessage> notEqualMessages = new ArrayList<>();
    private final List<ValuePathMessage> greaterMessages = new ArrayList<>();
    private final List<ValuePathMessage> lessMessages = new ArrayList<>();
    private final List<ValuePathMessage> missingMessages = new ArrayList<>();
    private final List<ValuePathMessage> extraMessages = new ArrayList<>();

    // when actual value was converted for comparison, e.g. bean to Map, it will go into this map
    private final Map<ValuePath, Object> convertedActualByPath = new HashMap<>();
    private Object topLevelExpected;
    private Object convertedTopLevelExpected;

    private ValuePath topLevelActualPath;
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
        return (path, original) -> {
            if (original == topLevelExpected) {
                return convertedTopLevelExpected;
            }

            return convertedActualByPath.getOrDefault(path, original);
        };
    }

    public Map<ValuePath, Object> getConvertedActualByPath() {
        return Collections.unmodifiableMap(convertedActualByPath);
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
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(lessMessages, equalMessages));
    }

    public TokenizedMessage generateGreaterThanOrEqualMismatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(lessMessages));
    }

    public TokenizedMessage generateLessThanOrEqualMismatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(greaterMessages));
    }

    public TokenizedMessage generateLessThanMismatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(greaterMessages, equalMessages));
    }

    public TokenizedMessage generateNotEqualMismatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(equalMessages));
    }

    public TokenizedMessage generateEqualMismatchReport() {
        if (missingMessages.isEmpty() && extraMessages.isEmpty()) {
            return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(notEqualMessages));
        }

        return combineReportParts(
                generateReportPart(topLevelActualPath, MISMATCHES_LABEL, Collections.singletonList(notEqualMessages)),
                generateReportPart(topLevelActualPath, tokenizedMessage().error("missing, but expected values"), Collections.singletonList(missingMessages)),
                generateReportPart(topLevelActualPath, tokenizedMessage().error("unexpected values"), Collections.singletonList(extraMessages)));
    }

    public List<ValuePathMessage> getMissingMessages() {
        return missingMessages;
    }

    public List<ValuePathMessage> getNotEqualMessages() {
        return notEqualMessages;
    }

    public Set<ValuePath> generateEqualMismatchPaths() {
        Set<ValuePath> result = new HashSet<>();
        result.addAll(extractActualPaths(notEqualMessages));
        result.addAll(extractActualPaths(extraMessages));
        result.addAll(extractActualPaths(missingMessages));

        return result;
    }

    public TokenizedMessage generateNotEqualMatchReport() {
        if (missingMessages.isEmpty() && extraMessages.isEmpty()) {
            return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(notEqualMessages));
        }

        return combineReportParts(
                generateReportPart(topLevelActualPath, MATCHES_LABEL, Collections.singletonList(notEqualMessages)),
                generateReportPart(topLevelActualPath, tokenizedMessage().matcher("missing values"), Collections.singletonList(missingMessages)),
                generateReportPart(topLevelActualPath, tokenizedMessage().matcher("extra values"), Collections.singletonList(extraMessages)));
    }

    public TokenizedMessage generateGreaterThanMatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(greaterMessages));
    }

    public TokenizedMessage generateGreaterThanOrEqualMatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(greaterMessages, equalMessages));
    }

    public TokenizedMessage generateLessThanMatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(lessMessages));
    }

    public TokenizedMessage generateLessThanOrEqualToMatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(equalMessages, lessMessages));
    }

    public TokenizedMessage generateEqualMatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(equalMessages));
    }

    public Set<ValuePath> generateEqualMatchPaths() {
        return extractActualPaths(equalMessages);
    }

    public List<ValuePathMessage> getEqualMessages() {
        return equalMessages;
    }

    public void resetReportData() {
        equalMessages.clear();
        notEqualMessages.clear();
        greaterMessages.clear();
        lessMessages.clear();
        missingMessages.clear();
        extraMessages.clear();
    }

    public void reportMissing(CompareToHandler reporter, ValuePath actualPath, Object value) {
        missingMessages.add(new ValuePathMessage(actualPath, tokenizedMessage().value(value)));
    }

    public void reportExtra(CompareToHandler reporter, ValuePath actualPath, Object value) {
        extraMessages.add(new ValuePathMessage(actualPath, tokenizedMessage().value(value)));
    }

    public void reportEqual(CompareToHandler reporter, ValuePath actualPath, TokenizedMessage message) {
        equalMessages.add(new ValuePathMessage(actualPath, message));
    }

    public void reportNotEqual(CompareToHandler reporter, ValuePath actualPath, TokenizedMessage message) {
        notEqualMessages.add(new ValuePathMessage(actualPath, message));
    }

    public void reportGreater(CompareToHandler reporter, ValuePath actualPath, TokenizedMessage message) {
        greaterMessages.add(new ValuePathMessage(actualPath, message));
    }

    public void reportLess(CompareToHandler reporter, ValuePath actualPath, TokenizedMessage message) {
        lessMessages.add(new ValuePathMessage(actualPath, message));
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
        updateTopLevelActualPath(actualPath);

        Object convertedActual = handler.convertedActual(actual, expected);
        recordConvertedActual(actualPath, actual, convertedActual);

        Object convertedExpected = handler.convertedExpected(actual, expected);
        recordConvertedExpected(expected, convertedExpected);

        CompareToComparator comparator = CompareToComparator.comparator(mode);
        handler.compareEqualOnly(comparator, actualPath, convertedActual, convertedExpected);

        mergeResults(comparator);

        return createCompareToResult(comparator);
    }

    private CompareToResult compareUsingCompareTo(AssertionMode mode, ValuePath actualPath, Object actual, Object expected) {
        setAssertionMode(mode);
        updateTopLevelActualPath(actualPath);

        CompareToHandler handler = findCompareToGreaterLessHandler(actual, expected);

        Object convertedActual = handler.convertedActual(actual, expected);
        recordConvertedActual(actualPath, actual, convertedActual);

        Object convertedExpected = handler.convertedExpected(actual, expected);
        recordConvertedExpected(expected, convertedExpected);

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

    private void recordConvertedExpected(Object expected, Object convertedExpected) {
        if (topLevelExpected != null) {
            return;
        }

        topLevelExpected = expected;
        convertedTopLevelExpected = convertedExpected;
    }

    private void setAssertionMode(AssertionMode mode) {
        assertionMode = mode;
    }

    private void updateTopLevelActualPath(ValuePath actualPath) {
        if (topLevelActualPath == null) {
            topLevelActualPath = actualPath;
        }
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
                    "\nactual: " + PrettyPrinter.renderAsTextWithoutColors(actual) + " " + TraceUtils.renderType(actual) +
                    "\nexpected: " + PrettyPrinter.renderAsTextWithoutColors(expected) + " " + TraceUtils.renderType(expected));
    }

    private Set<ValuePath> extractActualPaths(List<ValuePathMessage> messages) {
        return messages
                .stream()
                .map(ValuePathMessage::getActualPath)
                .collect(Collectors.toSet());
    }
}
