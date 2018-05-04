package com.twosigma.webtau.expectation;

import com.twosigma.webtau.Ddjt;
import com.twosigma.webtau.expectation.equality.EqualMatcher;

import java.util.function.Consumer;

public class ShouldAndWaitProperty<E> {
    private E actual;
    private Consumer<EqualMatcher> shouldHandler;

    public ShouldAndWaitProperty(E actual, Consumer<EqualMatcher> shouldHandler) {
        this.actual = actual;
        this.shouldHandler = shouldHandler;
    }

    public boolean equals(Object expected) {
        shouldHandler.accept(Ddjt.equal(expected));
        return true;
    }
}
