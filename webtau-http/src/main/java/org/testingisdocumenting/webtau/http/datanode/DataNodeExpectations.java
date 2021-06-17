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

package org.testingisdocumenting.webtau.http.datanode;

import org.testingisdocumenting.webtau.expectation.ActualPathAndDescriptionAware;
import org.testingisdocumenting.webtau.expectation.ActualValueExpectations;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.expectation.timer.ExpectationTimer;

import static org.testingisdocumenting.webtau.Matchers.*;

public interface DataNodeExpectations extends ActualValueExpectations, ActualPathAndDescriptionAware {
    @Override
    default void should(ValueMatcher valueMatcher) {
        actual(this).should(valueMatcher);
    }

    @Override
    default void shouldNot(ValueMatcher valueMatcher) {
        actual(this).shouldNot(valueMatcher);
    }

    @Override
    default void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void waitToNot(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        throw new UnsupportedOperationException();
    }
}
