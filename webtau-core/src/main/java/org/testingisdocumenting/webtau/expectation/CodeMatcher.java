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

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

public interface CodeMatcher {
    /**
     * @return about to start matching message
     */
    TokenizedMessage matchingTokenizedMessage();

    /**
     * @param codeBlock matching code block
     * @return match message
     */
    TokenizedMessage matchedTokenizedMessage(CodeBlock codeBlock);

    /**
     * @param codeBlock matching code block
     * @return mismatch message
     */
    TokenizedMessage mismatchedTokenizedMessage(CodeBlock codeBlock);

    boolean matches(CodeBlock codeBlock);

    /**
     * @return about to start negative matching (shouldNot/waitToNot case) message
     */
    TokenizedMessage negativeMatchingTokenizedMessage();

    /**
     * @param codeBlock matching code block
     * @return negative match message (shouldNot/waitToNot case)
     * @see ValuePath
     */
    TokenizedMessage negativeMatchedTokenizedMessage(CodeBlock codeBlock);

    /**
     * @param codeBlock matching code block
     * @return negative mismatch message (shouldNot/waitToNot case)
     * @see ValuePath
     */
    TokenizedMessage negativeMismatchedTokenizedMessage(CodeBlock codeBlock);

    /**
     * Evaluates matcher. Called only for shouldNot/waitToNot
     * @param codeBlock matching code block
     * @return true in case of a negative match
     * @see ValuePath
     */
    boolean negativeMatches(CodeBlock codeBlock);
}
