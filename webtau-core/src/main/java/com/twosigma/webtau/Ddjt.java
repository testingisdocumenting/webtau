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

package com.twosigma.webtau;

import com.twosigma.webtau.data.MultiValue;
import com.twosigma.webtau.data.table.TableData;
import com.twosigma.webtau.data.table.TableDataUnderscoreOrPlaceholder;
import com.twosigma.webtau.expectation.ActualCode;
import com.twosigma.webtau.expectation.ActualCodeExpectations;
import com.twosigma.webtau.expectation.ActualPath;
import com.twosigma.webtau.expectation.ActualValue;
import com.twosigma.webtau.expectation.ActualValueExpectations;
import com.twosigma.webtau.expectation.CodeBlock;
import com.twosigma.webtau.expectation.code.ThrowExceptionMatcher;
import com.twosigma.webtau.expectation.contain.ContainMatcher;
import com.twosigma.webtau.expectation.equality.EqualMatcher;
import com.twosigma.webtau.expectation.equality.GreaterThanMatcher;
import com.twosigma.webtau.expectation.equality.GreaterThanOrEqualMatcher;
import com.twosigma.webtau.expectation.equality.LessThanMatcher;
import com.twosigma.webtau.expectation.equality.LessThanOrEqualMatcher;
import com.twosigma.webtau.expectation.equality.NotEqualMatcher;
import com.twosigma.webtau.utils.CollectionUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Data Driven Java Testing
 * <p>
 * Convenient class for a single static * imports
 */
public class Ddjt {
    public static final TableDataUnderscoreOrPlaceholder __ = TableDataUnderscoreOrPlaceholder.INSTANCE;
    public static final TableDataUnderscoreOrPlaceholder ________ = TableDataUnderscoreOrPlaceholder.INSTANCE;
    public static final TableDataUnderscoreOrPlaceholder ________________ = TableDataUnderscoreOrPlaceholder.INSTANCE;
    public static final TableDataUnderscoreOrPlaceholder ________________________________ = TableDataUnderscoreOrPlaceholder.INSTANCE;
    public static final TableDataUnderscoreOrPlaceholder ________________________________________ = TableDataUnderscoreOrPlaceholder.INSTANCE;
    public static final TableDataUnderscoreOrPlaceholder ________________________________________________ = TableDataUnderscoreOrPlaceholder.INSTANCE;
    public static final TableDataUnderscoreOrPlaceholder ________________________________________________________________ = TableDataUnderscoreOrPlaceholder.INSTANCE;
    public static final TableDataUnderscoreOrPlaceholder ________________________________________________________________________________ = TableDataUnderscoreOrPlaceholder.INSTANCE;
    public static final TableDataUnderscoreOrPlaceholder ________________________________________________________________________________________________ = TableDataUnderscoreOrPlaceholder.INSTANCE;

    public static TableData table(String... columnNames) {
        return new TableData(Arrays.stream(columnNames));
    }

    public static TableData table(Object... columnNames) {
        return new TableData(Arrays.stream(columnNames));
    }

    public static MultiValue permute(Object atLeastOneValue, Object... values) {
        return new MultiValue(atLeastOneValue, values);
    }

    public static ActualValueExpectations actual(Object actual) {
        return new ActualValue(actual);
    }

    public static ActualCodeExpectations code(CodeBlock codeBlock) {
        return new ActualCode(codeBlock);
    }

    public static EqualMatcher equal(Object expected) {
        return new EqualMatcher(expected);
    }

    public static NotEqualMatcher notEqual(Object expected) {
        return new NotEqualMatcher(expected);
    }

    public static ContainMatcher contain(Object expected) {
        return new ContainMatcher(expected);
    }

    public static ContainMatcher containing(Object expected) {
        return new ContainMatcher(expected);
    }

    public static <K, V> Map<K, V> aMapOf(Object... kvs) {
        return CollectionUtils.aMapOf(kvs);
    }

    public static GreaterThanMatcher beGreaterThan(Object expected) {
        return new GreaterThanMatcher(expected);
    }

    public static GreaterThanOrEqualMatcher beGreaterThanOrEqual(Object expected) {
        return new GreaterThanOrEqualMatcher(expected);
    }

    public static LessThanMatcher beLessThan(Object expected) {
        return new LessThanMatcher(expected);
    }

    public static LessThanOrEqualMatcher beLessThanOrEqual(Object expected) {
        return new LessThanOrEqualMatcher(expected);
    }

    public static GreaterThanMatcher greaterThan(Object expected) {
        return beGreaterThan(expected);
    }

    public static GreaterThanOrEqualMatcher greaterThanOrEqual(Object expected) {
        return beGreaterThanOrEqual(expected);
    }

    public static LessThanMatcher lessThan(Object expected) {
        return beLessThan(expected);
    }

    public static LessThanOrEqualMatcher lessThanOrEqual(Object expected) {
        return beLessThanOrEqual(expected);
    }

    public static ThrowExceptionMatcher throwException(String expectedMessage) {
        return new ThrowExceptionMatcher(expectedMessage);
    }

    public static ThrowExceptionMatcher throwException(Pattern expectedMessageRegexp) {
        return new ThrowExceptionMatcher(expectedMessageRegexp);
    }

    public static ThrowExceptionMatcher throwException(Class expectedClass) {
        return new ThrowExceptionMatcher(expectedClass);
    }

    public static ThrowExceptionMatcher throwException(Class expectedClass, Pattern expectedMessageRegexp) {
        return new ThrowExceptionMatcher(expectedClass, expectedMessageRegexp);
    }

    public static ThrowExceptionMatcher throwException(Class expectedClass, String expectedMessage) {
        return new ThrowExceptionMatcher(expectedClass, expectedMessage);
    }

    public static ActualPath createActualPath(String path) {
        return new ActualPath(path);
    }
}
