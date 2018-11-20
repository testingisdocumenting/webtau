package com.twosigma.webtau.expectation.equality.handlers

import com.twosigma.webtau.data.live.LiveValue
import org.junit.Test

import java.time.LocalDate
import java.util.stream.Stream

import static com.twosigma.webtau.Ddjt.actual
import static com.twosigma.webtau.Ddjt.code
import static com.twosigma.webtau.Ddjt.equal
import static com.twosigma.webtau.Ddjt.lessThan
import static com.twosigma.webtau.Ddjt.throwException

class HandlerMessagesTest {
    // AnyCompareTo messages
    @Test
    void "equal objects"() {
        code {
            actual(new TestBean("foo")).should(equal(new TestBean("bar")))
        } should throwException(AssertionError, ~/expected: bar/)
    }

    @Test
    void "not equal objects"() {
        code {
            actual(new TestBean("foo")).shouldNot(equal(new TestBean("foo")))
        } should throwException(AssertionError, ~/expected: not foo/)
    }

    @Test
    void "not less than integers"() {
        code {
            actual(10).shouldNot(lessThan(20))
        } should throwException(AssertionError, ~/expected: >= 20/)
    }

    @Test
    void "less than integers"() {
        code {
            actual(10).should(lessThan(9))
        } should throwException(AssertionError, ~/expected: < 9/)
    }

    // DateAndStringCompareTo messages
    @Test
    void "equal date and string"() {
        code {
            actual("2018-10-31").should(equal(LocalDate.of(2018, 11, 1)))
        } should throwException(AssertionError, ~/expected: 2018-11-01/)
    }

    @Test
    void "not equal date and string"() {
        code {
            actual("2018-10-31").shouldNot(equal(LocalDate.of(2018, 10, 31)))
        } should throwException(AssertionError, ~/expected: not 2018-10-31/)
    }

    @Test
    void "less than date and string"() {
        code {
            actual("2018-10-31").should(lessThan(LocalDate.of(2018, 10, 31)))
        } should throwException(AssertionError, ~/expected: < 2018-10-31/)
    }

    // IterableCompareTo messages
    @Test
    void "equal iterables"() {
        code {
            actual(Arrays.asList(1)).should(equal(Arrays.asList(2)))
        } should throwException(AssertionError, ~/expected: 2/)
    }

    @Test
    void "not equal iterables"() {
        code {
            actual(Arrays.asList(1)).shouldNot(equal(Arrays.asList(1)))
        } should throwException(AssertionError, ~/expected: not 1/)
    }

    // LiveValueCompareTo messages
    @Test
    void "equal live value"() {
        code {
            actual(new TestLiveValue(1)).should(equal(2))
        } should throwException(AssertionError, ~/expected: 2/)
    }

    @Test
    void "not equal live value"() {
        code {
            actual(new TestLiveValue(1)).shouldNot(equal(1))
        } should throwException(AssertionError, ~/expected: not 1/)
    }

    // MapAndBeanCompareTo messages
    @Test
    void "equal map and bean"() {
        def expected = [
            prop: "b"
        ]

        code {
            actual(new TestBean("a")).should(equal(expected))
        } should throwException(AssertionError, ~/expected: "b"/)
    }

    @Test
    void "not equal map and bean"() {
        def expected = [
            prop: "a"
        ]

        code {
            actual(new TestBean("a")).shouldNot(equal(expected))
        } should throwException(AssertionError, ~/expected: not "a"/)
    }

    // MapsCompareTo messages
    @Test
    void "equal maps"() {
        def actualMap = [
            a: 1
        ]
        def expected = [
            a: 2
        ]

        code {
            actual(actualMap).should(equal(expected))
        } should throwException(AssertionError, ~/expected: 2/)
    }

    @Test
    void "not equal maps"() {
        def map = [
            a: 1
        ]

        code {
            actual(map).shouldNot(equal(map))
        } should throwException(AssertionError, ~/expected: not 1/)
    }

    // NullCompareTo messages
    @Test
    void "equal null"() {
        code {
            actual(1).should(equal(null))
        } should throwException(AssertionError, ~/expected: null/)
    }

    @Test
    void "not equal null"() {
        code {
            actual(null).shouldNot(equal(null))
        } should throwException(AssertionError, ~/expected: not null/)
    }

    // NumberAndStringCompareTo messages
    @Test
    void "equal number and string"() {
        code {
            actual("1").should(equal(2))
        } should throwException(AssertionError, ~/expected: 2/)
    }

    @Test
    void "not equal number and string"() {
        code {
            actual("1").shouldNot(equal(1))
        } should throwException(AssertionError, ~/expected: not 1/)
    }

    @Test
    void "less than number and string"() {
        code {
            actual("1").should(lessThan(0))
        } should throwException(AssertionError, ~/expected: < 0/)
    }

    // NumbersCompareTo messages
    @Test
    void "equal numbers"() {
        code {
            actual(1).should(equal(2.0))
        } should throwException(AssertionError, ~/expected: 2/)
    }

    @Test
    void "not equal numbers"() {
        code {
            actual(1).shouldNot(equal(1.0))
        } should throwException(AssertionError, ~/expected: not 1/)
    }

    @Test
    void "less than numbers"() {
        code {
            actual(1).should(lessThan(0.0))
        } should throwException(AssertionError, ~/expected: < 0/)
    }

    // RegexpEqualCompareTo messages
    @Test
    void "equal regexp"() {
        code {
            actual("foo").should(equal(~/bar/))
        } should throwException(AssertionError, ~/expected pattern: bar/)
    }

    @Test
    void "not equal regexp"() {
        code {
            actual("foo").shouldNot(equal(~/foo/))
        } should throwException(AssertionError, ~/expected pattern: not foo/)
    }

    // StreamAndIterableCompareTo messages
    @Test
    void "equal stream and iterable"() {
        code {
            actual(Stream.of(1)).should(equal(Arrays.asList(2)))
        } should throwException(AssertionError, ~/expected: 2/)
    }

    @Test
    void "not equal stream and iterable"() {
        code {
            actual(Stream.of(1)).shouldNot(equal(Arrays.asList(1)))
        } should throwException(AssertionError, ~/expected: not 1/)
    }

    // StringCompareTo messages
    @Test
    void "equal strings"() {
        code {
            actual("foo").should(equal("bar"))
        } should throwException(AssertionError, ~/expected: "bar"/)
    }

    @Test
    void "not equal strings"() {
        code {
            actual("foo").shouldNot(equal("foo"))
        } should throwException(AssertionError, ~/expected: not "foo"/)
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
