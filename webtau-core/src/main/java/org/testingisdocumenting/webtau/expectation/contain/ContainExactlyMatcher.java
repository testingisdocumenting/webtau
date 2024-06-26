/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.contain;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.ValuePathWithValue;
import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.data.render.PrettyPrintable;
import org.testingisdocumenting.webtau.data.render.PrettyPrinter;
import org.testingisdocumenting.webtau.expectation.ExpectedValuesAware;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.expectation.equality.*;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.expectation.TokenizedReportUtils.*;

public class ContainExactlyMatcher implements ValueMatcher, ExpectedValuesAware, PrettyPrintable {
    private final Iterable<Object> expectedList;
    private List<ValuePathWithValue<Object>> actualCopy;
    private List<ValuePathWithValue<Object>> expectedCopy;

    private final Map<ValuePath, List<ValuePathLazyMessageList>> notEqualMessagesByExpectedPath = new HashMap<>();
    private final ValuePathLazyMessageList notEqualCandidateMessages = new ValuePathLazyMessageList();
    private final ValuePathLazyMessageList missingMessages = new ValuePathLazyMessageList();
    private final ValuePathLazyMessageList extraMessages = new ValuePathLazyMessageList();

    private CompareToComparator comparator;

    public ContainExactlyMatcher(Iterable<Object> expected) {
        expectedList = expected;
    }

    @Override
    public ValueConverter valueConverter() {
        return comparator == null ? ValueConverter.EMPTY : comparator.createValueConverter();
    }

    @Override
    public Stream<Object> expectedValues() {
        return StreamSupport.stream(expectedList.spliterator(), false);
    }

    @Override
    public Set<ValuePath> mismatchedPaths() {
        Set<ValuePath> potentialPaths = Stream.concat(missingMessages.stream().map(ValuePathMessage::actualPath),
                        Stream.concat(extraMessages.stream().map(ValuePathMessage::actualPath),
                                notEqualCandidateMessages.stream().map(ValuePathMessage::actualPath)))
                .collect(Collectors.toSet());
        return potentialPaths.isEmpty() ?
                actualCopy.stream().map(ValuePathWithValue::getPath).collect(Collectors.toSet()) :
                potentialPaths;
    }

    @Override
    public TokenizedMessage matchingTokenizedMessage(ValuePath actualPath, Object actual) {
        comparator = CompareToComparator.comparator(CompareToComparator.AssertionMode.EQUAL);
        return tokenizedMessage().matcher("to contain exactly").valueFirstLinesOnly(expectedList);
    }

    @Override
    public TokenizedMessage matchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("contains exactly").valueFirstLinesOnly(expectedList);
    }

    @Override
    public TokenizedMessage mismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        TokenizedMessage messageTokens = tokenizedMessage();
        if (!expectedCopy.isEmpty()) {
            messageTokens = messageTokens.error("no matches found for").colon().value(
                    expectedCopy.stream().map(ValuePathWithValue::getValue).toList());
        }

        if (!actualCopy.isEmpty() && notEqualCandidateMessages.isEmpty()) {
            if (!messageTokens.isEmpty()) {
                messageTokens = messageTokens.newLine();
            }
            messageTokens = messageTokens.error("unexpected elements").colon().value(
                    actualCopy.stream().map(ValuePathWithValue::getValue).toList());
        }

        if (!notEqualCandidateMessages.isEmpty() || !missingMessages.isEmpty() || !extraMessages.isEmpty()) {
            messageTokens = messageTokens.newLine().add(generatePossibleMismatchesReport(actualPath));
        }

        return messageTokens;
    }

    @Override
    public boolean matches(ValuePath actualPath, Object actualIterable) {
        boolean result = matches(comparator, actualPath, actualIterable, true);
        notEqualCandidateMessages.addAll(extractPotentialNotEqualMessages());
        return result;
    }

    @Override
    public Set<ValuePath> matchedPaths() {
        return comparator.generateEqualMatchPaths();
    }

    @Override
    public TokenizedMessage negativeMatchingTokenizedMessage(ValuePath actualPath, Object actual) {
        comparator = CompareToComparator.comparator(CompareToComparator.AssertionMode.NOT_EQUAL);
        return tokenizedMessage().matcher("to not contain exactly").valueFirstLinesOnly(expectedList);
    }

    @Override
    public TokenizedMessage negativeMatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("not contains exactly").valueFirstLinesOnly(expectedList);
    }

    @Override
    public TokenizedMessage negativeMismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("contains exactly").value(expectedList);
    }

    @Override
    public boolean negativeMatches(ValuePath actualPath, Object actualIterable) {
        return !matches(comparator, actualPath, actualIterable, false);
    }

    @Override
    public void prettyPrint(PrettyPrinter printer) {
        printer.printDelimiter("<");
        printer.print(PrettyPrinter.CLASSIFIER_COLOR, "containExactly ");
        printer.printObject(expectedList);
        printer.printDelimiter(">");
    }

    @Override
    public String toString() {
        return PrettyPrinter.renderAsTextWithoutColors(this);
    }

    @SuppressWarnings("unchecked")
    private boolean matches(CompareToComparator comparator, ValuePath actualPath, Object actualIterable, boolean collectSuspects) {
        if (!(actualIterable instanceof Iterable)) {
            return false;
        }

        actualCopy = ValuePathWithValue.listFromIterable(actualPath, ((Iterable<Object>) actualIterable));
        expectedCopy = ValuePathWithValue.listFromIterable(actualPath, expectedList);

        CompareToHandler compareToHandlerPrevious = null;
        CompareToHandler compareToHandlerToUse = null;

        Iterator<ValuePathWithValue<Object>> expectedIt = expectedCopy.iterator();
        int notFoundCount = 0;
        while (expectedIt.hasNext() && notFoundCount < 10) {
            ValuePathWithValue<Object> expected = expectedIt.next();
            Iterator<ValuePathWithValue<Object>> actualIt = actualCopy.iterator();

            // collect mismatches for each remaining actual value
            // find elements with the largest number of mismatches
            // remember those elements as suspects per expected value
            List<CompareToResult> compareToResults = new ArrayList<>();
            boolean found = false;
            while (actualIt.hasNext()) {
                ValuePathWithValue<Object> actual = actualIt.next();

                // cache compare to handler to use
                if (actual.getValue() != null && expected.getValue() != null) {
                    if (compareToHandlerPrevious == null) {
                        compareToHandlerPrevious = CompareToComparator.findCompareToEqualHandler(actual.getValue(), expected.getValue());
                    }

                    compareToHandlerToUse = compareToHandlerPrevious;
                }

                CompareToResult compareToResult = compareToHandlerToUse != null ?
                        comparator.compareUsingEqualOnly(compareToHandlerToUse, actual.getPath(), actual.getValue(), expected.getValue()):
                        comparator.compareUsingEqualOnly(actual.getPath(), actual.getValue(), expected.getValue());

                if (compareToResult.isEqual()) {
                    actualIt.remove();
                    expectedIt.remove();
                    found = true;
                    break;
                }

                compareToResults.add(compareToResult);
            }

            if (!found && collectSuspects) {
                notFoundCount++;
                notEqualMessagesByExpectedPath.put(expected.getPath(),
                        compareToResults.stream().map(CompareToResult::getNotEqualMessages).toList());

                compareToResults.forEach(r -> missingMessages.addAll(r.getMissingMessages()));
                compareToResults.forEach(r -> extraMessages.addAll(r.getExtraMessages()));
            }
        }

        return actualCopy.isEmpty() && expectedCopy.isEmpty();
    }

    private ValuePathLazyMessageList extractPotentialNotEqualMessages() {
        List<ValuePath> actualPaths = actualCopy.stream().map(ValuePathWithValue::getPath).toList();
        ValuePathLazyMessageList notEqualCandidateMessages = new ValuePathLazyMessageList();
        int idx = 0;
        for (ValuePathWithValue<Object> expectedWithPath : expectedCopy) {
            idx++;
            if (idx > 10) {
                break;
            }

            List<ValuePathLazyMessageList> notEqualMessageBatches = notEqualMessagesByExpectedPath.get(expectedWithPath.getPath());
            if (notEqualMessageBatches == null) {
                continue;
            }

            // remove all the messages that were matched against eventually matched actual values
            notEqualMessageBatches = notEqualMessageBatches.stream()
                    .filter(batch -> {
                        if (batch.isEmpty()) {
                            return false;
                        }

                        ValuePathMessage firstMessage = batch.first();
                        return actualPaths.stream().anyMatch(path -> firstMessage.actualPath().startsWith(path));
                    })
                    .toList();


            // need to find a subset that has the least amount of mismatches
            // it will be a potential mismatch detail to display,
            //
            int minNumberOMismatches = notEqualMessageBatches.stream()
                    .map(ValuePathLazyMessageList::size)
                    .min(Integer::compareTo).orElse(0);

            List<ValuePathLazyMessageList> messagesWithMinFailures = notEqualMessageBatches.stream()
                    .filter(v -> v.size() == minNumberOMismatches).toList();

            if (messagesWithMinFailures.size() == 1 || notEqualMessageBatches.size() != messagesWithMinFailures.size()) {
                messagesWithMinFailures.forEach(notEqualCandidateMessages::addAll);
            }
        }

        return notEqualCandidateMessages;
    }

    private TokenizedMessage generatePossibleMismatchesReport(ValuePath topLevelActualPath) {
        return combineReportParts(
                generateReportPart(topLevelActualPath, tokenizedMessage().error("possible mismatches"),
                        Collections.singletonList(notEqualCandidateMessages)),
                generateReportPart(topLevelActualPath, tokenizedMessage().error("missing values"),
                        Collections.singletonList(missingMessages)),
                generateReportPart(topLevelActualPath, tokenizedMessage().error("extra values"),
                        Collections.singletonList(extraMessages)));
    }
}
