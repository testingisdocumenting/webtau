/*
 * Copyright 2024 webtau maintainers
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

import org.testingisdocumenting.webtau.data.SnapshotValueAware;
import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import static org.testingisdocumenting.webtau.WebTauCore.*;

public class SnapshotChangeValueMatcher implements ValueMatcher {
    private CompareToComparator comparator;

    @Override
    public TokenizedMessage matchingTokenizedMessage(ValuePath actualPath, Object actual) {
        comparator = CompareToComparator.comparator();
        return tokenizedMessage().matcher("to change");
    }

    @Override
    public TokenizedMessage matchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return tokenizedMessage().matcher("changed");
    }

    @Override
    public TokenizedMessage mismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return comparator.generateEqualMatchReport();
    }

    @Override
    public boolean matches(ValuePath actualPath, Object actual) {
        comparator.resetReportData();

        if (!(actual instanceof SnapshotValueAware)) {
            throw new IllegalArgumentException("actual value must implement SnapshotValueAware interface");
        }

        var before = ((SnapshotValueAware) actual).snapshotValue();
        var after = ((SnapshotValueAware) actual).actualValue();

        return comparator.compareIsNotEqual(actualPath, after, before);
    }

    @Override
    public TokenizedMessage negativeMatchingTokenizedMessage(ValuePath actualPath, Object actual) {
        return null;
    }

    @Override
    public TokenizedMessage negativeMatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return null;
    }

    @Override
    public TokenizedMessage negativeMismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        return null;
    }

    @Override
    public boolean negativeMatches(ValuePath actualPath, Object actual) {
        return false;
    }
}
