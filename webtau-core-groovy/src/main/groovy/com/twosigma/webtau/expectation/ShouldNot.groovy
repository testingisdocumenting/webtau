package com.twosigma.webtau.expectation

import com.twosigma.webtau.Ddjt

class ShouldNot {
    private Object actual

    ShouldNot(Object actual) {
        this.actual = actual
    }

    boolean equals(Object expected) {
        new ActualValue(actual).shouldNot(Ddjt.equal(expected))
        return true
    }
}
