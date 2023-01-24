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

import org.testingisdocumenting.webtau.data.render.DataRenderers;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.equality.handlers.AnyCompareToHandler;
import org.testingisdocumenting.webtau.expectation.equality.handlers.NullCompareToHandler;
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;
import org.testingisdocumenting.webtau.utils.TraceUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

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

    private static final String MISMATCHES_LABEL = "mismatches";
    private static final String MATCHES_LABEL = "matches";

    private static final List<CompareToHandler> handlers = discoverHandlers();

    private final List<ActualPathMessage> equalMessages = new ArrayList<>();
    private final List<ActualPathMessage> notEqualMessages = new ArrayList<>();
    private final List<ActualPathMessage> greaterMessages = new ArrayList<>();
    private final List<ActualPathMessage> lessMessages = new ArrayList<>();
    private final List<ActualPathMessage> missingMessages = new ArrayList<>();
    private final List<ActualPathMessage> extraMessages = new ArrayList<>();

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

    public boolean compareIsEqual(ValuePath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareUsingEqualOnly(AssertionMode.EQUAL, actualPath, actual, expected);
        return compareResult.isEqual();
    }

    public boolean compareIsNotEqual(ValuePath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareUsingEqualOnly(AssertionMode.NOT_EQUAL, actualPath, actual, expected);
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
        validateAssertionModeIsPresent();
        return compareUsingEqualOnly(assertionMode, actualPath, actual, expected);
    }

    public CompareToResult compareUsingCompareTo(ValuePath actualPath, Object actual, Object expected) {
        validateAssertionModeIsPresent();
        return compareUsingCompareTo(assertionMode, actualPath, actual, expected);
    }

    public AssertionMode getAssertionMode() {
        return assertionMode;
    }

    public String generateGreaterThanMismatchReport() {
        return generateReportPart(MISMATCHES_LABEL, Arrays.asList(lessMessages, equalMessages));
    }

    public String generateGreaterThanOrEqualMismatchReport() {
        return generateReportPart(MISMATCHES_LABEL, Collections.singletonList(lessMessages));
    }

    public String generateLessThanOrEqualMismatchReport() {
        return generateReportPart(MISMATCHES_LABEL, Collections.singletonList(greaterMessages));
    }

    public String generateLessThanMismatchReport() {
        return generateReportPart(MISMATCHES_LABEL, Arrays.asList(greaterMessages, equalMessages));
    }

    public String generateNotEqualMismatchReport() {
        return generateReportPart(MISMATCHES_LABEL, Collections.singletonList(equalMessages));
    }

    public String generateEqualMismatchReport() {
        return combineReportParts(
                generateReportPart(MISMATCHES_LABEL, Collections.singletonList(notEqualMessages)),
                generateReportPart("missing, but expected values", Collections.singletonList(missingMessages)),
                generateReportPart("unexpected values", Collections.singletonList(extraMessages)));
    }

    public Set<ValuePath> generateEqualMismatchPaths() {
        return extractActualPaths(notEqualMessages);
    }

    public String generateNotEqualMatchReport() {
        if (missingMessages.isEmpty() && extraMessages.isEmpty()) {
            return generateReportPartWithoutLabel(Collections.singletonList(notEqualMessages));
        }

        return combineReportParts(
                generateReportPart(MATCHES_LABEL, Collections.singletonList(notEqualMessages)),
                generateReportPart("missing values", Collections.singletonList(missingMessages)),
                generateReportPart("extra values", Collections.singletonList(extraMessages)));
    }

    public String generateGreaterThanMatchReport() {
        return generateReportPartWithoutLabel(Collections.singletonList(greaterMessages));
    }

    public String generateGreaterThanOrEqualMatchReport() {
        return generateReportPartWithoutLabel(Arrays.asList(greaterMessages, equalMessages));
    }

    public String generateLessThanMatchReport() {
        return generateReportPartWithoutLabel(Collections.singletonList(lessMessages));
    }

    public String generateLessThanOrEqualToMatchReport() {
        return generateReportPartWithoutLabel(Arrays.asList(equalMessages, lessMessages));
    }

    public String generateEqualMatchReport() {
        return generateReportPartWithoutLabel(Collections.singletonList(equalMessages));
    }

    public Set<ValuePath> generateEqualMatchPaths() {
        return extractActualPaths(equalMessages);
    }

    public void reportMissing(CompareToHandler reporter, ValuePath actualPath, Object value) {
        missingMessages.add(new ActualPathMessage(actualPath, DataRenderers.render(value)));
    }

    public void reportExtra(CompareToHandler reporter, ValuePath actualPath, Object value) {
        extraMessages.add(new ActualPathMessage(actualPath, DataRenderers.render(value)));
    }

    public void reportEqual(CompareToHandler reporter, ValuePath actualPath, String message) {
        equalMessages.add(new ActualPathMessage(actualPath, message));
    }

    public void reportNotEqual(CompareToHandler reporter, ValuePath actualPath, String message) {
        notEqualMessages.add(new ActualPathMessage(actualPath, message));
    }

    public void reportGreater(CompareToHandler reporter, ValuePath actualPath, String message) {
        greaterMessages.add(new ActualPathMessage(actualPath, message));
    }

    public void reportLess(CompareToHandler reporter, ValuePath actualPath, String message) {
        lessMessages.add(new ActualPathMessage(actualPath, message));
    }

    public void reportEqualOrNotEqual(CompareToHandler reporter, boolean isEqual, ValuePath actualPath, String message) {
        if (isEqual) {
            reportEqual(reporter, actualPath, message);
        } else {
            reportNotEqual(reporter, actualPath, message);
        }
    }

    public void reportCompareToValue(CompareToHandler reporter, int compareTo, ValuePath actualPath, String message) {
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

    private CompareToResult compareUsingEqualOnly(AssertionMode mode, ValuePath actualPath, Object actual, Object expected) {
        setAssertionMode(mode);
        CompareToHandler handler = findCompareToEqualHandler(actual, expected);

        CompareToComparator comparator = CompareToComparator.comparator(mode);
        handler.compareEqualOnly(comparator, actualPath, actual, expected);

        mergeResults(comparator);

        return createCompareToResult(comparator);
    }

    private CompareToResult compareUsingCompareTo(AssertionMode mode, ValuePath actualPath, Object actual, Object expected) {
        setAssertionMode(mode);
        CompareToHandler handler = findCompareToGreaterLessHandler(actual, expected);

        CompareToComparator comparator = CompareToComparator.comparator(mode);
        handler.compareGreaterLessEqual(comparator, actualPath, actual, expected);

        mergeResults(comparator);

        return createCompareToResult(comparator);
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
    }

    private String generateReportPart(String label, List<List<ActualPathMessage>> messagesGroups) {
        if (messagesGroups.stream().allMatch(List::isEmpty)) {
            return "";
        }

        return label + ":\n\n" + generateReportPartWithoutLabel(messagesGroups);
    }

    private String generateReportPartWithoutLabel(List<List<ActualPathMessage>> messagesGroups) {
        if (messagesGroups.stream().allMatch(List::isEmpty)) {
            return "";
        }

        return messagesGroups.stream()
                .flatMap(messages -> messages.stream().map(ActualPathMessage::getFullMessage))
                .collect(joining("\n"));
    }

    private String combineReportParts(String... parts) {
        return Arrays.stream(parts).filter(p -> !p.isEmpty()).collect(joining("\n\n"));
    }

    private static CompareToHandler findCompareToEqualHandler(Object actual, Object expected) {
        return handlers.stream().
                filter(h -> h.handleEquality(actual, expected)).findFirst().
                orElseThrow(() -> noHandlerFound(actual, expected));
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
