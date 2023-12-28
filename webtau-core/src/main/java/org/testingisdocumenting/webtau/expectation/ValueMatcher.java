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

package org.testingisdocumenting.webtau.expectation;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.converters.ValueConverter;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

import java.util.Collections;
import java.util.Set;

public interface ValueMatcher {
    /**
     * value optionally can be converted to another value to be passed down to comparison chain.
     * matchers can optionally return value converters so reporting can render the best representation
     *
     * @return value converter
     */
    default ValueConverter valueConverter() {
        return ValueConverter.EMPTY;
    }

    // positive matching

    /**
     * @return about to start matching message
     */
    TokenizedMessage matchingTokenizedMessage(ValuePath actualPath, Object actual);

    /**
     * match details
     * @param actualPath path to the value
     * @param actual actual value
     * @return match message
     * @see ValuePath
     */
    TokenizedMessage matchedTokenizedMessage(ValuePath actualPath, Object actual);

    /**
     * match paths
     * @return paths that matched
     */
    default Set<ValuePath> matchedPaths() {
        return Collections.emptySet();
    }

    /**
     * @param actualPath path to the value
     * @param actual actual value
     * @return mismatch message
     * @see ValuePath
     */
    TokenizedMessage mismatchedTokenizedMessage(ValuePath actualPath, Object actual);

    /**
     * mismatch paths
     * @return paths that mismatched
     */
    default Set<ValuePath> mismatchedPaths() {
        return Collections.emptySet();
    }

    /**
     * Evaluates matcher. Called for should/wait
     * @param actualPath path to the value
     * @param actual actual value
     * @return true in case of a match
     * @see ValuePath
     */
    boolean matches(ValuePath actualPath, Object actual);

    // negative matching

    /**
     * @return about to start negative matching (shouldNot/waitToNot case) message
     */
    TokenizedMessage negativeMatchingTokenizedMessage(ValuePath actualPath, Object actual);

    /**
     * @param actualPath path to the value
     * @param actual actual value
     * @return negative match message (shouldNot/waitToNot case)
     * @see ValuePath
     */
    TokenizedMessage negativeMatchedTokenizedMessage(ValuePath actualPath, Object actual);

    /**
     * @param actualPath path to the value
     * @param actual actual value
     * @return negative mismatch message (shouldNot/waitToNot case)
     * @see ValuePath
     */
    TokenizedMessage negativeMismatchedTokenizedMessage(ValuePath actualPath, Object actual);

    /**
     * Evaluates matcher. Called only for shouldNot
     * @param actualPath path to the value
     * @param actual actual value
     * @return true in case of a negative match (shouldNot/waitToNot case)
     * @see ValuePath
     */
    boolean negativeMatches(ValuePath actualPath, Object actual);
}
