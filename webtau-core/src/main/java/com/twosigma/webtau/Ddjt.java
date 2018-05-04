package com.twosigma.webtau;

import com.twosigma.webtau.data.table.TableData;
import com.twosigma.webtau.expectation.*;
import com.twosigma.webtau.expectation.code.ThrowExceptionMatcher;
import com.twosigma.webtau.expectation.contain.ContainMatcher;
import com.twosigma.webtau.expectation.equality.EqualMatcher;
import com.twosigma.webtau.expectation.ranges.GreaterThanMatcher;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Data Driven Java Testing
 * <p>
 * Convenient class for a single static * imports
 */
public class Ddjt {
    public static TableData header(String... columnNames) {
        return new TableData(Arrays.stream(columnNames));
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

    public static ContainMatcher contain(Object expected) {
        return new ContainMatcher(expected);
    }

    public static GreaterThanMatcher beGreaterThan(Comparable base) {
        return new GreaterThanMatcher(base);
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
