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
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.expectation.equality.CompareToResult;
import org.testingisdocumenting.webtau.expectation.equality.ValuePathMessage;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testingisdocumenting.webtau.WebTauCore.*;
import static org.testingisdocumenting.webtau.expectation.TokenizedReportUtils.*;

public class ContainExactlyMatcher implements ValueMatcher, ExpectedValuesAware, PrettyPrintable {
    private final Collection<Object> expectedList;
    private List<ValuePathWithValue<Object>> actualCopy;
    private List<ValuePathWithValue<Object>> expectedCopy;

    private final Map<ValuePath, List<List<ValuePathMessage>>> notEqualMessagesByExpectedPath = new HashMap<>();
    private final List<ValuePathMessage> notEqualCandidateMessages = new ArrayList<>();
    private final List<ValuePathMessage> missingMessages = new ArrayList<>();
    private final List<ValuePathMessage> extraMessages = new ArrayList<>();

    private CompareToComparator comparator;

    public ContainExactlyMatcher(Collection<Object> expected) {
        expectedList = expected;
    }

    @Override
    public ValueConverter valueConverter() {
        return comparator == null ? ValueConverter.EMPTY : comparator.createValueConverter();
    }

    @Override
    public Stream<Object> expectedValues() {
        return expectedList.stream();
    }

    @Override
    public Set<ValuePath> mismatchedPaths() {
        Set<ValuePath> potentialPaths = Stream.concat(missingMessages.stream().map(ValuePathMessage::getActualPath),
                        Stream.concat(extraMessages.stream().map(ValuePathMessage::getActualPath),
                                notEqualCandidateMessages.stream().map(ValuePathMessage::getActualPath)))
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

        Iterator<ValuePathWithValue<Object>> expectedIt = expectedCopy.iterator();
        while (expectedIt.hasNext()) {
            ValuePathWithValue<Object> expected = expectedIt.next();
            Iterator<ValuePathWithValue<Object>> actualIt = actualCopy.iterator();

            // collect mismatches for each remaining actual value
            // find elements with the largest number of mismatches
            // remember those elements as suspects per expected value
            List<CompareToResult> compareToResults = new ArrayList<>();
            boolean found = false;
            while (actualIt.hasNext()) {
                ValuePathWithValue<Object> actual = actualIt.next();
                CompareToResult compareToResult = comparator.compareUsingEqualOnly(actual.getPath(), actual.getValue(), expected.getValue());
                if (compareToResult.isEqual()) {
                    actualIt.remove();
                    expectedIt.remove();
                    found = true;
                    break;
                }

                compareToResults.add(compareToResult);
            }

            if (!found && collectSuspects) {
                notEqualMessagesByExpectedPath.put(expected.getPath(),
                        compareToResults.stream().map(CompareToResult::getNotEqualMessages).toList());

                compareToResults.forEach(r -> missingMessages.addAll(r.getMissingMessages()));
                compareToResults.forEach(r -> extraMessages.addAll(r.getExtraMessages()));
            }
        }

        return actualCopy.isEmpty() && expectedCopy.isEmpty();
    }

    private List<ValuePathMessage> extractPotentialNotEqualMessages() {
        List<ValuePath> actualPaths = actualCopy.stream().map(ValuePathWithValue::getPath).toList();
        List<ValuePathMessage> notEqualCandidateMessages = new ArrayList<>();
        for (ValuePathWithValue<Object> expectedWithPath : expectedCopy) {
            List<List<ValuePathMessage>> notEqualMessageBatches = notEqualMessagesByExpectedPath.get(expectedWithPath.getPath());
            if (notEqualMessageBatches == null) {
                continue;
            }

            // remove all the messages that were matched against eventually matched actual values
            notEqualMessageBatches = notEqualMessageBatches.stream()
                    .filter(batch -> {
                        if (batch.isEmpty()) {
                            return false;
                        }

                        ValuePathMessage firstMessage = batch.get(0);
                        return actualPaths.stream().anyMatch(path -> firstMessage.getActualPath().startsWith(path));
                    })
                    .toList();


            // need to find a subset that has the least amount of mismatches
            // it will be a potential mismatch detail to display,
            //
            int minNumberOMismatches = notEqualMessageBatches.stream()
                    .map(List::size)
                    .min(Integer::compareTo).orElse(0);

            List<List<ValuePathMessage>> messagesWithMinFailures = notEqualMessageBatches.stream()
                    .filter(v -> v.size() == minNumberOMismatches).toList();

            if (notEqualMessageBatches.size() != messagesWithMinFailures.size()) {
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
