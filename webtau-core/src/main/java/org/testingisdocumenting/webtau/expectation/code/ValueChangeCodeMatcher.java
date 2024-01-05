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

package org.testingisdocumenting.webtau.expectation.code;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.CodeBlock;
import org.testingisdocumenting.webtau.expectation.CodeMatcher;
import org.testingisdocumenting.webtau.expectation.equality.CompareToComparator;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.function.Supplier;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class ValueChangeCodeMatcher implements CodeMatcher {
    private CompareToComparator comparator;
    private final Supplier<Object> valueSupplier;
    private final String label;

    public ValueChangeCodeMatcher(String label, Supplier<Object> valueSupplier) {
        this.label = label;
        this.valueSupplier = valueSupplier;
    }

    @Override
    public TokenizedMessage matchingTokenizedMessage() {
        comparator = CompareToComparator.comparator();
        return tokenizedMessage().matcher("to change value").of().id(label);
    }

    @Override
    public TokenizedMessage matchedTokenizedMessage(CodeBlock codeBlock) {
        return tokenizedMessage().matcher("changed value").of().id(label);
    }

    @Override
    public TokenizedMessage mismatchedTokenizedMessage(CodeBlock codeBlock) {
        return comparator.generateEqualMatchReport();
    }

    @Override
    public boolean matches(CodeBlock codeBlock) {
        comparator.resetReportData();

        var before = valueSupplier.get();
        codeBlock.execute();
        var after = valueSupplier.get();

        return comparator.compareIsNotEqual(new ValuePath(label), after, before);
    }

    @Override
    public TokenizedMessage negativeMatchingTokenizedMessage() {
        comparator = CompareToComparator.comparator(CompareToComparator.AssertionMode.NOT_EQUAL);
        return tokenizedMessage().matcher("to not change value").of().id(label);
    }

    @Override
    public TokenizedMessage negativeMatchedTokenizedMessage(CodeBlock codeBlock) {
        return tokenizedMessage().matcher("didn't change value").of().id(label);
    }

    @Override
    public TokenizedMessage negativeMismatchedTokenizedMessage(CodeBlock codeBlock) {
        return comparator.generateNotEqualMatchReport();
    }

    @Override
    public boolean negativeMatches(CodeBlock codeBlock) {
        comparator.resetReportData();

        var before = valueSupplier.get();
        codeBlock.execute();
        var after = valueSupplier.get();

        return comparator.compareIsEqual(new ValuePath(label), after, before);
    }
}
