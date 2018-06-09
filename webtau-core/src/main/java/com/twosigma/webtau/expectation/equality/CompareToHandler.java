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

package com.twosigma.webtau.expectation.equality;


import com.twosigma.webtau.expectation.ActualPath;

public interface CompareToHandler {
    boolean handleEquality(Object actual, Object expected);

    default boolean handleGreaterLessEqual(Object actual, Object expected) {
        return false;
    }

    default boolean handleNulls() {
        return false;
    }

    void compareEqualOnly(CompareToComparator comparator, ActualPath actualPath, Object actual, Object expected);

    default void compareGreaterLessEqual(CompareToComparator compareToComparator, ActualPath actualPath, Object actual, Object expected) {
        throw new UnsupportedOperationException("doesn't handle greater-less comparison");
    }
}
