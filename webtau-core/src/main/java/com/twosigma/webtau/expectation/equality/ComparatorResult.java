package com.twosigma.webtau.expectation.equality;

public class ComparatorResult {
    private boolean isMismatch;

    public ComparatorResult(boolean isMismatch) {
        this.isMismatch = isMismatch;
    }

    public boolean isMismatch() {
        return isMismatch;
    }
}
