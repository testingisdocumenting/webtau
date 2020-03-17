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

package org.testingisdocumenting.webtau.expectation.equality;

import org.testingisdocumenting.webtau.data.render.DataRenderers;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;

class GreaterLessEqualMatcherRenderer {
    static String render(ValueMatcher valueMatcher, CompareToComparator.AssertionMode assertionMode, Object expected) {
        String renderedExpected = DataRenderers.render(expected);

        switch (assertionMode) {
            case GREATER_THAN:
                return "<greater than " + renderedExpected + ">";
            case GREATER_THAN_OR_EQUAL:
                return "<greater than or equal " + renderedExpected + ">";
            case LESS_THAN:
                return "<less than " + renderedExpected + ">";
            case LESS_THAN_OR_EQUAL:
                return "<less than or equal " + renderedExpected + ">";
        }

        throw new IllegalStateException("unexpected assertion mode: " + assertionMode);
    }
}
