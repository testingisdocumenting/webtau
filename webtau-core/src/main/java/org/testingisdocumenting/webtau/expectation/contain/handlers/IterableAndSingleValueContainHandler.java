/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.contain.handlers;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.contain.ContainAnalyzer;
import org.testingisdocumenting.webtau.expectation.contain.ContainHandler;

import java.util.List;

public class IterableAndSingleValueContainHandler implements ContainHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof Iterable;
    }

    @Override
    public void analyzeContain(ContainAnalyzer containAnalyzer, ValuePath actualPath, Object actual, Object expected) {
        IterableContainAnalyzer analyzer = new IterableContainAnalyzer(actualPath, actual, expected, false);
        List<IndexedValue> indexedValues = analyzer.findContainingIndexedValues();

        if (indexedValues.isEmpty()) {
            containAnalyzer.reportMismatchedValue(expected);
        }

        // we want to highlight the closest matches in actual output. So among all the iterable values we pick the ones with the least mismatches
        // and assume they are the closest match
        List<CombinedMismatchAndMissing> failureMessagesPerIdx = analyzer.getMismatchAndMissing();
        int minFailures = failureMessagesPerIdx.stream().map(CombinedMismatchAndMissing::size).min(Integer::compareTo).orElse(0);

        long numberOfEntriesWithMinMismatches = failureMessagesPerIdx.stream()
                .filter(v -> v.size() == minFailures).count();

        if (numberOfEntriesWithMinMismatches != failureMessagesPerIdx.size()) {
            List<CombinedMismatchAndMissing> suspects = failureMessagesPerIdx.stream()
                    .filter(v -> v.size() == minFailures)
                    .toList();
            suspects.forEach(list -> containAnalyzer.registerExtraMismatchPaths(list.extractPaths()));
            suspects.forEach(list -> {
                containAnalyzer.reportMismatches(this, list.mismatchMessages());
                containAnalyzer.reportMissing(this, list.missingMessage());
            });
        }

        containAnalyzer.registerConvertedActualByPath(analyzer.getComparator().getConvertedActualByPath());
    }

    @Override
    public void analyzeNotContain(ContainAnalyzer containAnalyzer, ValuePath actualPath, Object actual, Object expected) {
        IterableContainAnalyzer analyzer = new IterableContainAnalyzer(actualPath, actual, expected, true);
        List<IndexedValue> indexedValues = analyzer.findContainingIndexedValues();

        if (!indexedValues.isEmpty()) {
            analyzer.getComparator().getEqualMessages().forEach(message -> containAnalyzer.reportMatch(this, message.getActualPath(), message.getMessage()));
        }

        containAnalyzer.registerConvertedActualByPath(analyzer.getComparator().getConvertedActualByPath());
    }
}
