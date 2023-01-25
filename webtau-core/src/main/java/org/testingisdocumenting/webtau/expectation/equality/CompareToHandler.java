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

import org.testingisdocumenting.webtau.data.ValuePath;

public interface CompareToHandler {
    /**
     * determines whether supports equality comparison
     * @param actual actual value
     * @param expected expected value
     * @return true if comparison can be handled
     */
    boolean handleEquality(Object actual, Object expected);

    /**
     * value optionally can be converted to another value to be passed down comparison chain.
     * exposed as outside method for more precise reporting of actual values in case of a failure.
     *
     * @param actual original actual
     * @return optionally converted actual
     */
    default Object convertedActual(Object actual) {
        return actual;
    }

    /**
     * determines whether supports greater/less than comparison family
     * @param actual actual value
     * @param expected expected value
     * @return true if comparison can be handled
     */
    default boolean handleGreaterLessEqual(Object actual, Object expected) {
        return false;
    }

    /**
     * determines whether handler can handle nulls. usually left unimplemented
     * @return true if handler can match nulls
     */
    default boolean handleNulls() {
        return false;
    }

    /**
     * implementation logic of equality only
     * @param comparator comparator to delegate comparison to
     * @param actualPath path to use for reporting
     * @param actual actual value
     * @param expected expected value
     */
    void compareEqualOnly(CompareToComparator comparator,
                          ValuePath actualPath, Object actual,
                          Object expected);

    /**
     * implementation logic of greater/less than family
     * @param comparator comparator to delegate comparison to
     * @param actualPath path to use for reporting
     * @param actual actual value
     * @param expected expected value
     */
    default void compareGreaterLessEqual(CompareToComparator comparator,
                                         ValuePath actualPath, Object actual,
                                         Object expected) {
        throw new UnsupportedOperationException("greater-less comparison is not implemented");
    }
}
