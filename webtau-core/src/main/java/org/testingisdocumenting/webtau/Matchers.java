/*
 * Copyright 2021 webtau maintainers
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

package org.testingisdocumenting.webtau;

import org.testingisdocumenting.webtau.data.ValuePath;
import org.testingisdocumenting.webtau.data.live.LiveValue;
import org.testingisdocumenting.webtau.expectation.*;
import org.testingisdocumenting.webtau.expectation.code.ThrowExceptionMatcher;
import org.testingisdocumenting.webtau.expectation.contain.ContainAllMatcher;
import org.testingisdocumenting.webtau.expectation.contain.ContainMatcher;
import org.testingisdocumenting.webtau.expectation.equality.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;
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
     * Starting point of a value matcher with a provided name
     * <pre>
     * actual(price, "price").should(beGreaterThan(10));
     * actual(price, "price").shouldNot(beGreaterThan(10));
     * </pre>
     * Note: In Groovy you can just do <code>price.should beGreaterThan(10)</code>
     * @param actual value to assert against
     * @param path path to use in the reporting
     * @return Object to chain a matcher against
     */
    public static ActualValueExpectations actual(Object actual, String path) {
        return new ActualValue(actual, new ValuePath(path));
    }

    public static <E> ActualValueExpectations actual(Supplier<E> supplier) {
        return new ActualValue((LiveValue<E>) supplier::get);
    }

    /**
     * Starting point of a code matcher
     * <pre>
     * code(() -&gt; {
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
     * Contain all matcher
     * <pre>
     * actual(collection).should(containAll(list));
     * </pre>
     * @param expected collection of values to be contained in collection
     * @return matcher instance
     */
    public static ContainAllMatcher containAll(Collection<Object> expected) {
        return new ContainAllMatcher(expected);
    }

    /**
     * Contain all matcher
     * <pre>
     * actual(collection).should(containAll(2, 3, "a"));
     * </pre>
     * @param expected var arg of expected values
     * @return matcher instance
     */
    public static ContainAllMatcher containAll(Object... expected) {
        return new ContainAllMatcher(Arrays.asList(expected));
    }

    /**
     * Containing all matcher. Alias to containAll
     * <pre>
     * actual(listOfLists).should(contain(containingAll(myList)));
     * </pre>
     * @param expected collection of values to be contained in collection
     * @return matcher instance
     */
    public static ContainAllMatcher containingAll(Collection<Object> expected) {
        return new ContainAllMatcher(expected);
    }

    /**
     * Containing all matcher. Alias to containAll
     * <pre>
     * actual(listOfLists).should(contain(containingAll(2, 3, "a")));
     * </pre>
     * @param expected collection of values to be contained in collection
     * @return matcher instance
     */
    public static ContainAllMatcher containingAll(Object... expected) {
        return new ContainAllMatcher(Arrays.asList(expected));
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
     * Any of matcher
     * <pre>
     * actual(value).shouldBe(anyOf(3, greaterThan(8)));
     * </pre>
     * @param expected list of expected values or matchers
     * @return matcher instance
     */
    public static AnyOfMatcher anyOf(Object... expected) {
        return new AnyOfMatcher(Arrays.asList(expected));
    }

    /**
     * Any of matcher
     * <pre>
     * actual(value).shouldBe(anyOf(3, greaterThan(8)));
     * </pre>
     * @param expected list of expected values or matchers
     * @return matcher instance
     */
    public static AnyOfMatcher anyOf(Collection<Object> expected) {
        return new AnyOfMatcher(expected);
    }

    /**
     * Throw exception <code>code</code> matcher.
     * <pre>
     * code(() -&gt; {
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
     * code(() -&gt; {
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
     * code(() -&gt; {
     *      businessLogic(-10);
     * }).should(throwException(IllegalArgumentException.class));
     * </pre>
     * @see #code(CodeBlock)
     *
     * @param expectedClass expected exception class
     * @return matcher instance
     */
    public static ThrowExceptionMatcher throwException(Class<?> expectedClass) {
        return new ThrowExceptionMatcher(expectedClass);
    }

    /**
     * Throw exception <code>code</code> matcher.
     * <pre>
     * code(() -&gt; {
     *      businessLogic(-10);
     * }).should(throwException(IllegalArgumentException.class, Pattern.compile("negative .* not allowed")));
     * </pre>
     * @see #code(CodeBlock)
     *
     * @param expectedClass expected exception class
     * @param expectedMessageRegexp regular pattern to match expected exception message
     * @return matcher instance
     */
    public static ThrowExceptionMatcher throwException(Class<?> expectedClass, Pattern expectedMessageRegexp) {
        return new ThrowExceptionMatcher(expectedClass, expectedMessageRegexp);
    }

    /**
     * Throw exception <code>code</code> matcher.
     * <pre>
     * code(() -&gt; {
     *      businessLogic(-10);
     * }).should(throwException(IllegalArgumentException.class, "negatives are not allowed"));
     * </pre>
     * @see #code(CodeBlock)
     *
     * @param expectedClass expected exception class
     * @param expectedMessage expected exception message
     * @return matcher instance
     */
    public static ThrowExceptionMatcher throwException(Class<?> expectedClass, String expectedMessage) {
        return new ThrowExceptionMatcher(expectedClass, expectedMessage);
    }

    /**
     * @deprecated due to introduction of <code>should[Not]Be</code>, <code>waitTo[Not]</code> variants,
     * use {@link #greaterThan(Object)} instead
     * <pre>
     * actual(value).shouldBe(greaterThan(10));
     * </pre>
     * @param expected value to be greater than
     * @return matcher instance
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
     * @param expected value to be greater than or equal
     * @return matcher instance
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
     * @param expected value to be less than
     * @return matcher instance
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
     * @param expected value to be less than
     * @return matcher instance
     * @see #lessThanOrEqual(Object)
     */
    @Deprecated
    public static LessThanOrEqualMatcher beLessThanOrEqual(Object expected) {
        return lessThanOrEqual(expected);
    }
}
