/*
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

public interface ValueMatcher {
    // should

    /**
     * @return about to start matching message
     */
    String matchingMessage();

    /**
     * @param actualPath path to the value
     * @param actual actual value
     * @return match message
     * @see ActualPath
     */
    String matchedMessage(ActualPath actualPath, Object actual);

    /**
     * @param actualPath path to the value
     * @param actual actual value
     * @return mismatch message
     * @see ActualPath
     */
    String mismatchedMessage(ActualPath actualPath, Object actual);

    /**
     * Evaluates matcher. Called only for should
     * @param actualPath path to the value
     * @param actual actual value
     * @return true in case of a match
     * @see ActualPath
     */
    boolean matches(ActualPath actualPath, Object actual);

    // shouldNot

    /**
     * @return about to start negative matching (shouldNot case) message
     */
    String negativeMatchingMessage();

    /**
     * @param actualPath path to the value
     * @param actual actual value
     * @return negative match message (shouldNot case)
     * @see ActualPath
     */
    String negativeMatchedMessage(ActualPath actualPath, Object actual);

    /**
     * @param actualPath path to the value
     * @param actual actual value
     * @return negative mismatch message (shouldNot case)
     * @see ActualPath
     */
    String negativeMismatchedMessage(ActualPath actualPath, Object actual);

    /**
     * Evaluates matcher. Called only for shouldNot
     * @param actualPath path to the value
     * @param actual actual value
     * @return true in case of a negative match (shouldNot case)
     * @see ActualPath
     */
    boolean negativeMatches(ActualPath actualPath, Object actual);
}
