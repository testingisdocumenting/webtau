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

package com.twosigma.webtau.expectation.equality;

import com.twosigma.webtau.data.render.DataRenderers;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.equality.handlers.AnyCompareToHandler;
import com.twosigma.webtau.expectation.equality.handlers.NullCompareToHandler;
import com.twosigma.webtau.utils.ServiceUtils;
import com.twosigma.webtau.utils.TraceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class CompareToComparator {
    private static final String MISMATCHES_LABEL = "mismatches";
    private static final String MATCHES_LABEL = "matches";

    private static final List<CompareToHandler> handlers = discoverHandlers();

    private final List<ActualPathMessage> equalMessages = new ArrayList<>();
    private final List<ActualPathMessage> notEqualMessages = new ArrayList<>();
    private final List<ActualPathMessage> greaterMessages = new ArrayList<>();
    private final List<ActualPathMessage> lessMessages = new ArrayList<>();
    private final List<ActualPathMessage> missingMessages = new ArrayList<>();
    private final List<ActualPathMessage> extraMessages = new ArrayList<>();

    private final boolean isNegative;

    public static CompareToComparator comparator() {
        return new CompareToComparator(false);
    }

    public static CompareToComparator negativeComparator() {
        return new CompareToComparator(true);
    }

    /**
     * creates new instance of comparator without any collected mismatches. Preserves isNegative switch.
     * @return fresh copy of equal comparator
     */
    public CompareToComparator freshCopy() {
        return new CompareToComparator(isNegative);
    }

    private CompareToComparator(boolean isNegative) {
        this.isNegative = isNegative;
    }

    public boolean compareIsEqual(ActualPath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareUsingEqualOnly(actualPath, actual, expected);

        return compareResult.getNotEqualMessages().isEmpty() &&
                compareResult.hasNoExtraOrMissing();
    }

    public boolean compareIsGreater(ActualPath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareUsingCompareTo(actualPath, actual, expected);

        return compareResult.getLessMessages().isEmpty() &&
                compareResult.getEqualMessages().isEmpty() &&
                compareResult.hasNoExtraOrMissing();
    }

    public boolean compareIsGreaterOrEqual(ActualPath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareUsingCompareTo(actualPath, actual, expected);

        return compareResult.getLessMessages().isEmpty() &&
                compareResult.hasNoExtraOrMissing();
    }

    public boolean compareIsLess(ActualPath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareUsingCompareTo(actualPath, actual, expected);

        return compareResult.getGreaterMessages().isEmpty() &&
                compareResult.getEqualMessages().isEmpty() &&
                compareResult.hasNoExtraOrMissing();
    }

    public boolean compareIsLessOrEqual(ActualPath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareUsingCompareTo(actualPath, actual, expected);

        return compareResult.getGreaterMessages().isEmpty() &&
                compareResult.hasNoExtraOrMissing();
    }

    public CompareToResult compareUsingEqualOnly(ActualPath actualPath, Object actual, Object expected) {
        CompareToHandler handler = getCompareToEqualHandler(actual, expected);

        CompareToComparator comparator = freshCopy();
        handler.compareEqualOnly(comparator, actualPath, actual, expected);

        mergeResults(comparator);

        return createCompareToResult(comparator);
    }

    public CompareToResult compareUsingCompareTo(ActualPath actualPath, Object actual, Object expected) {
        CompareToHandler handler = getCompareToGreaterLessHandler(actual, expected);

        CompareToComparator comparator = freshCopy();
        handler.compareGreaterLessEqual(comparator, actualPath, actual, expected);

        mergeResults(comparator);

        return createCompareToResult(comparator);
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

    private CompareToHandler getCompareToEqualHandler(Object actual, Object expected) {
        return handlers.stream().
                filter(h -> h.handleEquality(actual, expected)).findFirst().
                orElseThrow(() -> noHandlerFound(actual, expected));
    }

    private CompareToHandler getCompareToGreaterLessHandler(Object actual, Object expected) {
        return handlers.stream().
                filter(h -> h.handleGreaterLessEqual(actual, expected)).findFirst().
                orElseThrow(() -> noHandlerFound(actual, expected));
    }

    /**
     * comparator operates in two mode: should and shouldNot
     * @return true if the mode is shouldNot
     */
    public boolean isNegative() {
        return isNegative;
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

    public String generateEqualMismatchReport() {
        return combineReportParts(
                generateReportPart(MISMATCHES_LABEL, Collections.singletonList(notEqualMessages)),
                generateReportPart("missing, but expected values", Collections.singletonList(missingMessages)),
                generateReportPart("unexpected values", Collections.singletonList(extraMessages)));
    }

    public String generateGreaterThanMatchReport() {
        return generateReportPart(MATCHES_LABEL, Collections.singletonList(greaterMessages));
    }

    public String generateGreaterThanOrEqualMatchReport() {
        return generateReportPart(MATCHES_LABEL, Arrays.asList(greaterMessages, equalMessages));
    }

    public String generateLessThanMatchReport() {
        return generateReportPart(MATCHES_LABEL, Collections.singletonList(lessMessages));
    }

    public String generateLessThanOrEqualToMatchReport() {
        return generateReportPart(MATCHES_LABEL, Arrays.asList(equalMessages, lessMessages));
    }

    public String generateEqualMatchReport() {
        return generateReportPart(MATCHES_LABEL, Collections.singletonList(equalMessages));
    }

    public void reportMissing(CompareToHandler reporter, ActualPath actualPath, Object value) {
        missingMessages.add(new ActualPathMessage(actualPath, DataRenderers.render(value)));
    }

    public void reportExtra(CompareToHandler reporter, ActualPath actualPath, Object value) {
        extraMessages.add(new ActualPathMessage(actualPath, DataRenderers.render(value)));
    }

    public void reportEqual(CompareToHandler reporter, ActualPath actualPath, String message) {
        equalMessages.add(new ActualPathMessage(actualPath, message));
    }

    public void reportNotEqual(CompareToHandler reporter, ActualPath actualPath, String message) {
        notEqualMessages.add(new ActualPathMessage(actualPath, message));
    }

    public void reportGreater(CompareToHandler reporter, ActualPath actualPath, String message) {
        greaterMessages.add(new ActualPathMessage(actualPath, message));
    }

    public void reportLess(CompareToHandler reporter, ActualPath actualPath, String message) {
        lessMessages.add(new ActualPathMessage(actualPath, message));
    }

    public void reportEqualOrNotEqual(CompareToHandler reporter, boolean isEqual, ActualPath actualPath, String message) {
        if (isEqual) {
            reportEqual(reporter, actualPath, message);
        } else {
            reportNotEqual(reporter, actualPath, message);
        }
    }

    public void reportCompareToValue(CompareToHandler reporter, int compareTo, ActualPath actualPath, String message) {
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

    private String generateReportPart(String label, List<List<ActualPathMessage>> messagesGroups) {
        if (messagesGroups.stream().allMatch(List::isEmpty)) {
            return "";
        }

        return Stream.concat(Stream.of(label + ":\n"),
                messagesGroups.stream()
                        .flatMap(messages -> messages.stream().map(ActualPathMessage::getFullMessage)))
                .collect(joining("\n"));
    }

    private String combineReportParts(String... parts) {
        return Arrays.stream(parts).filter(p -> !p.isEmpty()).collect(joining("\n\n"));
    }

    private static List<CompareToHandler> discoverHandlers() {
        List<CompareToHandler> result = new ArrayList<>();

        List<CompareToHandler> discovered = ServiceUtils.discover(CompareToHandler.class);

        discovered.stream().filter(CompareToHandler::handleNulls).forEach(result::add);
        result.add(new NullCompareToHandler());
        discovered.stream().filter(h -> ! h.handleNulls()).forEach(result::add);

        result.add(new AnyCompareToHandler());

        return result;
    }

    private RuntimeException noHandlerFound(Object actual, Object expected) {
        return new RuntimeException(
            "no compareUsingCompareTo handler found for\nactual: " + DataRenderers.render(actual) + " " + TraceUtils.renderType(actual) +
            "\nexpected: " + DataRenderers.render(expected) + " " + TraceUtils.renderType(expected));
    }
}
