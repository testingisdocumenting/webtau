package com.twosigma.webtau;

import com.twosigma.webtau.expectation.*;
import com.twosigma.webtau.expectation.code.ThrowExceptionMatcher;
import com.twosigma.webtau.expectation.contain.ContainMatcher;
import com.twosigma.webtau.expectation.equality.*;

import java.util.regex.Pattern;

/**
 * Convenient place to discover all the available matchers
 */
public class Matchers {
    /**
     * Starting point of a value matcher
     * <pre>
     * actual(value).should(beGreaterThan(10));
     * actual(value).shouldNot(beGreaterThan(10));
     * </pre>
     * Note: In Groovy you can just do <code>value.should beGreaterThan(10)</code>
     * @param actual value to assert against
     * @return Object to chain a matcher against
     */
    public static ActualValueExpectations actual(Object actual) {
        return new ActualValue(actual);
    }

    /**
     * Starting point of a code matcher
     * <pre>
     * code(() -> {
     *    businessLogic(-10);
     * }).should(throwException(IllegalArgumentException.class, "negatives are not allowed"));
     * </pre>
     *
     * @param codeBlock code to match against
     * @return Object to chain a matcher against
     */
    public static ActualCodeExpectations code(CodeBlock codeBlock) {
        return new ActualCode(codeBlock);
    }

    /**
     * Equal matcher
     * <pre>
     * actual(value).should(equal(10));
     * </pre>
     * @param expected value to be equal to
     * @return matcher instance
     */
    public static EqualMatcher equal(Object expected) {
        return new EqualMatcher(expected);
    }

    /**
     * Not equal matcher
     * <pre>
     * actual(value).should(notEqual(10));
     * </pre>
     * @param expected value to not be equal to
     * @return matcher instance
     */
    public static NotEqualMatcher notEqual(Object expected) {
        return new NotEqualMatcher(expected);
    }

    /**
     * Contain matcher
     * <pre>
     * actual(collection).should(contain(10));
     * actual(text).should(contain("hello"));
     * </pre>
     * @param expected value to be contained
     * @return matcher instance
     */
    public static ContainMatcher contain(Object expected) {
        return new ContainMatcher(expected);
    }

    /**
     * Containing matcher. Alias to contain
     * <pre>
     * actual(collectionWithText).should(contain(containing("hello")));
     * </pre>
     * @param expected value to be contained
     * @return matcher instance
     */
    public static ContainMatcher containing(Object expected) {
        return new ContainMatcher(expected);
    }

    /**
     * Greater than matcher
     * <pre>
     * actual(value).shouldBe(greaterThan(10));
     * </pre>
     * @param expected value to be greater than
     * @return matcher instance
     */
    public static GreaterThanMatcher greaterThan(Object expected) {
        return new GreaterThanMatcher(expected);
    }

    /**
     * Greater than or equal matcher
     * <pre>
     * actual(value).shouldBe(greaterThanOrEqual(10));
     * </pre>
     * @param expected value to be greater than or equal
     * @return matcher instance
     */
    public static GreaterThanOrEqualMatcher greaterThanOrEqual(Object expected) {
        return new GreaterThanOrEqualMatcher(expected);
    }

    /**
     * Less than matcher
     * <pre>
     * actual(value).shouldBe(lessThan(10));
     * </pre>
     * @param expected value to be less than
     * @return matcher instance
     */
    public static LessThanMatcher lessThan(Object expected) {
        return new LessThanMatcher(expected);
    }

    /**
     * Less than or equal matcher
     * <pre>
     * actual(value).shouldBe(lessThanOrEqual(10));
     * </pre>
     * @param expected value to be less than
     * @return matcher instance
     */
    public static LessThanOrEqualMatcher lessThanOrEqual(Object expected) {
        return new LessThanOrEqualMatcher(expected);
    }

    /**
     * Throw exception <code>code</code> matcher.
     * <pre>
     * code(() -> {
     *     businessLogic(-10);
     * }).should(throwException("negatives are not allowed"));
     * </pre>
     * @see #code(CodeBlock)
     *
     * @param expectedMessage expected exception message
     * @return matcher instance
     */
    public static ThrowExceptionMatcher throwException(String expectedMessage) {
        return new ThrowExceptionMatcher(expectedMessage);
    }

    /**
     * Throw exception <code>code</code> matcher.
     * <pre>
     * code(() -> {
     *      businessLogic(-10);
     * }).should(throwException(Pattern.compile("negative .* not allowed")));
     * </pre>
     * @see #code(CodeBlock)
     *
     * @param expectedMessageRegexp regular pattern to match expected exception message
     * @return matcher instance
     */
    public static ThrowExceptionMatcher throwException(Pattern expectedMessageRegexp) {
        return new ThrowExceptionMatcher(expectedMessageRegexp);
    }

    /**
     * Throw exception <code>code</code> matcher.
     * <pre>
     * code(() -> {
     *      businessLogic(-10);
     * }).should(throwException(IllegalArgumentException.class));
     * </pre>
     * @see #code(CodeBlock)
     *
     * @param expectedClass expected exception class
     * @return matcher instance
     */
    public static ThrowExceptionMatcher throwException(Class expectedClass) {
        return new ThrowExceptionMatcher(expectedClass);
    }

    /**
     * Throw exception <code>code</code> matcher.
     * <pre>
     * code(() -> {
     *      businessLogic(-10);
     * }).should(throwException(IllegalArgumentException.class, Pattern.compile("negative .* not allowed")));
     * </pre>
     * @see #code(CodeBlock)
     *
     * @param expectedClass expected exception class
     * @param expectedMessageRegexp regular pattern to match expected exception message
     * @return matcher instance
     */
    public static ThrowExceptionMatcher throwException(Class expectedClass, Pattern expectedMessageRegexp) {
        return new ThrowExceptionMatcher(expectedClass, expectedMessageRegexp);
    }

    /**
     * Throw exception <code>code</code> matcher.
     * <pre>
     * code(() -> {
     *      businessLogic(-10);
     * }).should(throwException(IllegalArgumentException.class, "negatives are not allowed"));
     * </pre>
     * @see #code(CodeBlock)
     *
     * @param expectedClass expected exception class
     * @param expectedMessage expected exception message
     * @return matcher instance
     */
    public static ThrowExceptionMatcher throwException(Class expectedClass, String expectedMessage) {
        return new ThrowExceptionMatcher(expectedClass, expectedMessage);
    }

    /**
     * @deprecated due to introduction of <code>should[Not]Be</code>, <code>waitTo[Not]</code> variants,
     * use {@link #greaterThan(Object)} instead
     * <pre>
     * actual(value).shouldBe(greaterThan(10));
     * </pre>
     * @see #greaterThan(Object)
     */
    @Deprecated
    public static GreaterThanMatcher beGreaterThan(Object expected) {
        return greaterThan(expected);
    }

    /**
     * @deprecated due to introduction of <code>should[Not]Be</code>, <code>waitTo[Not]</code> variants,
     * use {@link #greaterThanOrEqual(Object)} instead
     * <pre>
     * actual(value).shouldBe(greaterThanOrEqual(10));
     * </pre>
     * @see #greaterThanOrEqual(Object)
     */
    @Deprecated
    public static GreaterThanOrEqualMatcher beGreaterThanOrEqual(Object expected) {
        return greaterThanOrEqual(expected);
    }

    /**
     * @deprecated due to introduction of <code>should[Not]Be</code>, <code>waitTo[Not]</code> variants,
     * use {@link #lessThan(Object)} instead
     * <pre>
     * actual(value).shouldBe(lessThan(10));
     * </pre>
     * @see #lessThan(Object)
     */
    @Deprecated
    public static LessThanMatcher beLessThan(Object expected) {
        return lessThan(expected);
    }

    /**
     * @deprecated due to introduction of <code>should[Not]Be</code>, <code>waitTo[Not]</code> variants,
     * use {@link #lessThanOrEqual(Object)} instead
     * <pre>
     * actual(value).shouldBe(lessThanOrEqual(10));
     * </pre>
     * @see #lessThanOrEqual(Object)
     */
    @Deprecated
    public static LessThanOrEqualMatcher beLessThanOrEqual(Object expected) {
        return lessThanOrEqual(expected);
    }
}
