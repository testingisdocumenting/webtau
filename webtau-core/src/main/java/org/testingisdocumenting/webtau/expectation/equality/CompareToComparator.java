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
import java.util.function.Supplier;
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

    private final CompareToResult compareToResult = new CompareToResult();

    // when actual value was converted for comparison, e.g. bean to Map, it will go into this map
    private Map<ValuePath, Object> convertedActualByPath;
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

            return convertedActualByPath == null ? original :
                    convertedActualByPath.getOrDefault(path, original);
        };
    }

    public Map<ValuePath, Object> getConvertedActualByPath() {
        return convertedActualByPath == null ? Collections.emptyMap() :
                Collections.unmodifiableMap(convertedActualByPath);
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
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(compareToResult.getLessMessages(), compareToResult.getEqualMessages()));
    }

    public TokenizedMessage generateGreaterThanOrEqualMismatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(compareToResult.getLessMessages()));
    }

    public TokenizedMessage generateLessThanOrEqualMismatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(compareToResult.getGreaterMessages()));
    }

    public TokenizedMessage generateLessThanMismatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(compareToResult.getGreaterMessages(), compareToResult.getEqualMessages()));
    }

    public TokenizedMessage generateNotEqualMismatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(compareToResult.getEqualMessages()));
    }

    public TokenizedMessage generateEqualMismatchReport() {
        if (isEmpty(compareToResult.getMissingMessages()) && isEmpty(compareToResult.getExtraMessages())) {
            return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(compareToResult.getNotEqualMessages()));
        }

        return combineReportParts(
                generateReportPart(topLevelActualPath, MISMATCHES_LABEL, Collections.singletonList(compareToResult.getNotEqualMessages())),
                generateReportPart(topLevelActualPath, tokenizedMessage().error("missing, but expected values"), Collections.singletonList(compareToResult.getMissingMessages())),
                generateReportPart(topLevelActualPath, tokenizedMessage().error("unexpected values"), Collections.singletonList(compareToResult.getExtraMessages())));
    }

    public ValuePathLazyMessageList getMissingMessages() {
        return compareToResult.getMissingMessages();
    }

    public ValuePathLazyMessageList getNotEqualMessages() {
        return compareToResult.getNotEqualMessages();
    }

    public Set<ValuePath> generateEqualMismatchPaths() {
        Set<ValuePath> result = new HashSet<>();
        result.addAll(extractPaths(compareToResult.getNotEqualMessages()));
        result.addAll(extractPaths(compareToResult.getExtraMessages()));
        result.addAll(extractPaths(compareToResult.getMissingMessages()));

        return result;
    }

    public TokenizedMessage generateNotEqualMatchReport() {
        if (isEmpty(compareToResult.getMissingMessages()) && isEmpty(compareToResult.getExtraMessages())) {
            return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(compareToResult.getNotEqualMessages()));
        }

        return combineReportParts(
                generateReportPart(topLevelActualPath, MATCHES_LABEL,
                        Collections.singletonList(compareToResult.getNotEqualMessages())),
                generateReportPart(topLevelActualPath, tokenizedMessage().matcher("missing values"),
                        Collections.singletonList(compareToResult.getMissingMessages())),
                generateReportPart(topLevelActualPath, tokenizedMessage().matcher("extra values"),
                        Collections.singletonList(compareToResult.getExtraMessages())));
    }

    public TokenizedMessage generateGreaterThanMatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(compareToResult.getGreaterMessages()));
    }

    public TokenizedMessage generateGreaterThanOrEqualMatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(compareToResult.getGreaterMessages(), compareToResult.getEqualMessages()));
    }

    public TokenizedMessage generateLessThanMatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(compareToResult.getLessMessages()));
    }

    public TokenizedMessage generateLessThanOrEqualToMatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(compareToResult.getEqualMessages(), compareToResult.getLessMessages()));
    }

    public TokenizedMessage generateEqualMatchReport() {
        return generateReportPartWithoutLabel(topLevelActualPath, Stream.of(compareToResult.getEqualMessages()));
    }

    public Set<ValuePath> generateEqualMatchPaths() {
        return extractPaths(getEqualMessages());
    }

    public ValuePathLazyMessageList getEqualMessages() {
        return compareToResult.getEqualMessages();
    }

    public void resetReportData() {
        compareToResult.clear();
    }

    public void reportMissing(CompareToHandler reporter, ValuePath actualPath, Object value) {
        compareToResult.addMissingMessage(new ValuePathMessage(actualPath, () -> tokenizedMessage().value(value)));
    }

    public void reportExtra(CompareToHandler reporter, ValuePath actualPath, Object value) {
        compareToResult.addExtraMessage(new ValuePathMessage(actualPath, () -> tokenizedMessage().value(value)));
    }

    public void reportEqual(CompareToHandler reporter, ValuePath actualPath, Supplier<TokenizedMessage> message) {
        compareToResult.addEqualMessage(new ValuePathMessage(actualPath, message));
    }

    public void reportNotEqual(CompareToHandler reporter, ValuePath actualPath, Supplier<TokenizedMessage> message) {
        compareToResult.addNotEqualMessage(new ValuePathMessage(actualPath, message));
    }

    public void reportGreater(CompareToHandler reporter, ValuePath actualPath, Supplier<TokenizedMessage> message) {
        compareToResult.addGreaterMessage(new ValuePathMessage(actualPath, message));
    }

    public void reportLess(CompareToHandler reporter, ValuePath actualPath, Supplier<TokenizedMessage> message) {
        compareToResult.addLessMessage(new ValuePathMessage(actualPath, message));
    }

    public void reportEqualOrNotEqual(CompareToHandler reporter, boolean isEqual, ValuePath actualPath, Supplier<TokenizedMessage> message) {
        if (isEqual) {
            reportEqual(reporter, actualPath, message);
        } else {
            reportNotEqual(reporter, actualPath, message);
        }
    }

    public void reportCompareToValue(CompareToHandler reporter, int compareTo, ValuePath actualPath, Supplier<TokenizedMessage> message) {
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

        return comparator.compareToResult;
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

        return comparator.compareToResult;
    }

    private void recordConvertedActual(ValuePath actualPath, Object actual, Object convertedActual) {
        if (actual == convertedActual) {
            return;
        }

        if (convertedActualByPath == null) {
            convertedActualByPath = new HashMap<>();
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

    private void mergeResults(CompareToComparator comparator) {
        compareToResult.merge(comparator.compareToResult);
        if (comparator.convertedActualByPath != null) {
            if (convertedActualByPath == null) {
                convertedActualByPath = new HashMap<>();
            }

            convertedActualByPath.putAll(comparator.convertedActualByPath);
        }
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

    private Set<ValuePath> extractPaths(ValuePathLazyMessageList list) {
        return list == null ? Collections.emptySet() : list.extractPaths();
    }

    private boolean isEmpty(ValuePathLazyMessageList list) {
        return list == null || list.isEmpty();
    }

    private static RuntimeException noHandlerFound(Object actual, Object expected) {
        return new RuntimeException(
            "no compareUsingCompareTo handler found for" +
                    "\nactual: " + PrettyPrinter.renderAsTextWithoutColors(actual) + " " + TraceUtils.renderType(actual) +
                    "\nexpected: " + PrettyPrinter.renderAsTextWithoutColors(expected) + " " + TraceUtils.renderType(expected));
    }
}
