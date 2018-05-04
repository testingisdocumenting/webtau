package com.twosigma.webtau.expectation;

public class ActualCode implements ActualCodeExpectations {
    private CodeBlock actual;

    public ActualCode(CodeBlock actual) {
        this.actual = actual;
    }

    @Override
    public void should(CodeMatcher codeMatcher) {
        boolean matches = codeMatcher.matches(actual);
        if (! matches) {
            throw new AssertionError("\n" + codeMatcher.mismatchedMessage(actual));
        }
    }
}
