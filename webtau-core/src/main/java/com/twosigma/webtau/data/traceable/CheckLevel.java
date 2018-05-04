package com.twosigma.webtau.data.traceable;

public enum CheckLevel {
    None,
    FuzzyPassed,
    ExplicitPassed,
    FuzzyFailed,
    ExplicitFailed;

    public boolean isFailed() {
        return this != None && this != FuzzyPassed && this != ExplicitPassed;
    }

    public boolean isPassed() {
        return this != None && !isFailed();
    }
}
