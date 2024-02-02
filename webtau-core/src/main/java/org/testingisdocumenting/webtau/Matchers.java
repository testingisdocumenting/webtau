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
import org.testingisdocumenting.webtau.data.converters.ObjectProperties;
import org.testingisdocumenting.webtau.data.live.LiveValue;
import org.testingisdocumenting.webtau.expectation.*;
import org.testingisdocumenting.webtau.expectation.code.ValueChangeCodeMatcher;
import org.testingisdocumenting.webtau.expectation.code.ThrowExceptionMatcher;
import org.testingisdocumenting.webtau.expectation.contain.ContainAllMatcher;
import org.testingisdocumenting.webtau.expectation.contain.ContainExactlyMatcher;
import org.testingisdocumenting.webtau.expectation.contain.ContainMatcher;
import org.testingisdocumenting.webtau.expectation.equality.*;
import org.testingisdocumenting.webtau.expectation.state.HiddenValueMatcher;
import org.testingisdocumenting.webtau.expectation.state.VisibleValueMatcher;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Convenient place to discover all the available matchers
 */
public class Matchers {
    /**
     * any value matcher. use it in places like table or complex structures where you want to ignore a value
     */
    public static final ValueMatcher anyValue = new AnyValueMatcher();

    /**
     * snapshot based value change matcher. Check/wait for value to change/remain the same
     */
    public static final ValueMatcher change = new SnapshotChangeValueMatcher();

    /**
     * visible matcher to check if UI element is visible
     * @see #hidden
     */
    public static final ValueMatcher visible = new VisibleValueMatcher();

    /**
     * hidden matcher to check if UI element is hidden
     * @see #visible
     */
    public static final ValueMatcher hidden = new HiddenValueMatcher();

    /**
     * Starting point of a value matcher
     * <pre>
     * actual(value).should(beGreaterThan(10));
     * actual(value).shouldNot(beGreaterThan(10));
     * </pre>
     * Note: In Groovy you can do <code>value.shouldBe > 10</code>
     * @param actual value to assert against
     * @return Object to chain a matcher against
     */
    public static <E> ActualValueExpectations actual(E actual) {
        return new ActualValue(actual);
    }

    /**
     * Starting point of a value matcher with a provided name
     * <pre>
     * actual(price, "price").should(beGreaterThan(10));
     * actual(price, "price").shouldNot(beGreaterThan(10));
     * </pre>
     * Note: In Groovy you can do <code>price.shouldBe > 10</code>
     * @param actual value to assert against
     * @param path path to use in the reporting
     * @return Object to chain a matcher against
     */
    public static ActualValueExpectations actual(Object actual, String path) {
        return new ActualValue(actual, new ValuePath(path));
    }

    /**
     * wraps supplier into an instance of LiveValue
     * <pre>
     * actual(liveValue(this::consumeMessage)).waitTo(equal("message we wait for"));
     * </pre>
     * @param supplier value supplier that will be re-queried if required
     * @return LiveValue instance
     */
    public static <E> LiveValue<E> liveValue(Supplier<E> supplier) {
        return supplier::get;
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
     * Contain exactly matcher
     * <pre>
     * actual(collection).should(containExact(el1, el2, el3));
     * </pre>
     * @param expected vararg list of values to check
     * @return matcher instance
     */
    public static ContainExactlyMatcher containExactly(Object... expected) {
        return new ContainExactlyMatcher(Arrays.asList(expected));
    }

    /**
     * Contain exactly matcher
     * <pre>
     * actual(collection).should(containExact(el1, el2, el3));
     * </pre>
     * @param expected list of values to check
     * @return matcher instance
     */
    public static ContainExactlyMatcher containExactly(Iterable<Object> expected) {
        return new ContainExactlyMatcher(expected);
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
     * Same instance
     * <pre>
     * actual(value).shouldBe(sameInstance(anotherValue));
     * </pre>
     * @param expected expected instance
     * @return matcher instance
     */
    public static SameInstanceMatcher sameInstance(Object expected) {
        return new SameInstanceMatcher(expected);
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
     * @return code matcher instance
     */
    public static ThrowExceptionMatcher throwException(String expectedMessage) {
        return new ThrowExceptionMatcher(expectedMessage);
    }

    /**
     * Throw exception <code>code</code> matcher.
     * <pre>
     * code(() -&gt; {
     *     businessLogic(-10);
     * }).should(throwException(contain("negatives are not")));
     * </pre>
     * @see #code(CodeBlock)
     *
     * @param expectedMessageMatcher expected exception message ValueMatcher
     * @return code matcher instance
     */
    public static ThrowExceptionMatcher throwException(ValueMatcher expectedMessageMatcher) {
        return new ThrowExceptionMatcher(expectedMessageMatcher);
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
     * @return code matcher instance
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
     * @return code matcher instance
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
     * @return code matcher instance
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
     * @return code matcher instance
     */
    public static ThrowExceptionMatcher throwException(Class<?> expectedClass, String expectedMessage) {
        return new ThrowExceptionMatcher(expectedClass, expectedMessage);
    }


    /**
     * Throw exception <code>code</code> matcher.
     * <pre>
     * code(() -&gt; {
     *     businessLogic(-10);
     * }).should(throwException(IllegalArgumentException.class, contain("negatives are not")));
     * </pre>
     * @see #code(CodeBlock)
     *
     * @param expectedClass expected exception class
     * @param expectedMessageMatcher expected exception message ValueMatcher
     * @return matcher instance
     */
    public static ThrowExceptionMatcher throwException(Class<?> expectedClass, ValueMatcher expectedMessageMatcher) {
        return new ThrowExceptionMatcher(expectedClass, expectedMessageMatcher);
    }
  
    /**
     * Value change <code>code</code> matcher
     * <pre>
     * code(() -> {
     *     updateDbEntity(dbEntity);
     * }).should(change("dbEntity.id", dbEntity::getId));
     * </pre>
     * @param label expression label to use in reporting
     * @param valueSupplier value supplier to get before/after values for comparison
     * @return code matcher instance
     */
    public static ValueChangeCodeMatcher change(String label, Supplier<Object> valueSupplier) {
        return new ValueChangeCodeMatcher(label, valueSupplier);
    }

    /**
     * Object properties change <code>code</code> matcher
     * <pre>
     * code(() -> {
     *     buggyOperation(dbEntity);
     * }).should(changeSomeProperties("dbEntity", dbEntity));
     * </pre>
     * @param label expression label to use in reporting
     * @param object object which properties will be extracted for before/after comparison
     * @return code matcher instance
     */
    public static ValueChangeCodeMatcher change(String label, Object object) {
        // case for Groovy closures to avoid them being treated as Java Beans
        if (object instanceof Callable) {
            return new ValueChangeCodeMatcher(label, () -> {
                try {
                    return ((Callable<?>) object).call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return new ValueChangeCodeMatcher(label, () -> new ObjectProperties(object));
    }
}
