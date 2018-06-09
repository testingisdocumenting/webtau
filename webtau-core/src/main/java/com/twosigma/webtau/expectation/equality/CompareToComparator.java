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
import com.twosigma.webtau.utils.ServiceUtils;
import com.twosigma.webtau.utils.TraceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class CompareToComparator {
    private static List<CompareToHandler> handlers = discoverHandlers();

    private List<ActualPathMessage> equalMessages = new ArrayList<>();
    private List<ActualPathMessage> notEqualMessages = new ArrayList<>();
    private List<ActualPathMessage> greaterMessages = new ArrayList<>();
    private List<ActualPathMessage> lessMessages = new ArrayList<>();
    private List<ActualPathMessage> missingMessages = new ArrayList<>();
    private List<ActualPathMessage> extraMessages = new ArrayList<>();

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

    private CompareToComparator(final boolean isNegative) {
        this.isNegative = isNegative;
    }

    public boolean compareEqual(ActualPath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareTo(actualPath, actual, expected);

        return compareResult.getNotEqualMessages().isEmpty() &&
                compareResult.hasNoExtraOrMissing();
    }

    public boolean compareGreater(ActualPath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareTo(actualPath, actual, expected);

        return compareResult.getLessMessages().isEmpty() &&
                compareResult.getEqualMessages().isEmpty() &&
                compareResult.hasNoExtraOrMissing();
    }

    public boolean compareGreaterOrEqual(ActualPath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareTo(actualPath, actual, expected);

        return compareResult.getLessMessages().isEmpty() &&
                compareResult.hasNoExtraOrMissing();
    }

    public boolean compareLess(ActualPath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareTo(actualPath, actual, expected);

        return compareResult.getGreaterMessages().isEmpty() &&
                compareResult.getEqualMessages().isEmpty() &&
                compareResult.hasNoExtraOrMissing();
    }

    public boolean compareLessOrEqual(ActualPath actualPath, Object actual, Object expected) {
        CompareToResult compareResult = compareTo(actualPath, actual, expected);

        return compareResult.getGreaterMessages().isEmpty() &&
                compareResult.hasNoExtraOrMissing();
    }

    public CompareToResult compareEqualOnly(ActualPath actualPath, Object actual, Object expected) {
        CompareToHandler handler = getCompareToEqualHandler(actual, expected);

        CompareToComparator comparator = freshCopy();
        handler.compareEqual(comparator, actualPath, actual, expected);

        mergeResults(comparator);

        return createCompareToResult(comparator);
    }

    public CompareToResult compareTo(ActualPath actualPath, Object actual, Object expected) {
        CompareToHandler handler = getCompareToGreaterLessHandler(actual, expected);

        CompareToComparator comparator = freshCopy();
        handler.compareGreaterLess(comparator, actualPath, actual, expected);

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
                filter(h -> h.handleGreaterLess(actual, expected)).findFirst().
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
        return generateReportPart("mismatches", Arrays.asList(lessMessages, equalMessages));
    }

    public String generateGreaterThanOrEqualMismatchReport() {
        return generateReportPart("mismatches", Collections.singletonList(lessMessages));
    }

    public String generateLessThanOrEqualMismatchReport() {
        return generateReportPart("mismatches", Collections.singletonList(greaterMessages));
    }

    public String generateLessThanMismatchReport() {
        return generateReportPart("mismatches", Arrays.asList(greaterMessages, equalMessages));
    }

    public String generateGreaterThanMatchReport() {
        return generateReportPart("matches", Collections.singletonList(greaterMessages));
    }

    public String generateGreaterThanOrEqualMatchReport() {
        return generateReportPart("matches", Arrays.asList(greaterMessages, equalMessages));
    }

    public String generateLessThanMatchReport() {
        return generateReportPart("matches", Collections.singletonList(lessMessages));
    }

    public String generateLessThanOrEqualToMatchReport() {
        return generateReportPart("matches", Arrays.asList(equalMessages, lessMessages));
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

    private String generateReportPart(String label, List<List<ActualPathMessage>> messagesGroups) {
        if (messagesGroups.stream().allMatch(List::isEmpty)) {
            return "";
        }

        return Stream.concat(Stream.of(label + ":\n"),
                messagesGroups.stream()
                        .flatMap(messages -> messages.stream().map(ActualPathMessage::getFullMessage)))
                .collect(joining("\n"));
    }

    private static List<CompareToHandler> discoverHandlers() {
        List<CompareToHandler> result = new ArrayList<>();

        List<CompareToHandler> discovered = ServiceUtils.discover(CompareToHandler.class);

        discovered.stream().filter(CompareToHandler::handleNulls).forEach(result::add);
        discovered.stream().filter(h -> ! h.handleNulls()).forEach(result::add);

        result.add(new AnyCompareToHandler());

        return result;
    }

    private RuntimeException noHandlerFound(Object actual, Object expected) {
        return new RuntimeException(
            "no compareTo handler found for\nactual: " + DataRenderers.render(actual) + " " + TraceUtils.renderType(actual) +
            "\nexpected: " + DataRenderers.render(expected) + " " + TraceUtils.renderType(expected));
    }
}
