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
import org.testingisdocumenting.webtau.reporter.TokenizedMessage;

public interface ExpectationHandler {
    enum Flow {
        Terminate,
        PassToNext
    }

    default Flow onValueMismatch(ValueMatcher valueMatcher, ValuePath actualPath, Object actualValue, TokenizedMessage message) {
        return Flow.PassToNext;
    }

    default void onValueMatch(ValueMatcher valueMatcher, ValuePath actualPath, Object actualValue) {}

    default Flow onCodeMismatch(CodeMatcher codeMatcher, TokenizedMessage message) {
        return Flow.PassToNext;
    }

    default void onCodeMatch(CodeMatcher codeMatcher) {}
}
