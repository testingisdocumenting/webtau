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

import static org.testingisdocumenting.webtau.WebTauCore.*;

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

    // should

    /**
     * @deprecated override {@link #matchingTokenizedMessage(ValuePath, Object)} instead
     * @return about to start matching message
     */
    @Deprecated
    default String matchingMessage() {
        return "";
    }

    /**
     * @return about to start matching message
     */
    default TokenizedMessage matchingTokenizedMessage(ValuePath actualPath, Object actual) {
        String message = matchingMessage();
        if (message.isEmpty()) {
            throw new IllegalStateException("either matchingMessage(deprecated) or matchingTokenizedMessage must be implemented");
        }

        return tokenizedMessage().matcher(matchingMessage());
    }

    /**
     * @deprecated use {@link #matchedTokenizedMessage(ValuePath, Object)} instead
     */
    @Deprecated
    default String matchedMessage(ValuePath actualPath, Object actual) {
        return "";
    }

    /**
     * match details
     * @param actualPath path to the value
     * @param actual actual value
     * @return match message
     * @see ValuePath
     */
    default TokenizedMessage matchedTokenizedMessage(ValuePath actualPath, Object actual) {
        String message = matchedMessage(actualPath, actual);
        if (message.isEmpty()) {
            throw new IllegalStateException("either matchedMessage(deprecated) or matchedTokenizedMessage must be implemented");
        }

        return tokenizedMessage().matcher(matchedMessage(actualPath,actual));
    }

    /**
     * match paths
     * @return list of paths that matched
     */
    default Set<ValuePath> matchedPaths() {
        return Collections.emptySet();
    }

    /**
     * @deprecated use {@link #mismatchedTokenizedMessage} ()} instead
     */
    @Deprecated
    default String mismatchedMessage(ValuePath actualPath, Object actual) {
        return "";
    }

    /**
     * @param actualPath path to the value
     * @param actual actual value
     * @return mismatch message
     * @see ValuePath
     */
    default TokenizedMessage mismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        String message = mismatchedMessage(actualPath, actual);
        if (message.isEmpty()) {
            throw new IllegalStateException("either mismatchedMessage(deprecated) or mismatchedTokenizedMessage must be implemented");
        }

        return tokenizedMessage().error(mismatchedMessage(actualPath,actual));
    }

    /**
     * mismatch paths
     * @return list of paths that mismatched
     */
    default Set<ValuePath> mismatchedPaths() {
        return Collections.emptySet();
    }

    /**
     * Evaluates matcher. Called only for should
     * @param actualPath path to the value
     * @param actual actual value
     * @return true in case of a match
     * @see ValuePath
     */
    boolean matches(ValuePath actualPath, Object actual);

    // shouldNot

    /**
     * @deprecated use {@link #negativeMatchingTokenizedMessage} ()} instead
     */
    @Deprecated
    default String negativeMatchingMessage() {
        return "";
    }

    /**
     * @return about to start negative matching (shouldNot case) message
     */
    default TokenizedMessage negativeMatchingTokenizedMessage(ValuePath actualPath, Object actual) {
        String message = negativeMatchingMessage();
        if (message.isEmpty()) {
            throw new IllegalStateException("either negativeMatchingMessage(deprecated) or negativeMatchingTokenizedMessage must be implemented");
        }

        return tokenizedMessage().matcher(negativeMatchingMessage());
    }

    /*
     * @deprecated use {@link #negativeMatchedTokenizedMessage()} instead
     */
    @Deprecated
    default String negativeMatchedMessage(ValuePath actualPath, Object actual) {
        return "";
    }

    /**
     * @param actualPath path to the value
     * @param actual actual value
     * @return negative match message (shouldNot case)
     * @see ValuePath
     */
    default TokenizedMessage negativeMatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        String message = negativeMatchedMessage(actualPath, actual);
        if (message.isEmpty()) {
            throw new IllegalStateException("either negativeMatchedMessage(deprecated) or negativeMatchedTokenizedMessage must be implemented");
        }

        return tokenizedMessage().matcher(message);
    }

    /*
     * @deprecated use {@link #negativeMismatchedTokenizedMessage()} instead
     */
    @Deprecated
    default String negativeMismatchedMessage(ValuePath actualPath, Object actual) {
        return "";
    }

    /**
     * @param actualPath path to the value
     * @param actual actual value
     * @return negative mismatch message (shouldNot case)
     * @see ValuePath
     */
    default TokenizedMessage negativeMismatchedTokenizedMessage(ValuePath actualPath, Object actual) {
        String message = negativeMismatchedMessage(actualPath, actual);
        if (message.isEmpty()) {
            throw new IllegalStateException("either negativeMismatchedMessage(deprecated) or negativeMismatchedTokenizedMessage must be implemented");
        }

        return tokenizedMessage().error(message);
    }

    /**
     * Evaluates matcher. Called only for shouldNot
     * @param actualPath path to the value
     * @param actual actual value
     * @return true in case of a negative match (shouldNot case)
     * @see ValuePath
     */
    boolean negativeMatches(ValuePath actualPath, Object actual);
}
