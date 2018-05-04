/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.expectation

import com.twosigma.webtau.Ddjt


class Should {
    private Object actual

    Should(Object actual) {
        this.actual = actual
    }

    boolean equals(Object expected) {
        // TODO later replace with AST
        // this method will only have exception
        // also this won't work if actual is null as it won't even reach this place
        new ActualValue(actual).should(Ddjt.equal(expected))
        return true

        // throw new IllegalStateException("should not reach this place")
    }
}
