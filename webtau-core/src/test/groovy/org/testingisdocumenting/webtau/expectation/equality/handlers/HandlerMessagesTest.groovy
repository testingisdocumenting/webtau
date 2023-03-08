/*
 * Copyright 2023 webtau maintainers
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

package org.testingisdocumenting.webtau.expectation.equality.handlers

import org.testingisdocumenting.webtau.data.live.LiveValue
import org.junit.Test
import org.testingisdocumenting.webtau.testutils.TestConsoleOutput

import java.time.LocalDate
import java.util.regex.Pattern

import static org.testingisdocumenting.webtau.WebTauCore.*

class HandlerMessagesTest {
    // AnyCompareTo messages
    @Test
    void "equal objects"() {
        validateMessage(~/expected: bar/) {
            actual(new TestBean("foo")).should(equal(new TestBean("bar")))
        }
    }

    @Test
    void "not equal objects"() {
        validateMessage(~/expected: not foo/) {
            actual(new TestBean("foo")).shouldNot(equal(new TestBean("foo")))
        }
    }

    // Numbers messages
    @Test
    void "not less than integers"() {
        validateMessage(~/expected: greater than or equal to 20/) {
            actual(10).shouldNot(lessThan(20))
        }
    }

    @Test
    void "less than integers"() {
        validateMessage(~/expected: less than 9/) {
            actual(10).should(lessThan(9))
        }
    }

    // DateAndStringCompareTo messages
    @Test
    void "equal date and string"() {
        validateMessage(~/expected: 2018-11-01/) {
            actual("2018-10-31").should(equal(LocalDate.of(2018, 11, 1)))
        }
    }

    @Test
    void "not equal date and string"() {
        validateMessage(~/expected: not 2018-10-31/) {
            actual("2018-10-31").shouldNot(equal(LocalDate.of(2018, 10, 31)))
        }
    }

    @Test
    void "less than date and string"() {
        validateMessage(~/expected: less than 2018-10-31/) {
            actual("2018-10-31").should(lessThan(LocalDate.of(2018, 10, 31)))
        }
    }

    private static void validateMessage(Pattern expected, Closure code) {
        TestConsoleOutput.runExpectExceptionAndValidateOutput(AssertionError, expected, code)
    }

    // Test classes
    static class TestBean {
        private final String prop

        TestBean(String prop) {
            this.prop = prop
        }

        String getProp() {
            return prop
        }

        boolean equals(o) {
            if (this.is(o)) return true
            if (getClass() != o.class) return false

            TestBean testBean = (TestBean) o

            if (prop != testBean.prop) return false

            return true
        }

        int hashCode() {
            return (prop != null ? prop.hashCode() : 0)
        }

        @Override
        String toString() {
            return prop
        }
    }

    private static class TestLiveValue implements LiveValue<Integer> {
        private final Integer value

        TestLiveValue(Integer value) {
            this.value = value
        }

        @Override
        Integer get() {
            return value
        }

        @Override
        String toString() {
            return "TestLiveValue{" + value + '}'
        }
    }
}
