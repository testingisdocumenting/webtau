/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.contain;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.data.render.DataRenderers;
import org.testingisdocumenting.webtau.expectation.contain.handlers.IterableAndTableContainHandler;
import org.testingisdocumenting.webtau.expectation.contain.handlers.IterableAndSingleValueContainHandler;
import org.testingisdocumenting.webtau.expectation.contain.handlers.NullContainHandler;
import org.testingisdocumenting.webtau.expectation.equality.ActualPathMessage;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;
import org.testingisdocumenting.webtau.utils.ServiceLoaderUtils;
import org.testingisdocumenting.webtau.utils.TraceUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class ContainAnalyzer {
    private static final List<ContainHandler> handlers = discoverHandlers();

    private final List<ActualPathMessage> matches;
    private final List<ActualPathMessage> mismatches;

    private final List<Object> mismatchedExpectedValues;

    private ValuePath topLevelActualPath;

    private final Map<ValuePath, Object> convertedActualByPath = new HashMap<>();

    public static ContainAnalyzer containAnalyzer() {
        return new ContainAnalyzer();
    }

    public List<Object> getMismatchedExpectedValues() {
        return mismatchedExpectedValues;
    }

    public boolean contains(ValuePath actualPath, Object actual, Object expected) {
        updateTopLevelActualPath(actualPath);

        return contains(actual, expected, false,
                (handler) -> handler.analyzeContain(this, actualPath, actual, expected));
    }

    public boolean notContains(ValuePath actualPath, Object actual, Object expected) {
        updateTopLevelActualPath(actualPath);

        return contains(actual, expected, true,
                (handler) -> handler.analyzeNotContain(this, actualPath, actual, expected));
    }

    public ValueConverter createValueConverter() {
        return convertedActualByPath::getOrDefault;
    }

    public void reportMismatch(ContainHandler reporter, ValuePath actualPath, TokenizedMessage mismatch) {
        mismatches.add(new ActualPathMessage(actualPath, mismatch));
    }

    public void reportMismatch(ContainHandler reporter, ValuePath actualPath, TokenizedMessage mismatch, Object oneOfExpectedValues) {
        reportMismatch(reporter, actualPath, mismatch);
        mismatchedExpectedValues.add(oneOfExpectedValues);
    }

    public void reportMatch(ContainHandler reporter, ValuePath actualPath, TokenizedMessage mismatch) {
        matches.add(new ActualPathMessage(actualPath, mismatch));
    }

    public Set<ValuePath> generateMatchPaths() {
        return extractActualPaths(matches);
    }

    public Set<ValuePath> generateMismatchPaths() {
        return extractActualPaths(mismatches);
    }

    public TokenizedMessage generateMatchReport() {
        return TokenizedMessage.join("\n", matches.stream().map(message ->
                message.getActualPath().equals(topLevelActualPath) ?
                        message.getMessage() :
                        message.getFullMessage()).collect(Collectors.toList()));
    }

    public TokenizedMessage generateMismatchReport() {
        return !mismatches.isEmpty() ?
                tokenizedMessage().error("no match found") :
                tokenizedMessage();
    }

    public boolean noMismatches() {
        return mismatches.isEmpty();
    }

    public boolean noMatches() {
        return matches.isEmpty();
    }

    public void registerConvertedActualByPath(Map<ValuePath, Object> convertedActualByPath) {
        this.convertedActualByPath.putAll(convertedActualByPath);
    }

    private ContainAnalyzer() {
        this.matches = new ArrayList<>();
        this.mismatches = new ArrayList<>();
        this.mismatchedExpectedValues = new ArrayList<>();
    }

    private boolean contains(Object actual, Object expected, boolean isNegative, Consumer<ContainHandler> handle) {
        ContainHandler handler = handlers.stream().
                filter(h -> h.handle(actual, expected)).findFirst().
                orElseThrow(() -> noHandlerFound(actual, expected));

        int before = isNegative ? matches.size() :mismatches.size();
        handle.accept(handler);
        int after = isNegative ? matches.size() : mismatches.size();

        return after == before;
    }

    private Set<ValuePath> extractActualPaths(List<ActualPathMessage> notEqualMessages) {
        return notEqualMessages
                .stream()
                .map(ActualPathMessage::getActualPath)
                .collect(Collectors.toSet());
    }

    private void updateTopLevelActualPath(ValuePath actualPath) {
        if (topLevelActualPath == null) {
            topLevelActualPath = actualPath;
        }
    }

    private static List<ContainHandler> discoverHandlers() {
        List<ContainHandler> result = new ArrayList<>();
        result.add(new NullContainHandler());
        result.addAll(ServiceLoaderUtils.load(ContainHandler.class));
        result.add(new IterableAndTableContainHandler());
        result.add(new IterableAndSingleValueContainHandler());

        return result;
    }

    private RuntimeException noHandlerFound(Object actual, Object expected) {
        return new RuntimeException(
                "no contains handler found for\nactual: " + DataRenderers.render(actual) + " " + TraceUtils.renderType(actual) +
                        "\nexpected: " + DataRenderers.render(expected) + " " + TraceUtils.renderType(expected));
    }
}
