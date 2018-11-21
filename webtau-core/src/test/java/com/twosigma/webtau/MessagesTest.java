package com.twosigma.webtau;

import com.google.common.truth.Truth;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static com.twosigma.webtau.Ddjt.actual;
import static com.twosigma.webtau.Ddjt.equal;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class MessagesTest {
    // Pure Junit
    @Test
    public void junitEquals() {
        assertEquals(1, 2);
    }

    @Test
    public void junitNotEquals() {
        assertNotEquals(1, 1);
    }

    // Hamcrest
    @Test
    public void hamcrestEquals() {
        assertThat(1, equalTo(2));
    }

    @Test
    public void hamcrestNotEquals() {
        assertThat(1, not(equalTo(1)));
    }

    @Test
    public void hamcrestLessThan() {
        assertThat(1, lessThan(1));
    }

    @Test
    public void hamcrestNotLessThan() {
        assertThat(1, not(lessThan(2)));
    }

    // Truth
    @Test
    public void truthEquals() {
        Truth.assertThat(1).isEqualTo(2);
    }

    @Test
    public void truthNotEquals() {
        Truth.assertThat(1).isNotEqualTo(1);
    }

    @Test
    public void truthLessThan() {
        Truth.assertThat(1).isLessThan(1);
    }

    // AssertJ
    @Test
    public void assertJEquals() {
        Assertions.assertThat(1).isEqualTo(2);
    }

    @Test
    public void assertJNotEquals() {
        Assertions.assertThat(1).isNotEqualTo(1);
    }

    @Test
    public void assertJLessThan() {
        Assertions.assertThat(1).isLessThan(1);
    }

    // Webtau
    @Test
    public void webtauEquals() {
        actual(1).should(equal(2));
    }

    @Test
    public void webtauNotEquals() {
        actual(1).shouldNot(equal(1));
    }

    @Test
    public void webtauLessThan() {
        actual(1).should(Ddjt.lessThan(1));
    }

    @Test
    public void webtauNotLessThan() {
        actual(1).shouldNot(Ddjt.lessThan(2));
    }
}
